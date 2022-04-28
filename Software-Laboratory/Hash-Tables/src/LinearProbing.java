public class LinearProbing {

    public static Employee[] CreateHash(Employee[] employees, MyLinkedList list, int tableSize1) {
        Employee currNode = list.head;
        while (currNode != null) {
            int index = hash1(currNode.getphone(), tableSize1);
            employees = Put(employees, currNode, index, tableSize1);                                                    // place in employee linked list linear probing hash table
            currNode = currNode.next;
        }
        return employees;
    }

    private static Employee[] Put(Employee[] employees, Employee currNode, int index, int searchCount) {                // Placing the employee in the hash table according to linear probing rules
        if(searchCount > 0){
            if(employees[index] == null){
                Employee employee = new Employee(currNode.getemployeeCode(), currNode.getNRIC(), currNode.getphone(), null);
                employees[index] = employee;
            }
            else{
                index = (index + 1) % employees.length;
                Put(employees, currNode, index, searchCount - 1);
            }
        }
        else{
            System.out.println("Hastable is full");
        }
        return employees;
    }

    private static int hash1(int key, int tableSize) {                                                                  // calculate hash
        return (key % tableSize);
    }

    public static void printLinearProb(Employee[] employees, int tableSize1) {
        System.out.println("PART2\n" +
                "Hashtable for Linear Probing");
        for(int i = 0; i < tableSize1; i++){
            if(employees[i] != null){
                System.out.println("["+i+"]--->"+employees[i].getphone());
            }
            else{
                System.out.println("["+i+"]--->null");
            }
        }
    }

    public static void Get(Employee[] employees, int index, int searchKey, int Comparison, long start) {
        index = index % employees.length;
        if(employees[index] == null){
            System.out.println("Key Not Found.");
            return;
        }
        if(employees[index].getphone() == searchKey){
            System.out.println("Key found with "+ Comparison+ " comparisons");
            long time = (System.nanoTime() - start) / 100;
            System.out.println("CPU time taken to search =" + time +" ns" );
            return ;
        }
        else{
            Get(employees, index +1, searchKey, ++Comparison, start);
        }
    }
}
