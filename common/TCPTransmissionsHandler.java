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

    // reads a string from inputStream of socket, ends with end character '\0'
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

    // reads a file from socket and writes it in tcp directory with name fileName
    @Override
    public File readFile(String fileName) throws IOException {
        File file = new File(Utils.TCP_DIRECTORY + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        int length;
        byte[] buffer = new byte[8];

        // first, reads file the file length sent by the socket and stores it in fileSize
        inputStream.read(buffer);
        long fileSize = Utils.bytesToLong(buffer);
        buffer = new byte[BUFFER_SIZE];
        // reads as long as the length has not been reached or as long as the stream is not closed by the server
        while (fileSize > 0 && (length = inputStream.read(buffer)) != -1) {
            fileSize -= length;
            fileOutputStream.write(buffer, 0, length);
        }
        fileOutputStream.close();
        return file;
    }

    // Sends a string ending with ending character '\0'
    @Override
    public void sendMessage(String message) {
        PrintWriter printer = new PrintWriter(outputStream);
        printer.print(message + '\0');
        printer.flush();
    }

    // Sends a file to the outputStream
    @Override
    public void sendFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);

        // first, sends the file total length in bytes
        byte[] buffer = Utils.longToBytes(file.length());
        outputStream.write(buffer);

        buffer = new byte[BUFFER_SIZE];
        int length;
        // sends the total file through chunks with size of BUFFER_SIZE bytes
        while ((length = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        fileInputStream.close();
    }

    // closes the socket
    @Override
    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // gets the input and output streams from the socket
    private void initStreams(Socket socket) throws IOException {
        this.outputStream = socket.getOutputStream();
        this.inputStream = socket.getInputStream();
    }

    // tells if the socket is closed
    public boolean isClosed() {
        return socket.isClosed();
    }
}
