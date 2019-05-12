import java.io.*;
import java.net.DatagramPacket;

public class UDPClientHandler extends ClientHandler {
    private DatagramPacket connectionSocket;

    UDPClientHandler(DatagramPacket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
    }

    public void run() {
        manageRequest();
    }

    protected String readData() {
        // TODO read data from udp
        return "";
    }
    protected void writeData(String message) {
        // TODO write string to udp
    }
    protected void writeData(File file) {
        // TODO write file to udp
    }
}