public class Enrollment {
    private Long id;
    private Student student;
    private Course course;
    private Section section;
    private String labDayOfWeek;
    private String labTime;

    public Enrollment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getLabDayOfWeek() {
        return labDayOfWeek;
    }

    public void setLabDayOfWeek(String labDayOfWeek) {
        this.labDayOfWeek = labDayOfWeek;
    }

    public String getLabTime() {
        return labTime;
    }

    public void setLabTime(String labTime) {
        this.labTime = labTime;
    }
}

