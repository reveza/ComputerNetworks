import java.sql.Timestamp;
import java.net.InetAddress;

class Message {
    private String message;
    Message(InetAddress ip, Integer port, Timestamp time, String command) {
        message = "[" + ip.getHostAddress() + ":" + port + " - " + time + "]: " + command;
    }

    void display() {
        System.out.println(message);
    }
}