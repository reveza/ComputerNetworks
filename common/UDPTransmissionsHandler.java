import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class UDPTransmissionsHandler extends TransmissionsHandler {

    private static final int BUFFER_SIZE = 1024;

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
    public String readMessage() throws IOException {
        byte[] rawData = new byte[BUFFER_SIZE];
        DatagramPacket inData = new DatagramPacket(rawData, rawData.length);
        socket.receive(inData);
        return new String(inData.getData(), 0, inData.getLength());
    }

    @Override
    public File readFile(String fileName) throws IOException {
        File file = new File(Utils.UDP_DIRECTORY + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] rawData = new byte[BUFFER_SIZE];
        while (true) {
            DatagramPacket inData = new DatagramPacket(rawData, rawData.length);
            socket.receive(inData);
            fileOutputStream.write(inData.getData(), 0, inData.getLength());
            if (inData.getLength() < BUFFER_SIZE) {
                break;
            }
        }
        fileOutputStream.close();

        return file;
    }

    @Override
    public void sendMessage(String message) throws IOException {
        byte[] rawData = message.getBytes();
        DatagramPacket outData = new DatagramPacket(rawData, rawData.length, sendAddress, sendPort);
        socket.send(outData);
    }

    @Override
    public void sendFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] rawData = new byte[BUFFER_SIZE];
        int length;
        while ((length = fileInputStream.read(rawData, 0, rawData.length)) != -1) {
            DatagramPacket outData = new DatagramPacket(rawData, length, sendAddress, sendPort);
            socket.send(outData);
        }

        fileInputStream.close();
    }

    @Override
    public void close() {
        this.socket.close();
    }

    public void connect(InetAddress address, int port) {
        this.sendAddress = address;
        this.sendPort = port;
    }
}
