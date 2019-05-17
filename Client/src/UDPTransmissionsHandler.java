import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPTransmissionsHandler extends TransmissionsHandler {

    private DatagramSocket socket;

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
        DatagramPacket outData = new DatagramPacket(rawData, rawData.length);
        socket.send(outData);
    }

    private int getMessageLength() throws IOException {
        byte[] rawData = new byte[4];
        DatagramPacket inData = new DatagramPacket(rawData, rawData.length);
        this.socket.receive(inData);
        return Utils.deserializeInteger(rawData);
    }

    private void sendMessageLength(int length) throws IOException {
        byte[] rawData = Utils.serializeInteger(length);
        DatagramPacket outData = new DatagramPacket(rawData, rawData.length);
        socket.send(outData);
    }
}
