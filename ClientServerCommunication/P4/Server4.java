import java.io.*;
import java.util.*;
import java.net.*;

public class Server4 {
    private static Map<String, Integer> computationResults = new HashMap<>(); // Map to store computation results

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8080);
            System.out.println("\nServer is started successfully");

            while (true) {
                Socket socket = server.accept();
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            PrintStream output = new PrintStream(socket.getOutputStream());

                            // Read the operation type (asynchronous or deferred synchronous)
                            String operationType = input.readLine();

                            // Perform the computation
                            String computation = input.readLine();
                            int result = performComputation(computation);

                            // Save the result in the map
                            computationResults.put(computation, result);

                            // Send acknowledgment to the client
                            output.println("Acknowledgment: Computation received successfully");

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Method to perform the computation (example computation)
    private static int performComputation(String computation) {
        // Example computation: length of the input string
        return computation.length();
    }
}
