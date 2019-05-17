import java.io.IOException;

public abstract class TransmissionsHandler {
    public abstract <T> T readData() throws IOException, ClassNotFoundException;
    public abstract <T> void writeData(T object) throws IOException;
}
