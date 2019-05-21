import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {

    static int UDP_PORT = 0;
    static int TCP_PORT = 1;

    public static void main(String[] args) {
        initServer();
    }

    // Main udp thread, receives first udp datagram packet and sends it to another thread that can handle the request
    private static class UDPThread extends Thread {
        DatagramSocket udpSocket;
        private byte[] buf = new byte[TransmissionsHandler.BUFFER_SIZE];

        UDPThread(DatagramSocket socket) {
            this.udpSocket = socket;
        }

        // running thread
        @Override
        public void run() {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    // awaits datagram packet
                    this.udpSocket.receive(packet);
                    // handles it
                    ClientHandler udpClientHandler = new UDPClientHandler(packet, this.udpSocket);
                    udpClientHandler.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Main tcp thread, receives first a connection request from client, and starts a thread that handles all requests of specified this client
    private static class TCPThread extends Thread {
        ServerSocket tcpSocket;

        TCPThread(ServerSocket socket) {
            this.tcpSocket = socket;
        }

        // running thread
        @Override
        public void run() {
            while (true) {
                try {
                    // accepts connection from client
                    Socket connectionSocket = this.tcpSocket.accept();
                    // handles all client requests
                    ClientHandler tcpClientHandler = new TCPClientHandler(connectionSocket);
                    tcpClientHandler.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    // starts the main tcp thread on specified port
    private static void initTCP() {
        try {
            ServerSocket tcpSocket = new ServerSocket(TCP_PORT);
            Thread tcpThread = new TCPThread(tcpSocket);
            tcpThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // starts the main udp thread on specified port
    private static void initUDP() {
        try {
            DatagramSocket udpSocket = new DatagramSocket(UDP_PORT, InetAddress.getLocalHost());
            Thread udpThread = new UDPThread(udpSocket);
            udpThread.start();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    // sequential init server actions
    private static void initServer() {
        displayServerIP();
        inputTCPPort();
        inputUDPPort();
        initTCP();
        initUDP();
    }

    // get port number from user
    private static int inputUserPort(String networkType) {
        System.out.print("Input a port between 5001 and 5050 for " + networkType + " network : ");
        int port;
        do {
            Scanner sc = new Scanner(System.in);
            if (!sc.hasNextInt()) {
                System.out.print("Is not an Integer. Retry for " + networkType + " network: ");
                // if it is a number
            } else {
                port = sc.nextInt();
                // ensures the ports are not duplicate
                if (TCP_PORT != 1 && port == TCP_PORT) {
                    System.out.print("Is a duplicate of TCP port. Retry for " + networkType + " network: ");
                // ensures ports are in bounds
                } else if (port < 5001 || port > 5050) {
                    System.out.print("Is not between 5001 and 5050. Retry for " + networkType + " network: ");
                } else {
                    return port;
                }
            }
        } while (true);
    }

    // functions to get ports for tcp and udp connections

    private static void inputTCPPort() {
        TCP_PORT = inputUserPort("tcp");
    }

    private static void inputUDPPort() {
        UDP_PORT = inputUserPort("udp");
    }

    // displays ip of current machine
    private static void displayServerIP() {
        try {
            System.out.println("Server ip address : " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}