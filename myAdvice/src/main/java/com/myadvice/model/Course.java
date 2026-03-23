package com.myadvice.model;

import jakarta.persistence.*;

@Entity //Class as database table
public class Course {

    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-increments
    private Long id;

    //Column named course_code, can't be empty, two courses can't have same code
    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    //Column named course_name, can't be empty, two courses can't have same name
    @Column(name = "course_name", nullable = false, unique = true)
    private String courseName;

    //Can't be empty
    @Column(nullable = false)
    private Integer credits;


    //Course description
    private String description;

    //Empty constructor
    public Course() {}

    //Full constructor
    public Course(String courseCode, String courseName, Integer credits, String description) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.description = description;
    }

    //Getters and Setters
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


}