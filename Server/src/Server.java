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

    static final int UDP_PORT = 5001;
    static final int TCP_PORT = 5002;

    public static void main(String[] args) {
        System.out.println("TEST");
        connectToServer();
    }

    private static class UDPThread extends Thread {
        DatagramSocket UDPSocket;
        private byte[] buf = new byte[256];

        public UDPThread(DatagramSocket socket) {
            this.UDPSocket = socket;
        }

        @Override
        public void run() {
            while(true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try (Socket connectionSocket = this.UDPSocket.receive(packet)) {
                    ClientHandler UDPClientHandler = new UDPClientHandler(connectionSocket);
                    UDPClientHandler.start();
                }
            }
        }
    }
    private static class TCPThread extends Thread {
        ServerSocket tcpSocket;

        TCPThread(ServerSocket socket) {
            this.tcpSocket = socket;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket connectionSocket = this.tcpSocket.accept();
                    ClientHandler tcpClientHandler = new TCPClientHandler(connectionSocket);
                    tcpClientHandler.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        }
    }

    private static void initTCP() {
        //Try to connect the server on an unused port. Successful connection will return a socket
        try {
            ServerSocket tcpSocket = new ServerSocket(TCP_PORT);
            Thread tcpThread = new TCPThread(tcpSocket);
            tcpThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initUDP() {
        try {
            DatagramSocket udpSocket = new DatagramSocket(UDP_PORT);
            Thread udpThread = new UDPThread(udpSocket);
            udpThread.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private static void initServer() {
        getUserPorts();
        initTCP();
        initUDP();
    }

    private static void getUserPorts() {
        // TODO change ports variables depending on user input
    }


}