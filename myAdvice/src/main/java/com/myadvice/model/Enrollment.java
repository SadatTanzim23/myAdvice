package com.myadvice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "lab_day_of_week")
    private String labDayOfWeek;

    @Column(name = "lab_time")
    private String labTime;

    public Enrollment() {//constructors
    }

    public Enrollment(Student student, Course course, Section section, String labDayOfWeek, String labTime) {
        this.student = student;
        this.course = course;
        this.section = section;
        this.labDayOfWeek = labDayOfWeek;
        this.labTime = labTime;
    }

    //getters and setters
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

