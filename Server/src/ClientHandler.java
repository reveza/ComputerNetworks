import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

// abstract thread that handles client requests
public abstract class ClientHandler extends Thread {

    protected TransmissionsHandler transmissionsHandler;

    // main logical loop to handle client requests
    protected void manageRequest() throws IOException {
        String clientMessage = readMessage();
        String directory = this instanceof TCPClientHandler ? Utils.TCP_DIRECTORY : Utils.UDP_DIRECTORY;
        displayCommand(clientMessage);
        // if first part of message is...
            switch (clientMessage.split(" ", 2)[0]) {
                case Utils.LIST_COMMAND :
                    try {
                        // reduces every element of the directory in a single string with the right format
                        String data = getFilesInDirectory(directory).stream()
                                .reduce("", (a, b) -> a + "[File] " + b + "\n");
                        sendMessage(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case Utils.DOWNLOAD_COMMAND:
                    File fileToSend = getFile(directory, clientMessage.split(" ", 2)[1]);
                    sendFile(fileToSend);
                    break;

            }
    }

    // displays the client instruction in the right format
    private void displayCommand(String command) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            Integer port = this instanceof TCPClientHandler ? Server.TCP_PORT : Server.UDP_PORT;
            Timestamp time = new Timestamp(System.currentTimeMillis());

            System.out.println("[" + ip.getHostAddress() + ":" + port + " - " + time + "]: " + command);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    // returns a list of the files names in the directory as a list of strings
    private List<String> getFilesInDirectory(String path) throws IOException {
        List<String> result;
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {

            result = walk.filter(Files::isRegularFile)
                    .map(x -> x.getFileName().toString()).collect(Collectors.toList());

        } catch (IOException e) {
            throw e;
        }
        return result;
    }

    // gets a File object of the specified file
    private File getFile(String directory, String fileName) {
        return new File(directory + fileName);
    }

    // abstract functions used to transmit messages through sockets

    // reads a string
    protected abstract String readMessage() throws IOException;
    // transmits string
    protected abstract void sendMessage(String message) throws IOException;
    // transmits file
    protected abstract void sendFile(File file) throws IOException;
}