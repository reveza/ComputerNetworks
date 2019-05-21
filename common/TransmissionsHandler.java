import java.io.File;
import java.io.IOException;

// Abstract version of a transmission handler for udp and tcp sockets
public abstract class TransmissionsHandler {
    public static final int BUFFER_SIZE = 4096;

    // reads string from socket
    public abstract String readMessage() throws IOException;
    // reads file data from memory and writes it in file that has the file name fileName
    public abstract File readFile(String fileName) throws IOException;
    // sends string to socket
    public abstract void sendMessage(String s) throws IOException;
    // sends file to socket
    public abstract void sendFile(File f) throws IOException;
    // closes the socket used for communication
    public abstract  void close();
}
