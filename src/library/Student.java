
package library;

public class Student {
    private String studentId;
    private String name;
    private String department;
    private String phone;

    public Student(String studentId, String name,
                   String department, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.phone = phone;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) {
        this.department = department;
    }
    public void setPhone(String phone) { this.phone = phone; }
}