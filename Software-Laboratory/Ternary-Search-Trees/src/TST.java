import java.util.LinkedList;
import java.util.Queue;

public class TST {
    private int NodeNumber;                                                            // size of TST
    private Node root;                                                                 // root of TST

    public class Node {
        private char Char;                                                             
        private Node left, mid, right;                                                 // left, middle, and right subtries
        private Float Weight;                                                          // weight value associated with nGram
        private Integer Phish;														   // phishing-occurrence value associated with nGram
        private Integer Legitimate;													   // legitimate-occurrence value associated with nGram
        private boolean isleaf;														   // checks to be the last char


        public Float getWeight() {
            return Weight;
        }

        public void setWeight(Float Weight) {
            this.Weight = Weight;
        }

        public Integer getPhish() {
            return Phish;
        }

        public void setPhish(Integer Phish) {
            this.Phish = Phish;
        }

        public Integer getLegitimate() {
            return Legitimate;
        }

        public void setLegitimate(Integer Legitimate) {
            this.Legitimate = Legitimate;
        }
    }

    public int size(TST trie) {																			// returns number of nGrams in TST 
        return trie.NodeNumber;
    }

    public boolean contains(String key) {																// searchs nGram in TST
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    public Float get(String key) {																		// Returns the weight of the nGram called
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node node = get(root, key, 0);
        if (node == null) return null;
        return node.Weight;
    }

    public Integer getLeg(String key) {																	// Returns the legitimate-occurrence of the nGram called
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node node = get(root, key, 0);
        if (node == null) return null;
        return node.Legitimate;
    }

    public Integer getPhish(String key) {															 	 // Returns the phishing-occurrence of the nGram called
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node node = get(root, key, 0);
        if (node == null) return null;
        return node.Phish;
    }

    public Node getNode(String key) {																	// Returns the Node of the nGram called
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node node = get(root, key, 0);
        if (node == null){
            return null;
        }
        return node;
    }

    // return subtrie corresponding to given key
    private Node get(Node node, String key, int d) {
        if (node == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char Char = key.charAt(d);
        if (Char < node.Char) return get(node.left, key, d);
        else if (Char > node.Char) return get(node.right, key, d);
        else if (d == key.length() - 1 && node.isleaf == false) return null;                                             // if node is deleted
        else if (d < key.length() - 1) return get(node.mid, key, d + 1);

        else return node;
    }

    public void put(String key, Integer val, String type) {																// adding new node in TST
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(key)) NodeNumber++;
        else if (val == null) NodeNumber--;       																		// delete existing key
        root = put(root, key, val, 0, type);
    }

    private Node put(Node node, String key, Integer val, int d, String type) {
        char Char = key.charAt(d);
        if (node == null) {
            node = new Node();
            node.Char = Char;																							// isLeaf = true in the last character of nGram
            node.isleaf = true;
        }
        if (Char < node.Char) node.left = put(node.left, key, val, d, type);
        else if (Char > node.Char) node.right = put(node.right, key, val, d, type);
        else if (d < key.length() - 1) node.mid = put(node.mid, key, val, d + 1, type);
        else{
            node.Weight = 0.0f;
            if(type.equals("legitimate")){
                node.Legitimate = 1;
                node.Phish = 0;
            }
            else if (type.equals("phishing")){
                node.Legitimate = 0;
                node.Phish = 1;
            }
        }
        return node;
    }

    public void delete(String word) {
        delete(root, word.toCharArray(), 0);
    }
    
    private void delete(Node r, char[] word, int ptr){																	// function to delete a word 
        if (r == null){
            return;
        }

        if (word[ptr] < r.Char){
            delete(r.left, word, ptr);
        }
        else if (word[ptr] > r.Char){
            delete(r.right, word, ptr);
        }
        else{
            if (r.isleaf && ptr == word.length - 1){																	// to delete a word just make isleaf false
                r.isleaf = false;
            }

            else if (ptr + 1 < word.length){
                delete(r.mid, word, ptr + 1);
                //r.mid = null;
            }
        }
    }

    public Iterable<String> keys() {
        Queue<String> queue = new LinkedList<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    // all keys in subtrie rooted with given prefix
    private void collect(Node node, StringBuilder prefix, Queue<String> queue) {
        if (node == null) return;
        collect(node.left,  prefix, queue);
        if (node.Weight != null && node.isleaf) queue.add(prefix.toString() + node.Char);
        else if (node.Weight != null && !node.isleaf) ;
        collect(node.mid,   prefix.append(node.Char), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(node.right, prefix, queue);
    }
}