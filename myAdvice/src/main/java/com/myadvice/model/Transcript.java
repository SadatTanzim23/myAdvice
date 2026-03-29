package com.myadvice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity//class is a database table
public class Transcript {

    @Id//primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne//many transcripts can be linked to same student
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne//any transcripts can be linked to same course
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    //Can't be empty
    @Column(nullable = false)
    private Double grade;

    //Can't be empty
    @Column(nullable = false)
    private String term;

    //empty constructor
    public Transcript() {}

    //constructor
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