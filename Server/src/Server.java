import java.net.*;
import java.io.*;

public class Server {

    static final int UDP_PORT = 5001;
    static final int TCP_PORT = 5002;

    public static void main(String[] args) {
        initServer();
    }

    private static class UDPThread extends Thread {
        DatagramSocket udpSocket;
        private byte[] buf = new byte[256];

        UDPThread(DatagramSocket socket) {
            this.udpSocket = socket;
        }

        @Override
        public void run() {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    this.udpSocket.receive(packet);
                    ClientHandler udpClientHandler = new UDPClientHandler(packet);
                    udpClientHandler.start();
                } catch (IOException e) {
                    e.printStackTrace();
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