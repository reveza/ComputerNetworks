import java.net.*;
import java.io.*;

public class TCPClientHandler extends ClientHandler {
    private Socket connectionSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    TCPClientHandler(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        this.outputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(connectionSocket.getInputStream());

        System.out.println("tcp output");
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
            this.outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void writeData(File file) {
        try {
            this.outputStream.writeObject(file);
            this.outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}