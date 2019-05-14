import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {

    static int UDP_PORT = 0;
    static int TCP_PORT = 1;

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
                    ClientHandler udpClientHandler = new UDPClientHandler(packet, this.udpSocket);
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
        displayServerIP();
        inputTCPPort();
        inputUDPPort();
        initTCP();
        initUDP();
    }

    private static int inputUserPorts(String networkType) {
        boolean success = true;
        System.out.println("Input a port between 5001 and 5050 for " + networkType + " network : ");
        int port;
        do {
            Scanner sc = new Scanner(System.in);
            if (!sc.hasNextInt()) {
                success = false;
                System.out.println("Is not an Integer. Retry.");
            } else {
                port = sc.nextInt();
                if (TCP_PORT != 1 && port == TCP_PORT) {
                    success = false;
                    System.out.println("Is a duplicate of TCP port. Retry.");
                } else if (port < 5001 && port > 5050) {
                    success = false;
                    System.out.println("Is not between 5001 and 5050. Retry.");
                } 
            }
        } while (!success);
    }

    private static int inputTCPPort() {
        TCP_PORT = inputUserPorts("tcp");
    }

    private static int inputUDPPort() {
        UDP_PORT = inputUserPorts("udp");
    }

    private static void displayServerIP() {
        System.out.print(InetAddress.getLocalHost());
    }

}