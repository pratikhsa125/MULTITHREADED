package server;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    static Vector<ClientHandler> clients = new Vector<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server started...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                ClientHandler client = new ClientHandler(socket, dis, dos);
                clients.add(client);

                Thread t = new Thread(client);
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {

    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;

    public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos) {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
    }

    public void run() {
        String msg;

        try {
            while (true) {
                msg = dis.readUTF();

                // Broadcast message to all clients
                for (ClientHandler client : Server.clients) {
                    client.dos.writeUTF(msg);
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }
    }
}