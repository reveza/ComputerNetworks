import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClientHandler extends ClientHandler {
    private DatagramPacket messagePackets;
    private DatagramSocket udpSocket;

    UDPClientHandler(DatagramPacket messagePackets, DatagramSocket udpSocket) {
        this.messagePackets = messagePackets;
        this.udpSocket = udpSocket;
    }

    @Override
    public void run() {
        manageRequest();
    }

    @Override
    protected String readData() {
        return new String(this.messagePackets.getData());
    }

    @Override
    protected void writeData(String message) {
        final byte[] buf = message.getBytes();

        sendData(buf);
    }

    private void sendData(byte[] buf) {
        InetAddress address = this.messagePackets.getAddress();
        int port = this.messagePackets.getPort();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            this.udpSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void writeData(File file) {
        final byte[] buf = fileToBytes(file);

        sendData(buf);
    }

    private static byte[] fileToBytes(File file){
        FileInputStream fis = null;
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();      
            
        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }
        return bArray;
    }
}