import java.sql.Timestamp;
import java.net.InetAddress;

public class Message {
    public Message(InetAddress ip, Integer port, Timestamp time, String command) {
        System.out.println("[" + ip + ":" + port + " - " + time + "]: " + command);
    }
}