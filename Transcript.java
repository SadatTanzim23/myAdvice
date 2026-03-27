public class Transcript {
    private Long id;
    private Student student;
    private Course course;
    private Double grade;
    private String term;

    public Transcript() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    @Override
    public String toString() {
        String studentLabel = student != null ? student.toString() : "Unknown Student";
        String courseLabel = course != null ? course.toString() : "Unknown Course";
        return studentLabel + " | " + courseLabel + " | " + (grade != null ? grade : "N/A") + " | " + (term != null ? term : "");
    }
}

