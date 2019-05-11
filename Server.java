import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;
import java.sql.Timestamp;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class Server {
    public static void main(String[] args) {
        System.out.println("TEST");
        connectToServer();
    }

    class UDPThread extends Thread {
        DatagramSocket UDPSocket;
        private byte[] buf = new byte[256];

        public UDPThread(DatagramSocket socket) {
            this.UDPSocket = socket;
        }

        public void run() {
            while(true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try (Socket connectionSocket = this.UDPSocket.receive(packet)) {
                    ClientHandler UDPClientHandler = new UDPClientHandler(connectionSocket);
                    UDPClientHandler.start();
                }

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
            }
        }
    }
    class TCPThread extends Thread {
        ServerSocket TCPSocket;

        public TCPThread(ServerSocket socket) {
            this.TCPSocket = socket;
        }

        public void run() {
            while(true) {
                try(Socket connectionSocket = this.TCPSocket.accept()) {
                    ClientHandler TCPClientHandler = new TCPClientHandler(connectionSocket);
                    TCPClientHandler.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        }
    }

    public static void connectTCP() {
        //Try to connect the server on an unused port. Successful connection will return a socket
        try(ServerSocket TCPSocket = new ServerSocket(5001)) {
            TCPThread tcp = new TCPThread(TCPSocket);
            tcp.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void connectUDP() {
        try(DatagramSocket UDPSocket = new DatagramSocket(5001)) {
            UDPThread udp = new UDPThread(UDPSocket);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void connectToServer() {

    }

    InetAddress ip = InetAddress.getLocalHost();
    Integer port = 5002;
    Timestamp time = new Timestamp(System.currentTimeMillis());
    String command = "ls";

    Message msg = new Message(ip, port, time, command);

}