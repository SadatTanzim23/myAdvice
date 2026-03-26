public class Student {
    private Long id;
    private String firstName;
    private String lastName;
    private String facultyName;
    private String programName;
    private String email;
    private String studentNumber;

    public Student() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }

    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    @Override
    public String toString() {
        String name = ((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")).trim();
        if (!name.isEmpty()) {
            return name + (studentNumber != null ? " (" + studentNumber + ")" : "");
        }
        return studentNumber != null ? "Student " + studentNumber : "Student";
    }
}

