import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class Graph {
    private int NoVertex;                                                                                   // number of vertices
    public  LinkedList<Node>[] adj;                                                                         // adjacency Lists for nodes

    Graph(int NoVertex) {
        this.NoVertex = NoVertex;

        adj = new LinkedList[NoVertex];
        for (int i=0; i<NoVertex; ++i)
            adj[i] = new LinkedList();
    }

    public static void addEdge(Graph graph, Node v1, Node v2) {                                             // adding edge to graph
        graph.adj[v1.getId()].add(v2);
        graph.adj[v2.getId()].add(v1);
        Node.addParent(v1, v2);
    }

    public static ArrayList<Node> CreateBroad(Graph graph, Node source) {                                  // record the nodes that will occur at the end of broadcasting
        ArrayList<Node> BroadNode = new ArrayList<Node>();
        Node current = new Node(source.getName(), source.getCapacity(), source.getId());                   // starting from source node

        boolean election[] = new boolean[graph.getNoVertex()];
        LinkedList<Node> queue = new LinkedList<Node>();                                                      // create a queue for broadcasting

        election[current.getId()]=true;
        queue.add(current);
        int index = 0;
        while (queue.size() != 0) {
            current = queue.poll();                                                                        // get all adjacent vertices of the dequeued vertex current
            int CurrIndex= current.getId();
            current.setId(index ++);
            BroadNode.add(current);                                                                        // nodes are added to the broadnode array in order of their visit
            Iterator<Node> i = graph.adj[CurrIndex].listIterator();
            while (i.hasNext()) {
                Node nodeChild = i.next();
                Node Child = new Node (nodeChild.getName(), nodeChild.getCapacity(), nodeChild.getId());
                int indexChild = Child.getId();
                if (!election[indexChild]) {                                                               // If a adjacent has not been election, then add queue
                    election[indexChild] = true;
                    queue.add(Child);
                }
            }
        }
        return BroadNode;
    }

    public static Stack<Node> Bfs(Graph graph, Graph newGraph, Node source, ArrayList<Node> Nodes) {       // create the nodes that will occur at the end of broadcasting
        Node current = new Node(source.getName(), source.getCapacity(), source.getId());                   // starting from source node
        boolean election[] = new boolean[graph.getNoVertex()];

        LinkedList<Node> queue = new LinkedList<Node>();                                                      // create a queue for broadcasting
        Stack<Node> stack = new Stack<>();                                                                 // added to every vertex stack visited (for finding best node)

        election[current.getId()]=true;                                                                    // every node visited is marked as election
        queue.add(current);
        stack.push(current);
        while (queue.size() != 0) {                                                                        // traveling the graph in level order and creating new broadcasting graph
            current = queue.poll();

            for(int i =0; i < graph.adj[current.getId()].size(); i++){                                     // traveling neighbors of current node
                Node n = graph.adj[current.getId()].get(i);
                Node Child = new Node(n.getName(), n.getCapacity(), n.getId());
                if (!election[Child.getId()]) {
                    election[Child.getId()] = true;                                                       // child vertex if not visited before
                    Node V1 = Node.FindNode(Nodes, current.getName());
                    Node V2 = Node.FindNode(Nodes, Child.getName());
                    Graph.AddEdge(newGraph, V1, V2);                                                     // edge is added to the graph with current node and child node
                    queue.add(Child);
                    stack.push(Child);
                }
            }
        }
        return stack;                                                                                    // stack will be used in message passing.
    }


    public int getNoVertex() {
        return NoVertex;
    }

    public void setNoVertex(int NoVertex) {
        this.NoVertex = NoVertex;
    }

    public LinkedList<Node>[] getAdj() {
        return adj;
    }

    public void setAdj( LinkedList<Node>[] adj) {
        this.adj = adj;
    }

    public static void AddEdge(Graph graph, Node v1, Node v2) {
        graph.adj[v1.getId()].add(v2);
        Node.addParent(v1, v2);
    }
}
