import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.net.InetAddress;
public class Client {
    
                
    private static int TCP_PORT;
    private static int UDP_PORT;
    
    private static InetAddress IP;
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
                System.out.println("You are connected to " + IP.getHostAddress());
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
                outputTCP = new ObjectOutputStream(tcpSocket.getOutputStream());
                inputTCP = new ObjectInputStream(tcpSocket.getInputStream());

                handleMenu();
            } catch (IOException e) {
                System.out.println("catch");
                System.out.println("Connection impossible. Restart the process.");
                inputInformation();
            }
        } else {

            try {
                udpSocket = new DatagramSocket();
                handleMenu();
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
                } else if (command.split(" ")[0].equals("download")) {
                    sendMessage(command);
                    receiveDownloadedFile();
                } else if (command.equals("exit")) {
                    closeSocket();
                    break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        } while(true);
    }

    private static void closeSocket() {
        if (isTCP) {
            try {
                tcpSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            udpSocket.close();
        }
    }

    private static void sendMessage(String message) {
        if (isTCP) {
            try {
                outputTCP.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            final byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, IP, UDP_PORT);
            try {
                udpSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void receiveFileDirectory() throws IOException, ClassNotFoundException {
        String directory;
        if (isTCP) {
            directory = (String)inputTCP.readObject();
        } else {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            udpSocket.receive(packet);
            buf = packet.getData();
            directory = new String(buf);
        }
        System.out.println(directory);
    }

    private static void receiveDownloadedFile() throws IOException, ClassNotFoundException {
        
        File file;
        if (isTCP) {
            file = (File)inputTCP.readObject();
        } else {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
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