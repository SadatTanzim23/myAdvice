public class Section {
    private Long id;
    private Course course;
    private Faculty faculty;
    private String sectionNumber;
    private Integer capacity;
    private Integer enrolledCount;
    private String instructorName;
    private String dayOfWeek;
    private String room;
    private String labDayOfWeek;
    private String labTime;

    public Section() {}

    public Section(Course course, Faculty faculty, String sectionNumber,
                   Integer capacity, Integer enrolledCount,
                   String instructorName, String dayOfWeek) {
        this.course = course;
        this.faculty = faculty;
        this.sectionNumber = sectionNumber;
        this.capacity = capacity;
        this.enrolledCount = enrolledCount;
        this.instructorName = instructorName;
        this.dayOfWeek = dayOfWeek;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }

    public String getSectionNumber() { return sectionNumber; }
    public void setSectionNumber(String sectionNumber) { this.sectionNumber = sectionNumber; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getEnrolledCount() { return enrolledCount; }
    public void setEnrolledCount(Integer enrolledCount) { this.enrolledCount = enrolledCount; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getLabDayOfWeek() { return labDayOfWeek; }
    public void setLabDayOfWeek(String labDayOfWeek) { this.labDayOfWeek = labDayOfWeek; }

    public String getLabTime() { return labTime; }
    public void setLabTime(String labTime) { this.labTime = labTime; }

    @Override
    public String toString() {
        String courseLabel = (course != null && course.getCourseCode() != null)
                ? course.getCourseCode()
                : "NoCourse";
        return courseLabel + " - Sec " + (sectionNumber != null ? sectionNumber : "");
    }
}