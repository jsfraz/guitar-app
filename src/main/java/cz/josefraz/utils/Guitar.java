package cz.josefraz.utils;

public class Guitar {
    
    // Sends command
    public static void command(Command command) {
        byte[] bytes = String.format("%s;%s", MessageType.CMD, command).getBytes();
        Singleton.GetInstance().getSerialPort().writeBytes(bytes, bytes.length);
    }
}
