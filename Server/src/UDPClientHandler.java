import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPClientHandler extends ClientHandler {

    String message;

    UDPClientHandler(DatagramPacket messagePackets, DatagramSocket udpSocket) {
        transmissionsHandler = new UDPTransmissionsHandler(udpSocket);
        message = new String(messagePackets.getData(), 0, messagePackets.getLength());
        ((UDPTransmissionsHandler)transmissionsHandler).connect(messagePackets.getAddress(), messagePackets.getPort());
    }

    @Override
    public void run() {
        try {
            manageRequest();
        } catch (IOException ignored) {}
    }

    @Override
    protected String readMessage() {
        return message;
    }

    @Override
    protected void sendMessage(String message) throws IOException {
        transmissionsHandler.sendMessage(message);
    }

    @Override
    protected void sendFile(File file) throws IOException {
        transmissionsHandler.sendFile(file);
    }
}