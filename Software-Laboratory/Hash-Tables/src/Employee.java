public class Employee {
    private String employeeCode;
    private String NRIC;
    private int phone;
    Employee next;

    public Employee(String employeeCode, String NRIC, int phone, Employee next) {
        this.employeeCode= employeeCode;
        this.NRIC= NRIC;
        this.phone= phone;
        this.next = null;
    }
    public String getemployeeCode() {
        return employeeCode;
    }

    public void setemployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getNRIC() {
        return NRIC;
    }

    public void setNRIC(String NRIC) {
        this.NRIC = NRIC;
    }
    public int getphone() {
        return phone;
    }

    public void setphone(int phone) {
        this.phone = phone;
    }

    public  Employee  getnext() {
        return next;
    }

    public void setnext(Employee next) {
        this.next = next;
    }
}
