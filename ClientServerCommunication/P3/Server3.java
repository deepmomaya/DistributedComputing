import java.io.*;
import java.util.*;
import java.net.*;

public class Server3 implements Runnable {
    private Socket client;

    // Constructor to initialize the client socket
    public Server3(Socket socket) {
        this.client = socket;
    }

    public static void main(String[] args) {
        try {
            // Create a server socket listening on port 8080
            ServerSocket server = new ServerSocket(8080);
            System.out.println("\nServer is started successfully");

            // Continuously accept client connections
            while (true) {
                // Accept a client connection
                Socket socket = server.accept();
                // Create a new Server3 object for the connected client
                Server3 thread = new Server3(socket);
                // Start a new thread to handle the client's requests
                new Thread(thread).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // The run method is executed in a separate thread for each client
    public void run() {
        System.out.println("\nClient is now connected");
        try {
            // Set up input and output streams for communication with the client
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintStream output = new PrintStream(client.getOutputStream());

            String op;  // Variable to store the client's requested operation

            // Continuously read and process client requests
            while (client != null) {
                op = input.readLine();

                // Process the request based on the operation code received from the client
                switch (op) {
                    case "1":  // Calculate and return the value of pi
                        double pi = calculatePi();
                        output.println(Double.toString(pi));
                        break;

                    case "2":  // Add two numbers
                        int a1 = Integer.parseInt(input.readLine());
                        int a2 = Integer.parseInt(input.readLine());
                        int sum = a1 + a2;
                        output.println(Integer.toString(sum));
                        break;

                    case "3":  // Sort an array
                        int arraySize = Integer.parseInt(input.readLine());
                        int[] array = new int[arraySize];
                        for (int i = 0; i < arraySize; i++) {
                            array[i] = Integer.parseInt(input.readLine());
                        }
                        Arrays.sort(array);
                        for (int i = 0; i < arraySize; i++) {
                            output.println(Integer.toString(array[i]));
                        }
                        break;

                    case "4":  // Multiply matrices
                        int matrixSize = Integer.parseInt(input.readLine());
                        int[][] matrix1 = readMatrix(input, matrixSize);
                        int[][] matrix2 = readMatrix(input, matrixSize);
                        int[][] matrix3 = readMatrix(input, matrixSize);
                        int[][] result = multiplyMatrices(matrix1, matrix2, matrix3, matrixSize);
                        for (int[] row : result) {
                            for (int value : row) {
                                output.println(value);
                            }
                        }
                        break;

                    default:
                        System.out.println("Please select a correct option");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to calculate the value of pi using a series expansion
    private double calculatePi() {
        double pi = 0;
        double sign = 1;
        for (int i = 1; i < 10000; i += 2) {
            pi += sign / i;
            sign = -sign;
        }
        return pi * 4;
    }

    // Method to read a matrix from the input stream
    private int[][] readMatrix(BufferedReader input, int size) throws IOException {
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = Integer.parseInt(input.readLine());
            }
        }
        return matrix;
    }

    // Method to multiply three matrices
    private int[][] multiplyMatrices(int[][] matrix1, int[][] matrix2, int[][] matrix3, int size) {
        int[][] temp = new int[size][size];
        int[][] result = new int[size][size];

        // Multiply matrix1 and matrix2
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    temp[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        // Multiply the result with matrix3
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j] += temp[i][k] * matrix3[k][j];
                }
            }
        }

        return result;
    }
}
