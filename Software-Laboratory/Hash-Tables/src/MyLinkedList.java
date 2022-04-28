import java.io.*;

public class MyLinkedList {

    Employee head;

    public MyLinkedList(Employee head) {
        this.head= head;
    }

    public Employee gethead() {
        return head;
    }

    public void sethead(Employee head) {
        this.head = head;
    }

    public static MyLinkedList insert(MyLinkedList list, Employee employee){
        if (list.head == null) {
            list.head = employee;
        }
        else {
            Employee last = list.head;
            while (last.next != null) {
                last = last.next;
            }

            last.next = employee;
        }
        return list;
    }
}