import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

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
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder= new StringBuilder();
        char character;
        while ((character = (char)bufferedReader.read()) != '\0') {
            builder.append(character);
        }
        return builder.toString();
    }

    @Override
    public File readFile(String fileName) throws IOException {
        File file = new File(Utils.TCP_DIRECTORY + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        int length;
        byte[] bytes = new byte[1024];
        while ((length = inputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, length);
        }
        fileOutputStream.close();
        return file;
    }

    @Override
    public void sendMessage(String message) {
        PrintWriter printer = new PrintWriter(outputStream);
        printer.print(message + '\0');
        printer.flush();
    }

    @Override
    public void sendFile(File file) throws IOException {
        Path path = file.toPath();
        Files.copy(path, outputStream);
        outputStream.flush();
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
