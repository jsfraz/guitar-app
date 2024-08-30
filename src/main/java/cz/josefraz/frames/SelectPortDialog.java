package cz.josefraz.frames;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;

public class SelectPortDialog extends JFrame {

    public SelectPortDialog(GuitarApp mainWindow) {
        super("Select serial port");

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        // Port combo box
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
        for (SerialPort port : serialPorts) {
            model.addElement(port.getDescriptivePortName());
        }
        JComboBox<String> serialPortComboBox = new JComboBox<String>(model);
        contentPane.add(serialPortComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            Optional<SerialPort> port = Arrays.stream(serialPorts)
                    .filter(p -> p.getDescriptivePortName().equals((String) serialPortComboBox.getSelectedItem()))
                    .findFirst();
            if (port.isPresent()) {
                mainWindow.openSerialPort(port.get());
            } else {
                JOptionPane.showMessageDialog(null, String.format("Serial port %s is not available",
                        (String) serialPortComboBox.getSelectedItem()), "Error", JOptionPane.ERROR_MESSAGE);
            }
            mainWindow.setEnabled(true);
            dispose();
        });

        buttonPanel.add(okButton);
        // Space between buttons
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            mainWindow.setEnabled(true);
            dispose();
        });
        buttonPanel.add(cancelButton);
        buttonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        // Vertical alignment
        JPanel buttonPanelContainer = new JPanel();
        buttonPanelContainer.setLayout(new BoxLayout(buttonPanelContainer, BoxLayout.X_AXIS));
        buttonPanelContainer.add(Box.createHorizontalGlue());
        buttonPanelContainer.add(buttonPanel);
        buttonPanelContainer.add(Box.createHorizontalGlue());

        contentPane.add(buttonPanelContainer);

        // Enable main window when dialog is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainWindow.setEnabled(true);
                dispose();
            }
        });
        
        setContentPane(contentPane);
        setSize(300, 180);
        setResizable(false);
        setLocationRelativeTo(mainWindow);
        setVisible(true);
    }
}
