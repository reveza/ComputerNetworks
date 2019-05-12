import java.net.Socket;
import java.io.*;

public class TCPClientHandler extends ClientHandler {
    private Socket connectionSocket;
    protected ObjectInputStream inputStream;
    protected ObjectOutputStream outputStream;

    TCPClientHandler(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        inputStream = new ObjectInputStream(connectionSocket.getInputStream());
        outputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
    }

    // code logique de gestion de requetes du client
    public void run(){
        while (true) {
            manageRequest();
        }
    }

    protected String readData() {
        // TODO read from tcp
        return "";
    } 
    protected void writeData(String message) {
        // TODO write file to tcp
    }
    protected void writeData(File file) {
        // TODO write file to tcp
    }
}