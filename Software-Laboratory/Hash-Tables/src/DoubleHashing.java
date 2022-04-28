public class DoubleHashing {

    private static int hash1(int key, int tableSize) {
        return (key % tableSize);
    }

    private static int hash2(int key, int tableSize, int i) {
        return 1 + (key % (tableSize - 1));
    }

    public static Employee[] CreateHash(Employee[] employees, MyLinkedList list, int tableSize1) {
        Employee currNode = list.head;
        while (currNode != null) {
            int index = hash1(currNode.getphone(), tableSize1);
            employees = Put(employees, currNode, index,1);
            currNode = currNode.next;
        }
        return employees;
    }

    private static Employee[] Put(Employee[] employees, Employee currNode, int index, int i) {
        index = index % (employees.length);
        if(i <= employees.length){
            if(employees[index] == null){
                Employee employee = new Employee(currNode.getemployeeCode(), currNode.getNRIC(), currNode.getphone(), null);
                employees[index] = employee;
            }
            else{
                int h1 = hash1 (currNode.getphone(), employees.length);
                int h2 = hash2(currNode.getphone(), employees.length, i);
                index = (h1 + (i * h2)) % employees.length;
                Put(employees, currNode, index, ++i);
            }
        }
        else{
            System.out.println("Hastable is full");
        }
        return employees;
    }

    public static void printDoubleHash(Employee[] employees, int tableSize1) {
        System.out.println("Hashtable for Double Hashing");
        for(int i = 0; i < tableSize1; i++){
            if(employees[i] != null){
                System.out.println("["+i+"]--->"+employees[i].getphone());
            }
            else{
                System.out.println("["+i+"]--->null");
            }
        }
    }

    public static void Get(Employee[] employee, int index, int searchKey, int Comparison,  long start) {
        index = index % employee.length;
        if(employee[index] == null){
            System.out.println("Key Not Found.");
            return;
        }
        if(employee[index].getphone() == searchKey){
            System.out.println("Key found with "+ Comparison+ " comparisons");
            long time = (System.nanoTime() - start) / 100;
            System.out.println("CPU time taken to search =" + time +" ns" );
            return ;
        }
        else{
            int h1 = hash1 (searchKey, employee.length);
            int h2 = hash2(searchKey, employee.length, Comparison);
            index = (h1 + (Comparison * h2)) % employee.length;
            Get(employee, index, searchKey, ++Comparison, start);
        }
    }
}