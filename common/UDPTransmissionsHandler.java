import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPTransmissionsHandler extends TransmissionsHandler {

    private DatagramSocket socket;
    private InetAddress sendAddress;
    private int sendPort;

    public UDPTransmissionsHandler (int serverPort, InetAddress serverAddress) throws SocketException {
        this.socket = new DatagramSocket();
        this.sendAddress = serverAddress;
        this.sendPort = serverPort;
    }

    public UDPTransmissionsHandler (DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public <T> T readData() throws IOException, ClassNotFoundException {
        byte[] rawData = new byte[getMessageLength()];
        DatagramPacket inData = new DatagramPacket(rawData, rawData.length);
        socket.receive(inData);

        return Utils.deserializeObject(rawData);
    }

    @Override
    public <T> void writeData(T object) throws IOException {
        byte[] rawData = Utils.serializeObject(object);
        sendMessageLength(rawData.length);
        DatagramPacket outData = new DatagramPacket(rawData, rawData.length, sendAddress, sendPort);
        socket.send(outData);
    }

    @Override
    public void close() {
        this.socket.close();
    }

    private int getMessageLength() throws IOException {
        byte[] rawData = new byte[4];
        DatagramPacket inData = new DatagramPacket(rawData, rawData.length);
        this.socket.receive(inData);
        return Utils.deserializeInteger(rawData);
    }

    private void sendMessageLength(int length) throws IOException {
        byte[] rawData = Utils.serializeInteger(length);
        DatagramPacket outData = new DatagramPacket(rawData, rawData.length, sendAddress, sendPort);
        socket.send(outData);
    }

    public void connect(InetAddress address, int port) {
        this.sendAddress = address;
        this.sendPort = port;
    }
}
