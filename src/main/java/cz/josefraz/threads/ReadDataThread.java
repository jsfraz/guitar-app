package cz.josefraz.threads;

import java.nio.charset.StandardCharsets;

import javax.swing.JTextArea;

import com.fazecast.jSerialComm.SerialPort;

import cz.josefraz.utils.Singleton;

public class ReadDataThread extends Thread {

    private JTextArea deviceOutputTextArea;
    private boolean running;

    public ReadDataThread(JTextArea deviceOutputTextArea) {
        this.deviceOutputTextArea = deviceOutputTextArea;
        running = true;
    }

    public void run() {
        try {
            while (running) {
                // Read data from serial port
                SerialPort serialPort = Singleton.GetInstance().getSerialPort();
                byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                serialPort.readBytes(readBuffer, readBuffer.length);
                deviceOutputTextArea.append(new String(readBuffer, StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Stop the loop
    public void terminate() {
        this.running = false;
    }
}
