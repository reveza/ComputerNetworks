import java.net.DatagramSocket;

public class UDPTransmissionsHandler extends TransmissionsHandler {

    private DatagramSocket socket;

    public UDPTransmissionsHandler (DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public <T> T readData() {
        return null;
    }

    @Override
    public <T> void writeData(T object) {

    }
}
