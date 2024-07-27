import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    Socket socket;
    BufferedReader br;
    PrintWriter out;
    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection Done.");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void startReading() {
        // Thread to read Data
        Runnable r1 = () -> {
            System.out.println("Reader Started...");
            try {
                while (true) {

                    String message = br.readLine();
                    if(message.equalsIgnoreCase("exit")){
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("server: " + message);

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("Connection Closed");
            }
        };

        new Thread(r1).start();
    }
    private void startWriting() {
        // Thread to take data from user and will send to client
        Runnable r2 = () -> {
            System.out.println("Writer Started...");
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equalsIgnoreCase("exit")){
                        socket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        };

        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("This is Client...");
        System.out.println("Going to start Client...");
        new Client();
    }
}
