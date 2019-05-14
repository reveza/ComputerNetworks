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
        
        return (String)this.inputStream.readObject();
    }

    @Override
    protected void writeData(String message) {
        this.outputStream.writeObject(message);
    }

    @Override
    protected void writeData(File file) {
        this.outputStream.writeObject(file);
    }
}