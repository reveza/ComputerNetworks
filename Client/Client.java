import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.net.InetAddress;
public class Client {
    
                
    private static int TCP_PORT;
    private static int UDP_PORT;
    
    private static final InetAddress IP;
    private static Socket tcpSocket;
    private static DatagramSocket udpSocket;
    private static boolean isTCP;
    private static ObjectInputStream inputTCP;
    private static ObjectOutputStream outputTCP;

    public static void main(String[] args) {
        inputInformation();
        initConnection();
    }

    private static void inputInformation() {
        inputIPAddress();
        inputConnectionMode();
    }

    private static void inputIPAddress() {
        System.out.print("Input Server IP Address: ");
        String ip;
        do {
            Scanner sc = new Scanner(System.in);
            
            ip = sc.nextLine();
            try {
                IP = InetAddress.getByName(ip);
                System.out.print("You are connected to " + IP.getHostName());
                return;
            } catch (UnknownHostException e) {
                System.out.print("Is not a valid ip address. Enter Server IP Address: ");
            }
                
        } while (true);
    }

    private static int inputUserPorts(String networkType) {
        System.out.print("Input a port between 5001 and 5050 for " + networkType + " network : ");
        int port;
        do {
            Scanner sc = new Scanner(System.in);
            if (!sc.hasNextInt()) {
                System.out.print("Is not an Integer. Retry for " + networkType + " network: ");
            } else {
                port = sc.nextInt();
                if (TCP_PORT != 1 && port == TCP_PORT) {
                    System.out.print("Is a duplicate of TCP port. Retry for " + networkType + " network: ");
                } else if (port < 5001 || port > 5050) {
                    System.out.print("Is not between 5001 and 5050. Retry for " + networkType + " network: ");
                } else {
                    return port;
                }
            }
        } while (true);
    }

    private static void inputTCPPort() {
        TCP_PORT = inputUserPorts("tcp");
    }

    private static void inputUDPPort() {
        UDP_PORT = inputUserPorts("udp");
    }

    private static void inputConnectionMode() {
        boolean success = true;
        System.out.print("Input a network mode. Type tcp or udp in small caps(type exit to quit): ");
        String networkMode;
        do {
            Scanner sc = new Scanner(System.in);
            
            networkMode = sc.nextLine();
            if ( networkMode.equals("tcp") ) {
                isTCP = true;
                inputTCPPort();
                return; 
            } else if (networkMode.equals("udp")) {
                isTCP = false;
                inputUDPPort();
                return;
            } else if (networkMode.equals("exit")) {
                System.exit(0);
            } else {
                System.out.println("Wrong input! Please enter tcp or udp(type exit to quit) : ");
            }
                
        } while (true);
    }

    private static void initConnection() {
        if (isTCP) {
            try {
                tcpSocket = new Socket(IP, TCP_PORT);
                inputTCP = new ObjectInputStream(tcpSocket.getInputStream());
                outputTCP = new ObjectOutputStream(tcpSocket.getOutputStream());
                handleMenu();
            } catch (IOException e) {
                System.out.println("Connection impossible. Restart the process.");
                inputInformation();
            }
        } else {
            try {
                DatagramSocket udpSocket = new DatagramSocket();
            } catch (SocketException e) {
                System.out.println("Connection impossible. Restart the process.");
                inputInformation();
            }
        }
    }

    private static void handleMenu() {
        displayMenu();
        menuListener();
    }

    private static void displayMenu() {
        System.out.println("Choose one of the following commands:\n" +
        "ls : Display files of directory\n" +
        "download <filename> : Download a file from the directory\n" +
        "back: Go back to choose a different port or to exit the app.\n");
    }

    private static void menuListener() {
        String command;
        do {
            Scanner sc = new Scanner(System.in);
            
            command = sc.nextLine();
            try {
                if (command.equals("ls")) {
                    sendMessage(command);
                    receiveFileDirectory();
                } else if (command.toString().split(" ")[0].equals("download")) {
                    sendMessage(command);
                    receiveDownloadedFile();
                } else if (command.equals("exit")) {
                    closeSocket();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } while(true);
    }

    private static void closeSocket() {
        if (isTCP) {
            tcpSocket.close();
        } else {
            udpSocket.close();
        }
    }

    private static void sendMessage(String message) {
        if (isTCP) {
            outputTCP.writeObject(message);
        } else {
            final byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, IP, UDP_PORT);
            udpSocket.send(packet);
        }
    }

    private static void receiveFileDirectory() {
        String directory;
        if (isTCP) {
            directory = (String)inputTCP.readObject();
        } else {
            final byte[] buf;
            DatagramPacket packet = new DatagramPacket(buf, buf.length, IP, UDP_PORT);
            udpSocket.receive(packet);
            buf = packet.getData();
            directory = new String(buf);
        }
        System.out.println(directory);
    }

    private static void receiveDownloadedFile() {
        
        File file;
        if (isTCP) {
            file = (File)inputTCP.readObject();
        } else {
            final byte[] buf;
            DatagramPacket packet = new DatagramPacket(buf, buf.length, IP, UDP_PORT);
            udpSocket.receive(packet);
            buf = packet.getData();
            ByteArrayInputStream bis = new ByteArrayInputStream(buf);
            ObjectInputStream ois = new ObjectInputStream(bis);
            file = (File)ois.readObject();
        }
        System.out.println("The file " + file.getName() + " has been downloaded.");
        file.mkdir();
    }
}