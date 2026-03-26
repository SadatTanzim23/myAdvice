import java.util.List;

/**
 * Local Course class for Swing frontend
 * Mirrors the backend com.myadvice.model.Course class
 */
public class Course {
    private Long id;
    private String courseCode;
    private String courseName;
    private Integer credits;
    private String description;
    private List<Course> prerequisites;

    public Course() {}

    public Course(String courseCode, String courseName, Integer credits, String description) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.description = description;
    }

    public Course(Long id, String courseCode, String courseName, Integer credits, String description) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName;
    }

    public List<Course> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<Course> prerequisites) {
        this.prerequisites = prerequisites;
    }

    // Backward-compatible aliases if older UI code still uses the previous naming.
    public List<Course> getPrerequisiteIds() {
        return prerequisites;
    }

    public void setPrerequisiteIds(List<Course> prerequisiteIds) {
        this.prerequisites = prerequisiteIds;
    }
}

