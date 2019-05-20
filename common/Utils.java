import java.nio.ByteBuffer;

public class Utils {

    public static final String LIST_COMMAND = "ls";
    public static final String DOWNLOAD_COMMAND = "download";
    public static final String UDP_DIRECTORY = "./udp/";
    public static final String TCP_DIRECTORY = "./tcp/";


    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }
}
