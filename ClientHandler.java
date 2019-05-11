public class ClientHandler extends Thread {
    private Socket connectionSocket;
    ObjectInputStream inputStream;

    ClientHandler(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        inputStream = new ObjectInputStream(connectionSocket.getInputStream());
    }
}