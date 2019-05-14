import java.net.*;
import java.io.*;

public class TCPClientHandler extends ClientHandler {
    private Socket connectionSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    TCPClientHandler(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        this.inputStream = new ObjectInputStream(connectionSocket.getInputStream());
        this.outputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
    }

    @Override
    public void run(){
        while (!connectionSocket.isClosed()) {
            manageRequest();
        }
    }

    @Override
    protected String readData() {

        try {
            return (String)this.inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        // TODO arranger ca
        return "";
    }

    @Override
    protected void writeData(String message) {
        try {
            this.outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void writeData(File file) {
        try {
            this.outputStream.writeObject(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}