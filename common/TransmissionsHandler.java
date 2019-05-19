import java.io.File;
import java.io.IOException;

public abstract class TransmissionsHandler {
    public abstract String readMessage() throws IOException;
    public abstract File readFile(String path) throws IOException;
    public abstract void sendMessage(String s);
    public abstract void sendFile(File f) throws IOException;
    public abstract  void close();
}
