import java.io.*;
import java.util.*;
import java.net.*;

public class Client3 {
    public static void main(String[] args) {
        try {
            // Initialize the scanner for user input
            Scanner sc = new Scanner(System.in);

            // Establish a connection to the server running on localhost at port 8080
            Socket socket = new Socket("localhost", 8080);

            // Set up input and output streams for communication with the server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream output = new PrintStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String op = "";  // Variable to store the operation choice
            int i1 = 0, i2 = 0;  // Variables to store numbers and array/matrix dimensions

            // Continuously prompt the user for operations until the socket is closed
            while (socket != null) {
                // Display the menu to the user
                System.out.println("\nPlease select which operation you want to perform:\n");
                System.out.println("1. Know the value of pi");
                System.out.println("2. Add any two numbers");
                System.out.println("3. Sort an array");
                System.out.println("4. Multiply matrices\n");

                // Read the user's choice
                op = br.readLine();

                // Handle each operation based on the user's choice
                switch (op) {
                    case "1":  // Know the value of pi
                        System.out.println("\nOh! You want to know the value of pi\n");
                        output.println(op);  // Send operation to server
                        System.out.println("" + input.readLine());  // Receive and print the result
                        break;

                    case "2":  // Add any two numbers
                        System.out.println("\nPlease give the two numbers to add\n");
                        i1 = sc.nextInt();
                        i2 = sc.nextInt();
                        output.println(op);  // Send operation to server
                        output.println(Integer.toString(i1));  // Send first number
                        output.println(Integer.toString(i2));  // Send second number
                        System.out.println("The sum is " + input.readLine());  // Receive and print the result
                        break;

                    case "3":  // Sort an array
                        System.out.println("\nPlease give the number of elements in the array\n");
                        output.println(op);  // Send operation to server
                        i1 = sc.nextInt();
                        output.println(Integer.toString(i1));  // Send number of elements
                        System.out.println("\nEnter the elements to sort\n");
                        for (int i = 0; i < i1; i++) {
                            i2 = sc.nextInt();
                            output.println(Integer.toString(i2));  // Send each element
                        }
                        System.out.println("\nThe sorted array is");
                        for (int i = 0; i < i1; i++) {
                            System.out.println("\n" + input.readLine());  // Receive and print each sorted element
                        }
                        break;

                    case "4":  // Multiply matrices
                        output.println(op);  // Send operation to server
                        System.out.println("\nPlease give the number of rows for the matrices\n");
                        i1 = sc.nextInt();
                        output.println(Integer.toString(i1));  // Send number of rows
                        // Input elements for matrix 1
                        System.out.println("\nEnter the elements for matrix 1\n");
                        for (int i = 0; i < i1; i++) {
                            for (int j = 0; j < i1; j++) {
                                i2 = sc.nextInt();
                                output.println(Integer.toString(i2));  // Send each element of matrix 1
                            }
                        }
                        // Input elements for matrix 2
                        System.out.println("\nEnter the elements for matrix 2\n");
                        for (int i = 0; i < i1; i++) {
                            for (int j = 0; j < i1; j++) {
                                i2 = sc.nextInt();
                                output.println(Integer.toString(i2));  // Send each element of matrix 2
                            }
                        }
                        // Input elements for matrix 3
                        System.out.println("\nEnter the elements for matrix 3\n");
                        for (int i = 0; i < i1; i++) {
                            for (int j = 0; j < i1; j++) {
                                i2 = sc.nextInt();
                                output.println(Integer.toString(i2));  // Send each element of matrix 3
                            }
                        }
                        // Receive and print the result of matrix multiplication
                        System.out.println("\nThe multiplication result is ");
                        for (int i = 0; i < i1; i++) {
                            for (int j = 0; j < i1; j++) {
                                System.out.print(input.readLine() + " ");
                            }
                            System.out.println();
                        }
                        break;

                    default:  // Handle invalid operation choice
                        System.out.println("\nPlease select a correct option");
                        break;
                }
                // Exit the program after processing the operation
                System.exit(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
