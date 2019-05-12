import java.net.Socket;
import java.io.*;

public abstract class ClientHandler extends Thread {

    private final String LIST_REQUEST = "ls";
    private final String DOWNLOAD_REQUEST = "";

    public void manageRequest() {
        // TODO complete display of requests, with timestamp
        String clientMessage = readData();
            switch (clientMessage) {
                case LIST_REQUEST :
                    writeData("");
                    break;
                case DOWNLOAD_REQUEST:
                    writeData("");
                    break;
            }
    }

    protected abstract String readData();
    protected abstract void writeData(String message);
    protected abstract void writeData(File file);
}