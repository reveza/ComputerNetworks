import java.net.*;
import java.io.*;

public class TCPClientHandler extends ClientHandler {

    TCPClientHandler(Socket connectionSocket) throws IOException {
        transmissionsHandler = new TCPTransmissionsHandler(connectionSocket);
    }

    // for TCP the thread runs and handles every client requests until the client disconnects or a problem happens
    @Override
    public void run(){
        while (!((TCPTransmissionsHandler)transmissionsHandler).isClosed()) {
            try {
                manageRequest();
            } catch (IOException e) {
                e.printStackTrace();
                transmissionsHandler.close();
                break;
            }
        }
    }

    // every read or send functions "blank" to transmissionHandler TODO find word

    @Override
    protected String readMessage() throws IOException {
        return transmissionsHandler.readMessage();
    }

    @Override
    protected void sendMessage(String message) throws IOException {
        transmissionsHandler.sendMessage(message);
    }

    @Override
    protected void sendFile(File file) throws IOException {
        transmissionsHandler.sendFile(file);
    }
}