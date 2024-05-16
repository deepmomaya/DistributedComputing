import java.io.*;
import java.net.*;
import java.util.concurrent.*;

// Manager class to handle incoming connections and manage locks
class Manager extends Thread {
    private final int processes;
    private final int port;

    public Manager(int port, int processes) {
        this.port = port;
        this.processes = processes;
    }

    public void run() {
        try {
            // Setup server socket
            ServerSocket serverSocket = new ServerSocket(port);
            ConcurrentHashMap<InetAddress, Socket> people = new ConcurrentHashMap<>();

            // Accept incoming connections
            for (int i = 0; i < processes; i++) {
                Socket counter = serverSocket.accept();
                people.put(counter.getInetAddress(), counter);
            }

            boolean lockAcquired = false;
            // Main loop to manage locks and communication with clients
            while (!people.isEmpty()) {
                for (InetAddress addr : people.keySet()) {
                    Socket counter = people.get(addr);
                    try {
                        // Read messages from clients
                        BufferedReader in = new BufferedReader(new InputStreamReader(counter.getInputStream()));
                        PrintWriter out = new PrintWriter(counter.getOutputStream(), true);
                        String line;
                        while ((line = in.readLine()) != null) {
                            if (line.equals("acquirethelock")) {
                                if (!lockAcquired) {
                                    lockAcquired = true;
                                    out.println("thelockisacquired");
                                } else {
                                    out.println("full");
                                }
                            } else if (line.equals("releasethelock")) {
                                lockAcquired = false;
                            } else if (line.equals("Done")) {
                                out.println("Done");
                                people.remove(addr);
                                counter.close();
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Function class to simulate client behavior
class Function extends Thread {
    private final int port;
    private final int files;
    private final String locks;

    public Function(int port, int files, String locks) {
        this.port = port;
        this.files = files;
        this.locks = locks;
    }

    public void run() {
        try {
            // Connect to server
            Socket socket = new Socket("localhost", port);
            int base = 0;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (base < files) {
                try {
                    // Attempt to acquire lock
                    out.println("acquirethelock");
                    String response = in.readLine();
                    while (response.equals("full")) {
                        out.println("acquirethelock");
                        response = in.readLine();
                    }
                    System.out.println("P[" + Thread.currentThread().getId() + "] : Acquired the lock");

                    // Update counter in file
                    File file = new File(locks);
                    FileWriter writer = new FileWriter(file, true);
                    writer.write("1");
                    writer.close();
                    System.out.println("P[" + Thread.currentThread().getId() + "] : Updated the counter");

                    // Release lock
                    out.println("releasethelock");
                    System.out.println("P[" + Thread.currentThread().getId() + "] : Released the lock");
                    base++;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            // Notify server of completion
            out.println("Done");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Locking {
    public static void main(String[] args) {
        // Configuration parameters
        int port = 4444;
        int processes = 6;
        int files = 2;
        String locks = "C:/Users/Admin/Desktop/P3/lock.txt";
        
        // Start server manager thread
        Manager central = new Manager(port, processes);
        central.start();

        // Start client threads
        ExecutorService executor = Executors.newFixedThreadPool(processes);
        for (int i = 0; i < processes; i++) {
            Function client = new Function(port, files, locks);
            executor.execute(client);
        }
        executor.shutdown();
    }
}
