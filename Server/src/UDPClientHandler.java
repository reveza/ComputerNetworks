import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPClientHandler extends ClientHandler {

    UDPClientHandler(DatagramPacket messagePackets, DatagramSocket udpSocket) {
        transmissionsHandler = new UDPTransmissionsHandler(udpSocket);
        ((UDPTransmissionsHandler)transmissionsHandler).connect(messagePackets.getAddress(), messagePackets.getPort());
    }

    @Override
    public void run() {
        try {
            manageRequest();
        } catch (IOException ignored) {}
    }
}