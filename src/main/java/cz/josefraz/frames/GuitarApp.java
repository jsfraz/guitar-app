package cz.josefraz.frames;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

import java.awt.*;

public class GuitarApp extends JFrame {

    public GuitarApp() {
        // Set the title of the window
        setTitle("G.U.I.T.A.R");
        JFrame frame = this;
        Color borderColor = new Color(97, 99, 101);

        // Icon
        ImageIcon icon = new ImageIcon(getClass().getResource("/guitar.png"));
        this.setIconImage(icon.getImage());

        // Set the base layout to BorderLayout
        setLayout(new BorderLayout());

        // Create the top menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        fileMenu.add(newMenuItem);
        menuBar.add(fileMenu);

        // Create the "Theme" menu
        JMenu themeMenu = new JMenu("Theme");

        // TODO icons
        // Create icons for Dark and Light themes
        ImageIcon darkIcon = new ImageIcon("moon_icon.png");
        ImageIcon lightIcon = new ImageIcon("sun_icon.png");

        // Theme buttons
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
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createLineBorder(borderColor));
        leftPanel.setMinimumSize(new Dimension(0, 0)); // Minimum width of 0 for full hiding

        // Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createLineBorder(borderColor));

        // Add tabs
        JPanel editorPanel = new JPanel();
        tabbedPane.addTab("Editor", editorPanel);

        JPanel previewPanel = new JPanel();
        tabbedPane.addTab("Preview", previewPanel);

        // Create a SplitPane to divide the left panel and the tabbed pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, tabbedPane);
        splitPane.setOneTouchExpandable(true); // Allows full hiding and showing of the left panel
        // TODO auto location
        splitPane.setDividerLocation(200); // Initial width of the left panel
        splitPane.setResizeWeight(0); // Left panel resizes while the right panel remains the same

        // Add the main panel to the window
        add(splitPane, BorderLayout.CENTER);

        // Set the size of the window and the minimum size
        setSize(800, 600);
        setMinimumSize(new Dimension(640, 360));

        // Exit the program when the window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Display the window
        setVisible(true);
    }
}
