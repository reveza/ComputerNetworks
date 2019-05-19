import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.net.InetAddress;
public class Client {//TODO comment code
    //TODO rapport de lab

    private static TransmissionsHandler transmissionsHandler;
    private final static String BACK_COMMAND = "back";
    private final static String EXIT_COMMAND = "exit";

    private static boolean validServerAddress = false;
    private static boolean connected = false;

    private static InetAddress serverAddress;

    public static void main(String[] args) {
        while (true) {
            inputServerAddress();
            do {
                inputConnectionMode();
                if (validServerAddress && connected) {
                    handleMenu();
                }
            } while (validServerAddress);
        }
    }

    private static void inputServerAddress() {
        System.out.print("Input the server address (type exit to quit): ");
        String userInput;
        do {
            Scanner sc = new Scanner(System.in);
            userInput = sc.nextLine();
            tryClose(userInput);
            try {
                serverAddress = InetAddress.getByName(userInput);
                return;
            } catch (UnknownHostException e) {
                System.out.print("Is not a valid ip address. Enter a valid server address (type exit to quit): ");
            }

        } while (true);
    }

    private static int inputUserPorts(String networkType) {
        System.out.print("Input a port between 5001 and 5050 for " + networkType + " network (type exit to quit): ");
        int port;
        do {
            Scanner sc = new Scanner(System.in);
            if (!sc.hasNextInt()) {
                tryClose(sc.nextLine());
                System.out.print("Is not an Integer. Retry for " + networkType + " network (type exit to quit): ");
            } else {
                port = sc.nextInt();
                if (port < 5001 || port > 5050) {
                    System.out.print("Is not between 5001 and 5050. Retry for " + networkType + " network (type exit to quit): ");
                } else {
                    return port;
                }
            }
        } while (true);
    }

    private static void inputConnectionMode() {
        System.out.print("Input a network mode. Type tcp or udp (type exit to quit): ");
        String networkMode;
        do {
            Scanner sc = new Scanner(System.in);

            networkMode = sc.nextLine();
            tryClose(networkMode);
            switch (networkMode.toLowerCase()) {
                case "tcp":
                case "udp":
                    setupConnection(inputUserPorts(networkMode), networkMode.equals("tcp"));
                    return;
                default:
                    System.out.print("Wrong input! Please enter tcp or udp (type exit to quit): ");
            }

        } while (true);
    }

    private static void handleMenu() {
        displayMenu();
        menuListener();
    }

    private static void displayMenu() {
        System.out.println("Choose one of the following commands:\n" +
        "\tls : Display files of directory\n" +
        "\tdownload <filename> : Download a file from the directory\n" +
        "\tback : Go back to choose a different port.\n" +
        "\texit : Exit the app.\n");
    }

    private static void menuListener() {
        String command;
        boolean goBack = false;
        while (!goBack) {
            Scanner sc = new Scanner(System.in);

            System.out.print(">> ");

            command = sc.nextLine();
            tryClose(command);

            try {
                switch (command.split(" ")[0]) {
                    case Utils.LIST_COMMAND:
                        requestDirectoryList();
                        break;
                    case Utils.DOWNLOAD_COMMAND:
                        requestFile(command);
                        break;
                    case BACK_COMMAND:
                        goBack = true;
                        break;
                    default:
                        System.out.println("Enter a valid command!");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Something went wrong :/");
            }
        }
    }

    private static void requestDirectoryList() throws IOException {
        transmissionsHandler.sendMessage(Utils.LIST_COMMAND);
        System.out.println(transmissionsHandler.readMessage());
    }

    private static void requestFile(String command) throws IOException {
        String fileName = command.split(" ")[1];
        transmissionsHandler.sendMessage(command);
        transmissionsHandler.readFile(fileName);
        System.out.println("The file " + fileName + " is received.");
    }

    private static void setupConnection(int port, boolean isTCP) {
        try {
            transmissionsHandler = isTCP ? new TCPTransmissionsHandler(port, serverAddress) : new UDPTransmissionsHandler(port, serverAddress);
            validServerAddress = true;
            connected = true;
        } catch (IOException e) {
            connected = false;
            System.out.println("Connection impossible. Restart the process.");
        }
    }

    private static void tryClose(String userInput) {
        if (userInput.equals(EXIT_COMMAND)) {
            close();
        }
    }

    private static void close() {
        if (transmissionsHandler != null) {
            transmissionsHandler.close();
        }
        System.exit(0);
    }
}