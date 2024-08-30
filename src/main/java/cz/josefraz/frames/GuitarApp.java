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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class GuitarApp extends JFrame {

    private int leftHSplitPanelDividerWidth = 200;
    // TODO auto location
    // Height of bottom part of v SplitPanel
    private int bottomVSPlitPanelHeight = 220;
    // For checking if vSPlitPanel divider is being dragged
    private boolean isVSplitPanelDividerDragged = false;

    public GuitarApp() {
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
        JMenuItem connectDevice = new JMenuItem("Connect");
        deviceMenu.add(connectDevice);
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
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        JMenuItem lightThemeItem = new JMenuItem("Light", lightIcon);
        lightThemeItem.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
                SwingUtilities.updateComponentTreeUI(frame);
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        themeMenu.add(darkThemeItem);
        themeMenu.add(lightThemeItem);
        menuBar.add(themeMenu);
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
        JPanel outputPanel = new JPanel();
        bottomTabbedPane.addTab("Output", outputPanel);

        // Create a SplitPane to divide main parta nd bottom panel
        JSplitPane vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainTabbedPane, bottomTabbedPane);
        vSplitPane.setDividerLocation(getHeight() - bottomVSPlitPanelHeight);
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
                    bottomVSPlitPanelHeight = (newValue - frame.getHeight()) * -1;
                }
            }
        });

        // Listener to change vSplitPanel divider position on window resize
        addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                vSplitPane.setDividerLocation(getHeight() - bottomVSPlitPanelHeight);
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
                System.out.println(leftHSplitPanelDividerWidth);
            }
        });

        // Add the main panel to the window
        add(hSplitPane, BorderLayout.CENTER);

        // Status panel https://stackoverflow.com/a/3035893/19371130
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new EmptyBorder(0, 5, 0, 0));
        statusPanel.setBackground(Color.orange);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 24));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        JLabel statusLabel = new JLabel("Not connected. Go to Device > Connect");
        statusLabel.setForeground(Color.white);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);

        // Display the window
        setVisible(true);
    }
}
