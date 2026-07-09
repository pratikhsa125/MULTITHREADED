package server;
import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // Thread to receive messages
            Thread receive = new Thread(() -> {
                try {
                    DataInputStream serverInput = new DataInputStream(socket.getInputStream());
                    String msg;

                    while (true) {
                        msg = serverInput.readUTF();
                        System.out.println(msg);
                    }

                } catch (IOException e) {
                    System.out.println("Disconnected from server");
                }
            });

            receive.start();

            // Sending messages (FIXED PART)
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String msg = br.readLine();
                dos.writeUTF(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}