import java.net.Socket;
import java.io.*;

public abstract class ClientHandler extends Thread {

    private final String LIST_REQUEST = "ls";
    private final String DOWNLOAD_REQUEST = "";

    public void manageRequest() {
        String clientMessage = readData();
        displayCommand(clientMessage);
            switch (clientMessage) {
                case LIST_REQUEST :
                    writeData("");
                    break;
                case DOWNLOAD_REQUEST:
                    writeData("");
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

    protected abstract String readData();
    protected abstract void writeData(String message);
    protected abstract void writeData(File file);
}