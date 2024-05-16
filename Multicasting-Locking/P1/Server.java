import java.io.*;
import java.util.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws Exception {
        int nodes = 3;
        Scanner sc = new Scanner(System.in);
        Lamport sepro = new Lamport();
        sepro.nodes(nodes);
        
        // Create and start threads for different operations
        Thread Goo = new Thread() {
            public void run() {
                try {
                    sepro.Come(); // Execute the Come method
                } catch (Exception ex) {
                    ex.printStackTrace(); // Print any exceptions
                }			
            }
        };
        
        Thread Comee = new Thread() {
            public void run() {
                try {
                    sepro.Go(); // Execute the Go method
                } catch (Exception ex) {
                    ex.printStackTrace(); // Print any exceptions
                }                                           
            }
        };
        
        Goo.start(); // Start the Goo thread
        Comee.start(); // Start the Comee thread
    }
}

// Lamport class
class Lamport {
    int x = 0, y = 0, z = 0, nn = 1;
    int c = new Random().nextInt() + 1;
    boolean s = false;
    String msg = new String();
    
    // Method to handle Go operation
    public void Go() throws Exception {
        System.out.println("\nServer is started successfully\n");
        InetAddress bunch = InetAddress.getByName("224.0.0.7");
        MulticastSocket Socket = new MulticastSocket(8080);
        
        // Loop until condition s is true
        while (!s) {
            msg = Integer.toString(c);
            Socket.send(new DatagramPacket(msg.getBytes(), msg.length(), bunch, 8080));
            Thread.sleep(4000);
        }
    }
    
    // Method to handle Come operation
    public void Come() throws Exception {
        byte[] bf = new byte[1000];
        InetAddress bunch = InetAddress.getByName("224.0.0.7");
        MulticastSocket Socket = new MulticastSocket(8100);
        MulticastSocket Sockets = new MulticastSocket(8080);
        DatagramPacket pd = new DatagramPacket(bf, bf.length);
        Socket.joinGroup(bunch);
        while (!s) {
            Socket.receive(pd);
            String msg = new String(pd.getData(), 0, pd.getLength());
            System.out.println("A client got connected");
            int client = nn;
            if (nn == z) s = true;
            nn++;
            int d = Integer.valueOf(msg);
            x = d / nn;
            y = x - d;
            msg = x + ":" + y + ":" + client + ":" + z;
            c = c + x;
            System.out.println();
            Thread.sleep(2000);
            Sockets.send(new DatagramPacket(msg.getBytes(), msg.length(), bunch, 8080));
        }
        System.out.println("Process completed");
    }
    
    // Method to set the number of nodes
    public void nodes(int a) throws Exception {
        z = a;
    }
}
