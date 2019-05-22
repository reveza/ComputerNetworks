import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.net.InetAddress;
public class Client {

    private static TransmissionsHandler transmissionsHandler;
    private final static String BACK_COMMAND = "back";
    private final static String EXIT_COMMAND = "exit";

    // tells if the server address is valid
    private static boolean validServerAddress = false;

    // tells if the client is connected through any sort of socket
    private static boolean connected = false;

    private static InetAddress serverAddress;

    public static void main(String[] args) {
        // Prompts for server address until any type of connection is established with the server
        while (true) {
            inputServerAddress();
            // prompts only for input connection mode (tcp or udp and port) only after a successful connection
            // to the server happened at least one time
            do {
                inputConnectionMode();
                // if a connection was established start executing the main loop
                if (connected) {
                    handleMenu();
                }
            } while (validServerAddress);
        }
    }

    // asks the user for a valid ip address (tested with InetAddress constructor)
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

    // asks the port to the user until a valid port is entered (between 5001 and 5050 inclusive)
    private static int inputUserPorts(String networkType) {
        System.out.print("Input a port between 5001 and 5050 for " + networkType + " network (type exit to quit): ");
        int port;
        do {
            Scanner sc = new Scanner(System.in);
            // if input was not an integer (hasNextInt is blocking and tells if input is an integer)
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

    // gets the input connection mode from user
    private static void inputConnectionMode() {
        System.out.print("Input a network mode. Type tcp or udp (type exit to quit): ");
        String networkMode;
        do {
            Scanner sc = new Scanner(System.in);

            // ensures the input is correct even if the user has entered the answer in upper case
            networkMode = sc.nextLine().toLowerCase();
            tryClose(networkMode);
            switch (networkMode) {
                case "tcp":
                case "udp":
                    setupConnection(inputUserPorts(networkMode), networkMode.equals("tcp"));
                    return;
                default:
                    System.out.print("Wrong input! Please enter tcp or udp (type exit to quit): ");
            }

        } while (true);
    }

    // displays menu and handles commands entered by the user
    private static void handleMenu() {
        displayMenu();
        menuListener();
    }

    // displays the menu
    private static void displayMenu() {
        System.out.println("Choose one of the following commands:\n" +
        "\tls : Display files of directory\n" +
        "\tdownload <filename> : Download a file from the directory\n" +
        "\tback : Go back to choose a different port.\n" +
        "\texit : Exit the app.\n");
    }

    // handles the commands enterd by the user
    private static void menuListener() {
        String command;
        boolean goBack = false;
        while (!goBack) {
            Scanner sc = new Scanner(System.in);

            System.out.print(">> ");

            command = sc.nextLine();
            tryClose(command);

            try {
                // tests the first part of the command
                switch (command.split(" ", 2)[0]) {
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
        // closes the transmission handler after finished using it
        connected = false;
        transmissionsHandler.close();
    }

    // requests the directory list from server and prints it
    private static void requestDirectoryList() throws IOException {
        transmissionsHandler.sendMessage(Utils.LIST_COMMAND);
        System.out.println(transmissionsHandler.readMessage());
    }

    // requests file from server and writes it in memory
    private static void requestFile(String command) throws IOException {
        String fileName = command.split(" ", 2)[1];
        transmissionsHandler.sendMessage(command);
        transmissionsHandler.readFile(fileName);
        System.out.println("The file " + fileName + " is received.\n");
    }

    // tries to setup a connection with the server through tcp or udp sockets
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

    // tests if exit command was entered and if so, closes the program
    private static void tryClose(String userInput) {
        if (userInput.equals(EXIT_COMMAND)) {
            close();
        }
    }

    // tries to close the transmission handler if needed, then closes the program
    private static void close() {
        if (transmissionsHandler != null) {
            transmissionsHandler.close();
        }
        System.exit(0);
    }
}