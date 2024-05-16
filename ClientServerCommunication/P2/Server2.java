import java.io.*;
import java.util.*;
import java.net.*;

public class Server2 extends Thread {

    public static void main(String[] args) {
        try {
            // Initialize server socket on port 8080
            ServerSocket server = new ServerSocket(8080);
            System.out.println("\nServer is started successfully");

            while (true) {
                // Accept incoming client connections
                Socket socket = server.accept();
                DataInputStream inputdata = new DataInputStream(socket.getInputStream());
                DataOutputStream outputdata = new DataOutputStream(socket.getOutputStream());

                // Create a new thread for each client
                Server2 thread = new Server2(socket, inputdata, outputdata);
                thread.start(); // Start the thread
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Socket and stream variables
    private Socket clients;
    private DataInputStream inputdata;
    private DataOutputStream outputdata;

    // Constructor to initialize socket and streams
    public Server2(Socket clients, DataInputStream inputdata, DataOutputStream outputdata) {
        this.clients = clients;
        this.inputdata = inputdata;
        this.outputdata = outputdata;
    }

    // Method to handle client requests
    public void run() {
        try {
            System.out.println("Now the client thread is connected\n");
            String cm = inputdata.readUTF(); // Read client's selected operation

            // Process the client's selected operation
            switch (cm) {
                case "1":
                    handleUploadOperation();
                    break;
                case "2":
                    handleDownloadOperation();
                    break;
                case "3":
                    handleRenameOperation();
                    break;
                case "4":
                    handleDeleteOperation();
                    break;
                default:
                    System.out.println("Please select correct option");
            }
        } catch (Exception ex) {
            System.out.println("Wrong option selected by client so disconnected");
        }
        System.exit(0); // Exit the thread
    }

    // Method to handle file upload operation
    private void handleUploadOperation() {
        try {
            String change = inputdata.readUTF(); // Read the file name from the client
            String newfile = change + ".txt";
            InputStream input = new FileInputStream("C:\\Users\\Admin\\Desktop\\P2\\client2\\" + newfile);
            OutputStream output = new FileOutputStream("C:\\Users\\Admin\\Desktop\\P2\\server2\\" + change + "-ClientUpload.txt");

            byte[] buffer = new byte[1000];
            int n;
            while ((n = input.read(buffer)) > 0) { // Read data from input stream and write to output stream
                output.write(buffer, 0, n);
            }

            System.out.println("The file " + newfile + " has been uploaded by the client");
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Incorrectly done so disconnected");
        }
    }

    // Method to handle file download operation
    private void handleDownloadOperation() {
        try {
            String change = inputdata.readUTF(); // Read the file name from the client
            String newfile = change + ".txt";
            InputStream input = new FileInputStream("C:\\Users\\Admin\\Desktop\\P2\\server2\\" + newfile);
            OutputStream output = new FileOutputStream("C:\\Users\\Admin\\Desktop\\P2\\client2\\" + change + "-ServerUpload.txt");

            byte[] buffer = new byte[1000];
            int n;
            while ((n = input.read(buffer)) > 0) { // Read data from input stream and write to output stream
                output.write(buffer, 0, n);
            }

            System.out.println("The file " + newfile + " has been downloaded from the client");
        } catch (Exception ex) {
            System.out.println("Incorrectly done so disconnected");
        }
    }

    // Method to handle file rename operation
    private void handleRenameOperation() {
        try {
            String change = inputdata.readUTF(); // Read the file name and new name from the client
            String[] s = change.split(" ");
            String renameit = s[0] + ".txt";
            String newrenameit = s[1] + ".txt";

            File o = new File("C:\\Users\\Admin\\Desktop\\P2\\server2\\" + renameit);
            File n = new File("C:\\Users\\Admin\\Desktop\\P2\\server2\\" + newrenameit);
            if (o.renameTo(n)) {
                System.out.println("The file " + renameit + " has been renamed to " + newrenameit);
            } else {
                System.out.println("Incorrectly done so disconnected");
            }
        } catch (Exception ex) {
            System.out.println("Incorrectly done so disconnected");
        }
    }

    // Method to handle file delete operation
    private void handleDeleteOperation() {
        try {
            String change = inputdata.readUTF(); // Read the file name from the client
            String deleteit = change + ".txt";
            File newfile
            = new File("C:\\Users\\Admin\\Desktop\\P2\\server2\\" + deleteit);
            boolean delete = newfile.delete();
            if (delete) {
                System.out.println("The file " + deleteit + " has been deleted from the server");
            } else {
                System.out.println("Incorrectly done so disconnected");
            }
        } catch (Exception ex) {
            System.out.println("Incorrectly done so disconnected");
        }
    }
}
