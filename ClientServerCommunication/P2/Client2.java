import java.io.*;
import java.util.*;
import java.net.*;

public class Client2 {
    private static BufferedReader bufferedReader;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket("localhost", 8080); // Connect to the server on localhost at port 8080
             DataInputStream inputData = new DataInputStream(socket.getInputStream()); // For reading data from server
             DataOutputStream outputData = new DataOutputStream(socket.getOutputStream())) { // For sending data to server

            bufferedReader = new BufferedReader(new InputStreamReader(System.in)); // For reading user input from the console

            // Display options to the user
            System.out.println("\nPlease select which operation you want to perform:\n");
            System.out.println("1. Upload file to server");
            System.out.println("2. Download file from the server");
            System.out.println("3. Rename the file on the server");
            System.out.println("4. Delete file from the server\n");

            String operation = bufferedReader.readLine(); // Read user-selected operation
            String serverMessage, clientMessage;

            if (isValidOperation(operation)) { // Check if the selected operation is valid
                outputData.writeUTF(operation); // Send the selected operation to the server
                outputData.flush();

                serverMessage = inputData.readUTF(); // Read server's response message
                System.out.println(serverMessage);

                clientMessage = bufferedReader.readLine(); // Read user's input for the operation
                outputData.writeUTF(clientMessage); // Send user's input to the server
                outputData.flush();

                serverMessage = inputData.readUTF(); // Read the final response from the server
                System.out.println("\n" + serverMessage); // Display the server's response to the user
            } else {
                System.out.println("\nPlease select a correct option"); // Inform user of invalid selection
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Print stack trace in case of an exception
        }
    }

    // Method to validate if the user-selected operation is one of the allowed options
    private static boolean isValidOperation(String operation) {
        return operation.equals("1") || operation.equals("2") || operation.equals("3") || operation.equals("4");
    }
}
