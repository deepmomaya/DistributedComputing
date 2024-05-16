import java.io.*;
import java.net.*;

public class Client1 {
    private static BufferedReader bufferedReader;

    public static void main(String[] args) {
        try (
            // Connect to the server running on localhost at port 8080
            Socket socket = new Socket("localhost", 8080);
            // Set up input and output streams for communication with the server
            DataInputStream inputData = new DataInputStream(socket.getInputStream());
            DataOutputStream outputData = new DataOutputStream(socket.getOutputStream())
        ) {
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            // Display operation options to the user
            System.out.println("\nPlease select which operation you want to perform:\n");
            System.out.println("1. Upload file to server");
            System.out.println("2. Download file from the server");
            System.out.println("3. Rename the file on the server");
            System.out.println("4. Delete file from the server\n");

            // Read the user's choice
            String operation = bufferedReader.readLine();

            // Check if the selected operation is valid
            if (isValidOperation(operation)) {
                // Send the operation choice to the server
                outputData.writeUTF(operation);
                outputData.flush();

                // Perform the selected operation
                switch (operation) {
                    case "1":
                        handleFileUpload(inputData, outputData);
                        break;
                    case "2":
                        handleFileDownload(inputData, outputData);
                        break;
                    case "3":
                        handleFileRename(inputData, outputData);
                        break;
                    case "4":
                        handleFileDelete(inputData, outputData);
                        break;
                    default:
                        System.out.println("\nInvalid operation.");
                        break;
                }
            } else {
                System.out.println("\nPlease select a correct option.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Helper method to validate the user's operation choice
    private static boolean isValidOperation(String operation) {
        return operation.equals("1") || operation.equals("2") || operation.equals("3") || operation.equals("4");
    }

    private static void handleFileUpload(DataInputStream inputData, DataOutputStream outputData) throws IOException {
        System.out.println("Enter the path of the file to upload:");
        String filePath = bufferedReader.readLine();

        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            outputData.writeUTF(file.getName());
            outputData.flush();

            FileInputStream fileInput = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInput.read(buffer)) != -1) {
                outputData.write(buffer, 0, bytesRead);
            }
            outputData.flush();
            fileInput.close();
            System.out.println("File uploaded successfully.");
        } else {
            System.out.println("File not found or is a directory.");
        }
    }

    private static void handleFileDownload(DataInputStream inputData, DataOutputStream outputData) throws IOException {
        System.out.println("Enter the name of the file to download:");
        String fileName = bufferedReader.readLine();
        outputData.writeUTF(fileName);
        outputData.flush();

        String serverResponse = inputData.readUTF();
        if (serverResponse.equals("File not found")) {
            System.out.println("File not found on server.");
        } else {
            FileOutputStream fileOutput = new FileOutputStream(new File("downloaded_" + fileName));
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputData.read(buffer)) != -1) {
                fileOutput.write(buffer, 0, bytesRead);
            }
            fileOutput.close();
            System.out.println("File downloaded successfully.");
        }
    }

    private static void handleFileRename(DataInputStream inputData, DataOutputStream outputData) throws IOException {
        System.out.println("Enter the current name of the file to rename:");
        String currentName = bufferedReader.readLine();
        outputData.writeUTF(currentName);
        outputData.flush();

        System.out.println("Enter the new name for the file:");
        String newName = bufferedReader.readLine();
        outputData.writeUTF(newName);
        outputData.flush();

        String serverResponse = inputData.readUTF();
        System.out.println(serverResponse);
    }

    private static void handleFileDelete(DataInputStream inputData, DataOutputStream outputData) throws IOException {
        System.out.println("Enter the name of the file to delete:");
        String fileName = bufferedReader.readLine();
        outputData.writeUTF(fileName);
        outputData.flush();

        String serverResponse = inputData.readUTF();
        System.out.println(serverResponse);
    }
}
