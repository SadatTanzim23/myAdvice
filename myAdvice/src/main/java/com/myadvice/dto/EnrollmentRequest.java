package com.myadvice.dto;

public class EnrollmentRequest {
    private Long studentId;
    private Long courseId;
    private Long sectionId;
    private String labDayOfWeek;
    private String labTime;

    public EnrollmentRequest() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
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

