import java.net.*;
import java.io.*;

public class TCPClientHandler extends ClientHandler {

    TCPClientHandler(Socket connectionSocket) throws IOException {
        transmissionsHandler = new TCPTransmissionsHandler(connectionSocket);
    }

    @Override
    public void run(){
        while (!((TCPTransmissionsHandler)transmissionsHandler).isClosed()) {
            try {
                manageRequest();
            } catch (IOException e) {
                break;
            }
        }
    }
}