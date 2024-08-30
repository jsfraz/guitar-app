package cz.josefraz.utils;

import com.fazecast.jSerialComm.SerialPort;

public class Singleton { 
    private static Singleton instance;
    private SerialPort serialPort;
 
    private Singleton() { 
    } 
 
    public static Singleton GetInstance() { 
        if (instance == null) { 
            instance = new Singleton(); 
        } 
        return instance; 
    } 

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }
} 