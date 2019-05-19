import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPTransmissionsHandler extends TransmissionsHandler {

    private OutputStream outputStream;
    private InputStream inputStream;
    private Socket socket;

    public TCPTransmissionsHandler(int serverPort, InetAddress serverAddress) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        initStreams(this.socket);
    }

    public TCPTransmissionsHandler(Socket socket) throws IOException {
        this.socket = socket;
        initStreams(socket);
    }

    @Override
    public String readMessage() throws IOException {
        return new BufferedReader(new InputStreamReader(inputStream)).readLine();
    }

    @Override
    public File readFile(String path) throws IOException {
        File file = new File(path);
        outputStream = new FileOutputStream(file);

        int length = 0;
        byte[] bytes = new byte[1024];
        while ((length = is.read(bytes)) != -1) {
            outputStream.write(bytes, 0, length);
        }
        return file;
    }

    @Override
    public void sendMessage(String message) {
        this.outputStream.writeObject(object);
    }

    @Override
    public void sendFile(File file) {
        this.outputStream.writeObject(object);
    }

    @Override
    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initStreams(Socket socket) throws IOException {
        this.outputStream = socket.getOutputStream();
        this.inputStream = socket.getInputStream();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}
