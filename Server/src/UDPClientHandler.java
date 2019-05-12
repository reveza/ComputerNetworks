import java.io.*;
import java.net.DatagramPacket;

public class UDPClientHandler extends ClientHandler {
    private DatagramPacket messagePackets;

    UDPClientHandler(DatagramPacket messagePackets) {
        this.messagePackets = messagePackets;
    }

    @Override
    public void run() {
        manageRequest();
    }

    @Override
    protected String readData() {
        // TODO read data from udp
        return "";
    }

    @Override
    protected void writeData(String message) {
        // TODO write string to udp
    }

    @Override
    protected void writeData(File file) {
        // TODO write file to udp
    }
}