import java.io.*;
import java.util.*;
import java.net.*;

public class Client {
    static boolean s = false;
    public static int p = 0;
    
    public static void main(String[] args) throws Exception {
        // Create an instance of the Multicast class
        Multicast sepro = new Multicast();
        sepro.Get(); // Call the Get method to initiate communication
        
        // Create and start threads for different operations
        Thread Enter = new Thread() {
            public void run() {
                try {
                    sepro.Enter(); // Execute the Enter method
                } catch (Exception ex) {
                    ex.printStackTrace(); // Print any exceptions
                }			
            }
        };
        
        Thread Goo = new Thread() {
            public void run() {
                try {
                    sepro.Go(); // Execute the Go method
                } catch (Exception ex) {
                    ex.printStackTrace(); // Print any exceptions
                }			
            }
        };
        
        Thread Comee = new Thread() {
            public void run() {
                try {
                    sepro.Come(); // Execute the Come method
                } catch (Exception ex) {
                    ex.printStackTrace(); // Print any exceptions
                }                                           
            }
        };
        
        Enter.start(); // Start the Enter thread
        Goo.start(); // Start the Goo thread
        Comee.start(); // Start the Comee thread
    }
}

// Multicast class
class Multicast {
    static boolean s = false;
    int c = new Random().nextInt(100)+1; // Generate a random integer
    int x = 0, y = 0, z = 0, n = 0, p_id = 0;
    String msg = new String();
    boolean sn = false;
    
    // Method to get initial data
    public void Get() throws Exception {
        int cn, d;
        byte[] bf = new byte[1000];
        InetAddress bunch = InetAddress.getByName("224.0.0.7");              
        MulticastSocket Socket = new MulticastSocket(8080);
        MulticastSocket Sockets = new MulticastSocket(8100);
        Socket.joinGroup(bunch);
        DatagramPacket pd = new DatagramPacket(bf,bf.length);
        Socket.receive(pd);
        String msg = new String(pd.getData(),0,pd.getLength());
        cn = Integer.parseInt(msg);
        d = c-cn;
        msg = Integer.toString(d);
        DatagramPacket pdp = new DatagramPacket(msg.getBytes(),msg.length(),bunch,8100);
        Sockets.send(pdp);                 
    }
    
    // Method to handle entry
    public void Enter() throws Exception {
        byte[] bf = new byte[1000];
        InetAddress bunch = InetAddress.getByName("224.0.0.7");
        MulticastSocket Sockets = new MulticastSocket(8080);
        Sockets.joinGroup(bunch);
        DatagramPacket pd = new DatagramPacket(bf,bf.length);
        
        // Loop until condition s is true
        while(!s) {
            Sockets.receive(pd); // Receive data
            String msg = new String(pd.getData(),0,pd.getLength());
            String[] en = msg.split(":");
            if(en.length==4) {
                if(!sn) {
                    c = Integer.valueOf(en[1])+c;
                    p_id = Integer.valueOf(en[2]);
                    n = Integer.valueOf(en[3]);
                    sn = true;
                    if(p_id==n) {
                        s = true;
                    }
                } else {
                    c = Integer.valueOf(en[0])+c;
                    if(Integer.valueOf(en[2])==n)
                        s= true;
                }
            }
        }
    }
    
    // Method to perform Go operation
    public void Go() throws Exception {
        String msg = "";
        InetAddress bunch = InetAddress.getByName("224.0.0.7");
        MulticastSocket Socket = new MulticastSocket(8100);
        
        // Loop until condition s is true
        while(!s)
            Thread.sleep(4000);
        for(int k=0;k<2;k++) {
            Thread.sleep(new Random().nextInt(50)+1);
            msg = "Current P["+p_id+"] : ["+k+"]";
            DatagramPacket pd = new DatagramPacket(msg.getBytes(),msg.length(),bunch,8100);
            Socket.send(pd);
        }                                       
    }
    
    // Method to handle Come operation
    public void Come() throws Exception {
        byte[] bf = new byte[1000];
        String msg = "";
        InetAddress bunch = InetAddress.getByName("224.0.0.7");
        MulticastSocket Sockets = new MulticastSocket(8100);
        Sockets.joinGroup(bunch);
        DatagramPacket pd = new DatagramPacket(bf,bf.length);
        System.out.println();
        
        // Loop until condition s is true
        while(!s)
            Thread.sleep(2000);         
        while(true) {
            Sockets.receive(pd);
            msg = new String(pd.getData(),0,pd.getLength());
            if(msg.length()>9)
                System.out.println(msg);
        }
    }
}
