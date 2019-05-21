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

    // executes the handling of transmission handler request only once
    @Override
    public void run() {
        try {
            manageRequest();
        } catch (IOException ignored) {}
        transmissionsHandler.close();
    }

    // readMessage returns the message read from the main udp server thread
    @Override
    protected String readMessage() {
        return message;
    }

    // send functions dispatch to transmissionHandler

    @Override
    protected void sendMessage(String message) throws IOException {
        transmissionsHandler.sendMessage(message);
    }

    @Override
    protected void sendFile(File file) throws IOException {
        transmissionsHandler.sendFile(file);
    }
}