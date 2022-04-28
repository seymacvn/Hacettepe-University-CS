import java.io.*;

public class main {
    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        BufferedReader br = new BufferedReader(new FileReader(file));

        System.setOut(new PrintStream("output.txt"));                                                          // creates output file
        FileWriter writer = new FileWriter("output.txt");

        String strLine;
        String firstLine = br.readLine();
        if(firstLine.equals("E_Code NRIC Phone") && args.length == 4){                                                  // checks the content of the first line
            int NoEmployee = 0;
            MyLinkedList list = null;

            while ((strLine = br.readLine()) != null && !strLine.equals("")) {                                          // reads the input file
                String[] parts = strLine.split(" ");
                Employee employee= new Employee(parts[0], parts[1], Integer.parseInt(parts[2]), null);            // creates employee for each row
                if( NoEmployee == 0){
                    list = new MyLinkedList(employee);
                    NoEmployee ++;
                }
                else{
                    list = MyLinkedList.insert(list, employee);                                                         // "list" is created from the employees in the input file
                    NoEmployee ++;
                }
            }

            int TableSize = (int) (NoEmployee / Double.parseDouble(args[1]));                                           // TableSize for separate chaining
            int TableSize1 = (int) (NoEmployee / Double.parseDouble(args[2]));                                          // TableSize1 for linear probing and double hashing

            if(CheckLoadFactor(args[1], args[2])){                                                                        // load factor (L2) can not be greater than 1

                String[] name = args[0].split(".txt");
                System.out.println(name[0] +",LF1="+ args[1]+",LF2="+ args[2]+ ","+ args[3]);

                MyLinkedList[] TableMember = new MyLinkedList[TableSize];
                MyHashTable myHashTable = new MyHashTable(TableMember, TableSize);
                MyHashTable.CreateHash(list, myHashTable);                                                              //  creates hash table for separate chaining
                MyHashTable.printHash(myHashTable, TableSize);                                                          // prints hashtabe (separate chaining)

                Employee[] employees = new Employee[TableSize1];
                Employee[] employee = new Employee[TableSize1];

                employees = LinearProbing.CreateHash(employees, list, TableSize1);                                      // creates hash table for linear probing
                LinearProbing.printLinearProb(employees, TableSize1);                                                   // prints hashtabe (linear probing)

                employee = DoubleHashing.CreateHash(employee, list, TableSize1);                                        // creates hash table for double hashing
                DoubleHashing.printDoubleHash(employee, TableSize1);                                                    // prints hashtabe (double hashing)

                int searchKey = Integer.parseInt(args[3]);                                                              // searching phone number
                int index = (searchKey % TableSize1);
                long start = System.nanoTime();
                MyHashTable.Get(myHashTable, searchKey, start);

                System.out.println("LINEAR PROBING:");
                start = System.nanoTime();
                LinearProbing.Get(employees, index, searchKey, 1, start);

                System.out.println("DOUBLE HASHING:");
                start = System.nanoTime();
                DoubleHashing.Get(employee, index, searchKey, 1, start);
            }
        }
    }

    private static boolean CheckLoadFactor(String arg, String arg1) {
        double L1 =  Double.parseDouble(arg);
        double L2 =  Double.parseDouble(arg1);
        if(0 < L1 && 0 < L2 && L2 < 1){
            return true;
        }
        else{
            System.out.println("Invalid load factor");
            return false;
        }
    }
}