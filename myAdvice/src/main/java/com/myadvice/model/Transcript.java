package com.myadvice.model;

import jakarta.persistence.*;

@Entity //Class is a database table
public class Transcript {

    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne //Many transcripts can be linked to same student
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne //Many transcripts can be linked to same course
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    //Can't be empty
    @Column(nullable = false)
    private Double grade;

    //Can't be empty
    @Column(nullable = false)
    private String term;

    //Empty constructor
    public Transcript() {}

    //Full constructor
    public Transcript(Student student, Course course, Double grade, String term) {
        this.student = student;
        this.course = course;
        this.grade = grade;
        this.term = term;
    }

    //Getters and Setters
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

    public Double getGrade() {
        return grade;
    }
    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}