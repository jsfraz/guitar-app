package cz.josefraz.frames;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class GuitarApp extends JFrame {

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
        
        // Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createLineBorder(borderColor));

        // Add tabs
        JPanel editorPanel = new JPanel();
        tabbedPane.addTab("Editor", editorPanel);
        JPanel previewPanel = new JPanel();
        tabbedPane.addTab("Preview", previewPanel);

        // Create a SplitPane to divide the left panel and the tabbed pane
        JSplitPane hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lPanel, tabbedPane);
        hSplitPane.setOneTouchExpandable(true); // Allows full hiding and showing of the left panel
        // TODO auto location
        hSplitPane.setDividerLocation(200); // Initial width of the left panel
        hSplitPane.setResizeWeight(0); // Left panel resizes while the right panel remains the same

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
