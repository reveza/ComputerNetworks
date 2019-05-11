import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;
import java.sql.Timestamp;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {
    public static void main(String[] args) {
        System.out.println("TEST");
        connectToServer();
    }

    public static void connectToServer() {
        //Try to connect the server on an unused port. Successful connection will return a socket
        try(ServerSocket serverSocket = new ServerSocket(5001)) {
            Socket connectionSocket = serverSocket.accept();

            ClientHandler clientHandler = new ClientHandler(connectionSocket);
            //Create IOStream for the connection
            ObjectOutputStream objectOutputFromServer = new ObjectOutputStream(connectionSocket.getOutputStream());

            InetAddress ip = InetAddress.getLocalHost();
            Integer port = 5002;
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String command = "ls";

            Message msg = new Message(ip, port, time, command);


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}