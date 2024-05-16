import java.util.*;

// Represents a node in the multi-paxos protocol
class Node {
    private int nodeId;
    private List<Integer> proposedValues;
    private int currentProposalNumber;

    // Constructor
    public Node(int nodeId) {
        this.nodeId = nodeId;
        this.proposedValues = new ArrayList<>();
        this.currentProposalNumber = 0;
    }

    // Propose a value in the multi-paxos protocol
    public void proposeValue(int value) {
        this.proposedValues.add(value);
    }

    // Run the multi-paxos protocol
    public void runMultiPaxos() {
        // Simulate the process of reaching consensus on a value
        System.out.println("Node " + nodeId + " executing multi-paxos protocol...");
        System.out.println("Proposed values: " + proposedValues);
        System.out.println("Current proposal number: " + currentProposalNumber);
        // Additional implementation of multi-paxos protocol goes here
    }
}

// Main class
public class MultiPaxos {
    public static void main(String[] args) {
        // Create nodes for the multi-paxos protocol
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);

        // Node 1 proposes values
        node1.proposeValue(100);
        node1.proposeValue(200);
        node1.proposeValue(300);

        // Node 2 proposes values
        node2.proposeValue(150);
        node2.proposeValue(250);

        // Node 3 proposes values
        node3.proposeValue(175);
        node3.proposeValue(275);
        node3.proposeValue(325);

        // Run multi-paxos protocol on each node
        node1.runMultiPaxos();
        node2.runMultiPaxos();
        node3.runMultiPaxos();
    }
}
