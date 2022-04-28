public class MyHashTable {
    private static int tableSize;
    private MyLinkedList[] tableMember;

    public MyHashTable(MyLinkedList[] tableMember, int tableSize) {
        this.tableMember= tableMember;
        this.tableSize = tableSize;
    }

    public MyLinkedList[] gettableMember() {
        return tableMember;
    }

    public void settableMember(MyLinkedList[] tableMember) {
        this.tableMember = tableMember;
    }

    private static int hash1(int key) {
        return (key % tableSize);
    }

    public static void CreateHash(MyLinkedList list, MyHashTable myHashTable) {
        Employee currNode = list.head;
        while (currNode != null) {
            myHashTable = MyHashTable.Put(myHashTable, currNode);
            currNode = currNode.next;
        }
    }

    public static MyHashTable Put(MyHashTable myHashTable, Employee currNode) {
        int index = hash1(currNode.getphone());
        if( myHashTable.tableMember[index] == null){                                                                    //if there is no element in the index, a new linked list is created
            Employee employee = new Employee(currNode.getemployeeCode(), currNode.getNRIC(), currNode.getphone(), null);
            MyLinkedList list = new MyLinkedList(employee);
            myHashTable.tableMember[index] = list;
        }
        else{                                                                                                           // employee will be added to the linkedlist in the indexth element of the hash table
            Employee employee = new Employee(currNode.getemployeeCode(), currNode.getNRIC(), currNode.getphone(), null);
            Employee last = myHashTable.tableMember[index].head;
            while (last.next != null){
                last = last.next;
            }
            last.next = employee;
        }
        return myHashTable;
    }

    public static Employee Get(MyHashTable myHashTable, int searchKey, long start) {                                    // search for phoneNumber
        System.out.println("SEPARATE CHAINING:");
        int index = hash1(searchKey), comparisons = 1;
        if( myHashTable.tableMember[index] == null){
            System.out.println("Key Not Found");
            return null;
        }
        Employee search = myHashTable.tableMember[index].head;
        if(searchKey == search.getphone()){
            System.out.println("Key found with " + comparisons +" comparisons");
            long time = (System.nanoTime() - start) / 100;
            System.out.println("CPU time taken to search =" + time +" ns" );
            return search;
        }
        else{
            while (search != null) {
                if(search.getphone() == searchKey){
                    System.out.println("Key found with " + comparisons +" comparisons");
                    long time = (System.nanoTime() - start) / 100;
                    System.out.println("CPU time taken to search =" + time +" ns" );
                    return search;
                }
                else{
                    comparisons++;
                }
                search = search.next;
            }
            System.out.println("Key Not Found");
            return null;
        }
    }

    public static void printHash(MyHashTable myHashTable, int tableSize) {
        System.out.println("PART1");
        for(int i = 0; i < tableSize; i ++){
            if(myHashTable.gettableMember()[i] != null){
                System.out.print("[Chain "+ i +"]: ");
                printList(myHashTable.gettableMember()[i]);
            }
            else{
                System.out.println("[Chain "+ i +"]: Null");
            }
        }
    }

    public static void printList(MyLinkedList list) {
        Employee currNode = list.head;
        int node = 0;
        while (currNode != null) {
            if(node != 0){
                System.out.print("---->");
            }
            System.out.print(currNode.getphone());
            currNode = currNode.next;
            node ++;
        }
        System.out.println();
    }
}