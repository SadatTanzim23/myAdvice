public class Schedule {
    private Long id;
    private Course course;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String roomNumber;
    private String term;

    public Schedule() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Override
    public String toString() {
        String code = (course != null && course.getCourseCode() != null) ? course.getCourseCode() : "N/A";
        return "ID " + (id != null ? id : "-")
                + " | " + code
                + " | " + (dayOfWeek != null ? dayOfWeek : "N/A")
                + " " + (startTime != null ? startTime : "")
                + "-" + (endTime != null ? endTime : "")
                + " | Room " + (roomNumber != null ? roomNumber : "N/A");
    }
}

