import java.net.*;
import java.util.concurrent.*;

// Function class to simulate client behavior
class Function extends Thread {
    private int events; // Number of events
    private String server; // Server address
    private int destn; // Destination port
    private int[] space; // Vector clock space
    private int clocks; // Clock index
    private int number; // Number of iterations
    private int p_id; // Process ID

    // Constructor
    public Function(int events, String server, int destn, int[] space, int clocks, int number, int p_id) {
        this.events = events;
        this.server = server;
        this.destn = destn;
        this.space = space;
        this.clocks = clocks;
        this.number = number;
        this.p_id = p_id;
    }

    @Override
    public void run() {
        // Thread to receive messages from other processes
        Thread get = new Thread(() -> gets(server, destn, space, clocks, p_id));
        get.start();

        int xx = 1;
        while (xx < number) {
            space[clocks]++; // Increment local clock
            String text = p_id + "." + xx + "." + space[clocks]; // Construct message
            System.out.println("Current P[" + p_id + "] : Vector Clock:" + space[clocks]); // Print current vector clock
            // Thread to send message to other processes
            Thread put = new Thread(() -> puts(server, destn, text));
            put.start();
            try {
                put.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            xx++;
        }

        String text = p_id + ".End";
        Thread put = new Thread(() -> puts(server, destn, text));
        try {
            get.join(); // Wait for receive thread to finish
            get.interrupt(); // Interrupt receive thread
            put.start(); // Start send thread
            put.join(); // Wait for send thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to receive messages from other processes
    private void gets(String server, int destn, int[] space, int clocks, int pid) {
        try {
            DatagramSocket socket = new DatagramSocket(destn);
            byte[] buffer = new byte[10000];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String text = new String(packet.getData(), 0, packet.getLength());
                if (!text.equals("End")) {
                    space[clocks]++; // Increment clock on message receipt
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to send messages to other processes
    private void puts(String server, int destn, String text) {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = text.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(server), destn);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Main class
public class VectorClock {
    public static void main(String[] args) {
        String server = "localhost"; // Server address
        int destn = 4444; // Destination port
        int events = 3; // Number of events
        int number = 6; // Number of iterations
        int[] space = new int[3]; // Vector clock space
        int p_id = 0; // Process ID

        Function[] lists = new Function[events];
        // Start multiple Function threads
        for (int k = 0; k < events; k++) {
            lists[k] = new Function(events, server, destn, space, k, number, p_id);
            lists[k].start();
        }

        // Wait for all Function threads to finish
        for (Function list : lists) {
            try {
                list.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
