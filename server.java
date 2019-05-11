public class Server {
    public static void main(String[] args) {
        connectToServer();
    }

    public static void connectToServer() {
        //Try to connect the server on an unused port. Successful connection will return a socket
        try(ServerSocket serverSocket = new ServerSocket(5001)) {
            Socket connectionSocket = serverSocket.accept();

            //Create IOStream for the connection
            ObjectInputStream objectInputToServer = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream objectOutputFromServer = new ObjectOutputStream(connectionSocket.getOutputStream());


        }
    }

}