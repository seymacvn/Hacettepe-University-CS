import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Node {                                                                             // node class represents the vertex in the graph
    private String Name;
    private int Capacity;
    private int Id;
    public Node Parent;


    Node(String Name, int Capacity, int Id){
        this.Name= Name;
        this.Capacity= Capacity;
        this.Id= Id;
        Parent = null;
    }

    public static Node FindNode(ArrayList<Node> nodes, String nodeName) {                      // finds the node given name
        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).Name.equals(nodeName)){
                return nodes.get(i);
            }
        }
        return null;
    }

    public static Node FindNodee(ArrayList<Node> nodes, int NodeIndex) {                     // finds the node given Id number
        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).getId() == NodeIndex){
                return nodes.get(i);
            }
        }
        return null;
    }

    public static void addParent(Node v1, Node v2){                                          // Adds parent to given node
        v2.Parent = v1;
    }

    public static void GraphStructure(Graph graph) {                                        // printing nodes and their neighbors name -
        System.out.println("Graph structure:");                                             // in the order in which they are added to the graph
        for(int i = 0; i < graph.adj.length; i++){
            LinkedList<Node> A = graph.adj[i];                                              // traveling adjacency list
            Node node = A.get(0);
            System.out.print(node.getName()+"("+ node.getCapacity()+")-->");
            for(int j = 2; j< A.size(); j++){
                System.out.print(A.get(j).getName()+ " ");
            }
            System.out.println();
        }
    }

    public static void BroadcastSteps(Graph graph, ArrayList<Node> Nodes) {                // printing the nodes and their neighbors of the -
        System.out.println("Broadcast steps :");                                           // graph formed at the end of broadcasting
        for(int i = 0; i < graph.adj.length; i++){
            LinkedList<Node> A = graph.adj[i];
            if(A.size() > 0){
                Node node = FindNodee(Nodes,i);
                System.out.print(node.getName()+ "-->");
                for(int j = 0; j< A.size(); j++){
                    System.out.print(A.get(j).getName()+ " ");
                }
                System.out.println();
            }
        }
    }

    public static void messagePassing(ArrayList<Node> broadNode, Stack<Node> bestStack) {
        ArrayList<String>[] BestNodes = new ArrayList[broadNode.size()];                   // compares capacities from leaf node to source node
        for (int i=0; i<broadNode.size(); ++i){
            BestNodes[i] = new ArrayList<String>();
        }
        System.out.println("Message passing:");
        for(int i = 0; i < broadNode.size(); i++){
            BestNodes[i].add(broadNode.get(i).getName());
            BestNodes[i].add(String.valueOf(broadNode.get(i).getCapacity()));
        }

        while (! bestStack.empty()){
            Node Pop = bestStack.pop();
            Node Current = FindNode(broadNode, Pop.getName());
            int currİndex = Current.getId();
            int CurrCapacity = Integer.parseInt(BestNodes[currİndex].get(1));
            if(Current.Parent != null){                                                  // provides transfer up to source node
                Node Parent = Current.Parent;
                int ParentCapacity = Integer.parseInt(BestNodes[Parent.getId()].get(1));
                System.out.print(Current.getName()+"--->");
                for(int j = 0; j < BestNodes[Current.getId()].size(); j= j+2){
                    System.out.print("["+ BestNodes[Current.getId()].get(j) + "," + BestNodes[Current.getId()].get(j+1)+ "]");
                }
                System.out.println("--->" + Parent.getName());
                if(CurrCapacity > ParentCapacity){                                      // if the capacity of current node is greater than the capacity of its parent node
                    BestNodes[Parent.getId()].clear();
                    for(int i = 0; i < BestNodes[Current.getId()].size(); i ++){        // the parent's capacity is changed.
                        BestNodes[Parent.getId()].add(BestNodes[Current.getId()].get(i));
                    }
                }
                else if (CurrCapacity == ParentCapacity){                               // if the capacity of current node is equal to the capacity of its parent node
                    for(int i = 0; i < BestNodes[Current.getId()].size(); i ++){        // best capacities of the current node are also added to the parent node
                        BestNodes[Parent.getId()].add(BestNodes[Current.getId()].get(i));
                    }
                }
            }
        }
    }

    public static void FindBest(ArrayList<Node> broadNode) {                            // travel to all nodes and prints the largest capacity nodes
        int BestIndex = 0;
        ArrayList<String> BestNodes = new ArrayList<>();
        for(int i = 0; i < broadNode.size(); i ++){
            int current = broadNode.get(i).getCapacity();
            int BestNode = broadNode.get(BestIndex).getCapacity();
            if(current > BestNode){
                BestNodes.clear();
                BestNodes.add(broadNode.get(i).getName());
                BestIndex = i;
            }
            else if(current == BestNode){
                BestNodes.add(broadNode.get(i).getName());
                BestIndex = i;
            }
        }
        System.out.print("Best node-->");
        for(int i = 0; i < BestNodes.size(); i ++){
            System.out.print(BestNodes.get(i) + " ");
        }
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int Capacity) {
        this.Capacity = Capacity;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
}
