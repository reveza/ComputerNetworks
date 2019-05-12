import java.net.Socket;
import java.io.*;

public class TCPClientHandler extends ClientHandler {
    private Socket connectionSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    TCPClientHandler(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        inputStream = new ObjectInputStream(connectionSocket.getInputStream());
        outputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
    }

    // code logique de gestion de requetes du client
    @Override
    public void run(){
        while (!connectionSocket.isClosed()) {
            manageRequest();
        }
    }

    @Override
    protected String readData() {
        // TODO read from tcp
        return "";
    }

    @Override
    protected void writeData(String message) {
        // TODO write file to tcp
    }

    @Override
    protected void writeData(File file) {
        // TODO write file to tcp
    }
}