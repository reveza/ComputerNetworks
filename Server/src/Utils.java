import java.io.*;
import java.nio.ByteBuffer;

public class Utils {
    public static byte[] serializeInteger(int integer) {
        return ByteBuffer.allocate(4).putInt(integer).array();
    }

    public static int deserializeInteger(byte[] data) {
        return ByteBuffer.wrap(data).getInt();
    }

    public static <T> byte[] serializeObject(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        return byteArrayOutputStream.toByteArray();
    }

    public static <T> T deserializeObject(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (T) objectInputStream.readObject();
    }
}
