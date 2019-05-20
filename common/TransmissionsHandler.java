import java.io.File;
import java.io.IOException;

public abstract class TransmissionsHandler {
    protected static final int BUFFER_SIZE = 4096;

    public abstract String readMessage() throws IOException;
    public abstract File readFile(String fileName) throws IOException;
    public abstract void sendMessage(String s) throws IOException;
    public abstract void sendFile(File f) throws IOException;
    public abstract  void close();
}
