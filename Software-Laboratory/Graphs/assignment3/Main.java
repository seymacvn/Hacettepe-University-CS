import javax.print.attribute.standard.NumberOfDocuments;
import java.io.*;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Main {
    public static void main(String args[]) throws IOException {
        File file = new File(args[0]);
        BufferedReader br = new BufferedReader(new FileReader(file));

        System.setOut(new PrintStream(args[1]));                                            // creates output file
        FileWriter writer = new FileWriter(args[1]);

        String strLine;
        strLine = br.readLine();
        int NumberNode = NumberNode(strLine);


        String[][] Capacity = StoreCapacity(strLine, NumberNode);                           // keep their capacities, names and ids with the information in the first line
        String[][] Id = FindId(strLine, NumberNode);

        ArrayList<Node> Nodes = new ArrayList<Node>();
        Graph graph = new Graph(NumberNode);

        for (int i = 0; i < NumberNode; i++){                                               //new nodes are created with their names and capacities
            int id = Integer.parseInt(Id[i][1]);
            int capacity = Integer.parseInt(Capacity[i][1]);
            String name = Capacity[i][0];
            Node node = new Node(name, capacity, id);
            Graph.addEdge(graph, node, node);
            Nodes.add(node);
        }                                                                                   // created nodes arraylist that holds the vertex in the graph

        String SourceNode = br.readLine();
        while ((strLine = br.readLine()) != null) {                                         // vertices read from the input file are added to the graph as edge
            String[] V = strLine.split(" ");
            Node V1 = Node.FindNode(Nodes, V[0]);
            Node V2 = Node.FindNode(Nodes, V[1]);
            Graph.addEdge(graph, V1, V2);
        }

        Node.GraphStructure(graph);                                                         // graph is printed according to the edges and nodes
        Node Source = Node.FindNode(Nodes, SourceNode);                                     // finding source node
        Graph newGraph = new Graph(graph.getNoVertex());                                    // new graph created in broadcasting
        ArrayList<Node> BroadNodes = Graph.CreateBroad(graph, Source);                      // BroadNodes array holds at the vertecies of the new graph
        Stack<Node> BestStack = Graph.Bfs(graph, newGraph, Source, BroadNodes);             // new graph is created after broadcasting in bfs method
        Node.BroadcastSteps(newGraph, BroadNodes);                                          // printing broadcast steps
        Node.messagePassing(BroadNodes, BestStack);                                 // printing mesaage passing
        Node.FindBest(BroadNodes);                                                          // finding and printing best node
    }

    private static String[][] FindId(String strLine, int numberNode) {
        String[][] Id = new String[numberNode][2];
        String[] FirstLine = strLine.split(" ");
        int j = 0;
        for(int i = 0; i< 2*numberNode; i= i+2){
            Id[j][0]= FirstLine[i];
            Id[j][1] = Integer.toString(j++);
        }
        return Id;
    }

    private static int NumberNode(String strLine) {                                         // finds the total number of all nodes in the graph
        char ch = ' ';
        int frequency = 0;
        for(int i = 0; i < strLine.length(); i++) {
            if(ch == strLine.charAt(i)) {
                ++frequency;
            }
        }
        frequency = (frequency + 1) /2;
        return frequency;
    }

    private static String[][] StoreCapacity(String strLine, int numberNode) {               // finds the capacity of nodes
        String[][]Capacity = new String[numberNode][2];
        String[] FirstLine = strLine.split(" ");
        int j = 0;
        for(int i = 0; i< 2*numberNode; i= i+2){
            Capacity[j][0]= FirstLine[i];
            Capacity[j++][1] = FirstLine[i+1];
        }
        return Capacity;
    }
}

