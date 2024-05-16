import java.io.*;
import java.util.*;
import java.net.*;

public class Client4 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            System.out.println("\nServer connected");

            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintStream output = new PrintStream(socket.getOutputStream());

                        // Send the operation type to the server (asynchronous or deferred synchronous)
                        output.println("asynchronous");

                        // Perform the computation
                        String computation = "Calculate this";
                        output.println(computation);

                        // Receive acknowledgment from the server
                        String acknowledgment = input.readLine();
                        System.out.println(acknowledgment);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
