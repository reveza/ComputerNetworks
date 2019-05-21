import java.io.*;
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

    // reads a string with max length of BUFFER_SIZE
    @Override
    public String readMessage() throws IOException {
        byte[] rawData = new byte[BUFFER_SIZE];
        DatagramPacket inData = new DatagramPacket(rawData, rawData.length);
        socket.receive(inData);
        return new String(inData.getData(), 0, inData.getLength());
    }

    // receives and writes file in udp directory
    @Override
    public File readFile(String fileName) throws IOException {
        File file = new File(Utils.UDP_DIRECTORY + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] rawData = new byte[BUFFER_SIZE];
        // awaits for DatagramPacket until it has received end datagram packet identifiable with length 0
        while (true) {
            DatagramPacket inData = new DatagramPacket(rawData, rawData.length);
            socket.receive(inData);
            if (inData.getLength() == 0) {
                break;
            }
            fileOutputStream.write(inData.getData(), 0, inData.getLength());
        }
        fileOutputStream.close();

        return file;
    }

    // sends the BUFFER_SIZE first characters of the string (if in UTF-8)
    @Override
    public void sendMessage(String message) throws IOException {
        byte[] rawData = message.getBytes();
        DatagramPacket outData = new DatagramPacket(rawData, rawData.length, sendAddress, sendPort);
        socket.send(outData);
    }

    // sends the file through a series of DatagramPackets
    @Override
    public void sendFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] rawData = new byte[BUFFER_SIZE];
        int length;
        while ((length = fileInputStream.read(rawData, 0, rawData.length)) != -1) {
            DatagramPacket outData = new DatagramPacket(rawData, length, sendAddress, sendPort);
            socket.send(outData);
        }
        // ends file transmission with end datagram packet with length 0
        DatagramPacket outData = new DatagramPacket(rawData, 0, sendAddress, sendPort);
        socket.send(outData);

        fileInputStream.close();
    }

    // closes the socket
    @Override
    public void close() {
        this.socket.close();
    }

    // sets up the port and address of the destination
    public void connect(InetAddress address, int port) {
        this.sendAddress = address;
        this.sendPort = port;
    }
}
