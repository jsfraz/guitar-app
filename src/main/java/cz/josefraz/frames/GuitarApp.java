package cz.josefraz.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.fazecast.jSerialComm.SerialPort;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import cz.josefraz.threads.ReadDataThread;
import cz.josefraz.utils.Command;
import cz.josefraz.utils.CustomOutputStream;
import cz.josefraz.utils.Guitar;
import cz.josefraz.utils.Singleton;

public class GuitarApp extends JFrame {
    // Width of left part og SplitPanel
    private int leftHSplitPanelDividerWidth = 200;
    // Height of bottom part of v SplitPanel
    private int bottomVSplitPanelHeight = 220;
    // For checking if vSPlitPanel divider is being dragged
    private boolean isVSplitPanelDividerDragged = false;

    // Connect to device item
    private JMenuItem connectDeviceItem;
    // Disonnect device item
    private JMenuItem disconnectDeviceItem;
    // Commands
    JMenu commandMenu;
    // Device output
    private JTextArea deviceOutputTextArea;
    // Program logs
    private JTextArea programLogTextArea;
    // Status panel
    private JPanel statusPanel;
    // Status panel label
    private JLabel statusLabel;

    // Thread for reading data form serial port
    private ReadDataThread readDataThread;

    private final Color disconnectedStatusPanelColor = new Color(240, 167, 50);
    private final String disconnectedStatusPanelMsg = "Not connected. Go to Device > Connect";

    public GuitarApp() {
        System.out.println("Loading main window");

        // Set the title of the window
        setTitle("G.U.I.T.A.R");
        JFrame frame = this;
        Color borderColor = new Color(97, 99, 101);
        // Set the base layout to BorderLayout
        setLayout(new BorderLayout());
        // Set the size of the window and the minimum size
        setSize(800, 600);
        setMinimumSize(new Dimension(640, 360));
        // Exit the program when the window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Icon
        ImageIcon icon = new ImageIcon(getClass().getResource("/guitar.png"));
        this.setIconImage(icon.getImage());

        // Create the top menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        fileMenu.add(newMenuItem);
        menuBar.add(fileMenu);
        JMenu deviceMenu = new JMenu("Device");
        connectDeviceItem = new JMenuItem("Connect");
        connectDeviceItem.addActionListener(e -> {
            setEnabled(false);
            new SelectPortDialog(this);
        });
        deviceMenu.add(connectDeviceItem);
        disconnectDeviceItem = new JMenuItem("Disconnect");
        disconnectDeviceItem.addActionListener(e -> {
            readDataThread.terminate();
            Singleton.GetInstance().getSerialPort().closePort();
            Singleton.GetInstance().setSerialPort(null);
            System.out.println("Closed serial port");
            deviceOutputTextArea.append("---- Closed serial port ----\n");
            statusPanel.setBackground(disconnectedStatusPanelColor);
            statusLabel.setText(disconnectedStatusPanelMsg);
            connectDeviceItem.setEnabled(true);
            disconnectDeviceItem.setEnabled(false);
            commandMenu.setEnabled(false);
        });
        disconnectDeviceItem.setEnabled(false);
        deviceMenu.add(disconnectDeviceItem);
        menuBar.add(deviceMenu);
        JMenu themeMenu = new JMenu("Theme");
        // TODO icons
        ImageIcon darkIcon = new ImageIcon("moon_icon.png");
        ImageIcon lightIcon = new ImageIcon("sun_icon.png");
        JMenuItem darkThemeItem = new JMenuItem("Dark", darkIcon);
        darkThemeItem.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                SwingUtilities.updateComponentTreeUI(frame);
                System.out.println("Dark theme set");
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        JMenuItem lightThemeItem = new JMenuItem("Light", lightIcon);
        lightThemeItem.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
                SwingUtilities.updateComponentTreeUI(frame);
                System.out.println("Light theme set");
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        themeMenu.add(darkThemeItem);
        themeMenu.add(lightThemeItem);
        menuBar.add(themeMenu);
        JMenu outputMenu = new JMenu("Output");
        JMenuItem deviceOutputClearItem = new JMenuItem("Clear device output");
        deviceOutputClearItem.addActionListener(e -> {
            deviceOutputTextArea.setText(null);
        });
        outputMenu.add(deviceOutputClearItem);
        JMenuItem programLogClearItem = new JMenuItem("Clear program logs");
        programLogClearItem.addActionListener(e -> {
            programLogTextArea.setText(null);
        });
        outputMenu.add(programLogClearItem);
        menuBar.add(outputMenu);
        commandMenu = new JMenu("Command");
        commandMenu.setEnabled(false);
        JMenuItem helloCommandItem = new JMenuItem("Hello");
        helloCommandItem.addActionListener(e -> {
            // Send HELLO command
            Guitar.command(Command.HELLO);
        });
        commandMenu.add(helloCommandItem);
        JMenuItem systemInfoCommandItem = new JMenuItem("System info");
        systemInfoCommandItem.addActionListener(e -> {
            // Send HELLO command
            Guitar.command(Command.SYSTEMINFO);
        });
        commandMenu.add(systemInfoCommandItem);
        menuBar.add(commandMenu);
        setJMenuBar(menuBar);

        // Create the left panel
        JPanel lPanel = new JPanel();
        lPanel.setBorder(BorderFactory.createLineBorder(borderColor));
        lPanel.setMinimumSize(new Dimension(0, 0)); // Minimum width of 0 for full hiding

        // Create main tabbed pane
        JTabbedPane mainTabbedPane = new JTabbedPane();
        mainTabbedPane.setBorder(BorderFactory.createLineBorder(borderColor));

        // Add tabs
        JPanel editorPanel = new JPanel();
        mainTabbedPane.addTab("Editor", editorPanel);
        JPanel previewPanel = new JPanel();
        mainTabbedPane.addTab("Preview", previewPanel);

        // Create bottom tabbed pane
        JTabbedPane bottomTabbedPane = new JTabbedPane();
        bottomTabbedPane.setBorder(BorderFactory.createLineBorder(borderColor));

        // Add tabs
        JPanel problemPanel = new JPanel();
        bottomTabbedPane.addTab("Problems", problemPanel);
        // Device output
        deviceOutputTextArea = new JTextArea();
        deviceOutputTextArea.setEditable(false);
        bottomTabbedPane.addTab("Device output", new JScrollPane(deviceOutputTextArea));
        // Logs
        programLogTextArea = new JTextArea();
        programLogTextArea.setEditable(false);
        bottomTabbedPane.addTab("Program logs", new JScrollPane(programLogTextArea));

        // Create a SplitPane to divide main parta nd bottom panel
        JSplitPane vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainTabbedPane, bottomTabbedPane);
        vSplitPane.setDividerLocation(getHeight() - bottomVSplitPanelHeight);
        vSplitPane.setResizeWeight(0);
        // Divider event listener
        BasicSplitPaneUI vSPlitPaneUi = (BasicSplitPaneUI) vSplitPane.getUI();
        vSPlitPaneUi.getDivider().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isVSplitPanelDividerDragged = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isVSplitPanelDividerDragged = false;
            }

        });
        // Get bottom height when value is changed by dragging divider
        vSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (isVSplitPanelDividerDragged) {
                    int newValue = (int) evt.getNewValue();
                    bottomVSplitPanelHeight = (newValue - frame.getHeight()) * -1;
                }
            }
        });

        // Listener to change vSplitPanel divider position on window resize
        addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                vSplitPane.setDividerLocation(getHeight() - bottomVSplitPanelHeight);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

        });

        // Create a SplitPane to divide the left panel and the tabbed pane
        JSplitPane hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lPanel, vSplitPane);
        hSplitPane.setDividerLocation(leftHSplitPanelDividerWidth); // Initial width of the left panel
        hSplitPane.setResizeWeight(0); // Left panel resizes while the right panel remains the same
        // Get left width when value is changed by dragging divider
        hSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                leftHSplitPanelDividerWidth = (int) evt.getNewValue();
            }
        });

        // Add the main panel to the window
        add(hSplitPane, BorderLayout.CENTER);

        // Status panel https://stackoverflow.com/a/3035893/19371130
        statusPanel = new JPanel();
        statusPanel.setBorder(new EmptyBorder(0, 5, 0, 0));
        statusPanel.setBackground(disconnectedStatusPanelColor);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 24));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel(disconnectedStatusPanelMsg);
        statusLabel.setForeground(Color.white);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);

        // Initialize custom output stream
        PrintStream printStream = new PrintStream(new CustomOutputStream(programLogTextArea));
        System.setOut(printStream);
        System.setErr(printStream);

        // Display the window
        setVisible(true);
        System.out.println("Main window loaded");
        System.out.println(String.format("%s %s, %s, Java %s", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"), System.getProperty("java.version")));
    }
    
    // Opens serial port
    public void openSerialPort(SerialPort serialPort) {
        // Serial port settings
        serialPort.setBaudRate(115200);
        serialPort.setNumDataBits(8);
        serialPort.setNumStopBits(1);
        serialPort.setParity(0);
        Singleton.GetInstance().setSerialPort(serialPort);

        // Open
        boolean opened = Singleton.GetInstance().getSerialPort().openPort();
        if (opened) {
            deviceOutputTextArea.append("---- Opened serial port ----\n");
            this.connectDeviceItem.setEnabled(false);
            this.disconnectDeviceItem.setEnabled(true);
            this.commandMenu.setEnabled(true);
            // Port path for Linux, friendly name for other
            if (System.getProperty("os.name").startsWith("Linux")) {
                System.out.println(String.format("Opened serial port %s", Singleton.GetInstance().getSerialPort().getSystemPortPath()));
            } else {
                System.out.println(String.format("Opened serial port %s", Singleton.GetInstance().getSerialPort().getDescriptivePortName()));
            }

            // Change status panel
            statusPanel.setBackground(new Color(76, 135, 200));
            statusLabel.setText("Connected!");

            // Start thread for reading output
            readDataThread = new ReadDataThread(deviceOutputTextArea);
            readDataThread.start();

            // Sends SYSTEMINFO command
            Guitar.command(Command.SYSTEMINFO);
        }
    }
}
