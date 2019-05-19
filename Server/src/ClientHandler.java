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

public abstract class ClientHandler extends Thread {

    protected TransmissionsHandler transmissionsHandler;


    protected void manageRequest() throws IOException {
        System.out.println("prep");
        String clientMessage = transmissionsHandler.readMessage();
        System.out.println("client message : " + clientMessage);
        String directory = this instanceof TCPClientHandler ? Utils.TCP_DIRECTORY : Utils.UDP_DIRECTORY;
        displayCommand(clientMessage);
            switch (clientMessage.split(" ")[0]) {
                case Utils.LIST_COMMAND :
                    try {
                        String data = getFilesInDirectory(directory).stream()
                                .reduce("", (a, b) -> a + "[File] " + b + "\n");
                        transmissionsHandler.sendMessage(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case Utils.DOWNLOAD_COMMAND:
                    File fileToSend = getFile(directory, clientMessage.split(" ")[1]);
                    transmissionsHandler.sendFile(fileToSend);
                    break;

            }
    }

    private void displayCommand(String command) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            Integer port = this instanceof TCPClientHandler ? Server.TCP_PORT : Server.UDP_PORT;
            Timestamp time = new Timestamp(System.currentTimeMillis());

            Message msg = new Message(ip, port, time, command);
            msg.display();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

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

    private File getFile(String directory, String fileName) {
        return new File(directory + fileName);
    }
}