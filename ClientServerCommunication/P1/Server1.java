import java.io.*;
import java.net.*;

public class Server1 {
    public static void main(String[] args) {
        try {
            // Initialize server socket on port 8080
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("\nServer has started successfully");

            // Accept a client connection
            Socket socket = serverSocket.accept();
            System.out.println("Client has connected\n");

            // Set up input and output streams for communication with the client
            try (DataInputStream inputData = new DataInputStream(socket.getInputStream());
                 DataOutputStream outputData = new DataOutputStream(socket.getOutputStream())) {

                // Read the client's chosen operation
                String clientMessage = inputData.readUTF();
                String serverMessage;
                String change;

                // Perform the appropriate operation based on the client's choice
                switch (clientMessage) {
                    case "1": // Upload file to server
                        serverMessage = "\nUpload: Please provide the name of the file you want to upload\n";
                        outputData.writeUTF(serverMessage);

                        change = inputData.readUTF();
                        if (uploadOperation(change, inputData)) {
                            serverMessage = "The file has been uploaded successfully";
                        } else {
                            serverMessage = "Upload failed, please check and try again";
                        }
                        outputData.writeUTF(serverMessage);
                        break;

                    case "2": // Download file from server
                        serverMessage = "\nDownload: Please provide the name of the file you want to download\n";
                        outputData.writeUTF(serverMessage);

                        change = inputData.readUTF();
                        if (downloadOperation(change, outputData)) {
                            serverMessage = "The file has been downloaded successfully";
                        } else {
                            serverMessage = "Download failed, please check and try again";
                        }
                        outputData.writeUTF(serverMessage);
                        break;

                    case "3": // Rename file on server
                        serverMessage = "\nRename: Please provide the current and new name of the file\n";
                        outputData.writeUTF(serverMessage);

                        change = inputData.readUTF();
                        String[] names = change.split(" ");
                        if (renameOperation(names[0], names[1])) {
                            serverMessage = "The file has been renamed successfully";
                        } else {
                            serverMessage = "Rename failed, please check and try again";
                        }
                        outputData.writeUTF(serverMessage);
                        break;

                    case "4": // Delete file from server
                        serverMessage = "\nDelete: Please provide the name of the file you want to delete\n";
                        outputData.writeUTF(serverMessage);

                        change = inputData.readUTF();
                        if (deleteOperation(change)) {
                            serverMessage = "The file has been deleted successfully";
                        } else {
                            serverMessage = "Delete failed, please check and try again";
                        }
                        outputData.writeUTF(serverMessage);
                        break;

                    default:
                        System.out.println("Invalid option selected");
                        break;
                }
            } catch (IOException ex) {
                System.out.println("Error communicating with client");
                ex.printStackTrace();
            } finally {
                // Close the socket and server socket
                socket.close();
                serverSocket.close();
            }
        } catch (IOException ex) {
            System.out.println("Server encountered an error");
            ex.printStackTrace();
        }
    }

    // Handles uploading a file to the server
    private static boolean uploadOperation(String fileName, DataInputStream inputData) {
        try {
            File file = new File("server_files/" + fileName);
            FileOutputStream fileOutput = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputData.read(buffer)) != -1) {
                fileOutput.write(buffer, 0, bytesRead);
            }
            fileOutput.close();
            System.out.println("The file " + fileName + " has been uploaded by the client");
            return true;
        } catch (IOException ex) {
            System.out.println("Error uploading file");
            ex.printStackTrace();
        }
        return false;
    }

    // Handles downloading a file from the server
    private static boolean downloadOperation(String fileName, DataOutputStream outputData) {
        try {
            File file = new File("server_files/" + fileName);
            if (!file.exists()) {
                outputData.writeUTF("File not found");
                return false;
            } else {
                outputData.writeUTF("File found");
            }

            FileInputStream fileInput = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInput.read(buffer)) != -1) {
                outputData.write(buffer, 0, bytesRead);
            }
            fileInput.close();
            System.out.println("The file " + fileName + " has been downloaded by the client");
            return true;
        } catch (IOException ex) {
            System.out.println("Error downloading file");
            ex.printStackTrace();
        }
        return false;
    }

    // Handles renaming a file on the server
    private static boolean renameOperation(String oldName, String newName) {
        File oldFile = new File("server_files/" + oldName);
        File newFile = new File("server_files/" + newName);

        if (oldFile.renameTo(newFile)) {
            System.out.println("The file " + oldName + " has been renamed to " + newName);
            return true;
        } else {
            System.out.println("Error renaming file");
        }
        return false;
    }

    // Handles deleting a file from the server
    private static boolean deleteOperation(String fileName) {
        File file = new File("server_files/" + fileName);

        if (file.delete()) {
            System.out.println("The file " + fileName + " has been deleted from the server");
            return true;
        } else {
            System.out.println("Error deleting file");
        }
        return false;
    }
}
