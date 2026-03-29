package com.myadvice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity //Tells JPA this class is a database table
public class Student {

    @Id //Field is marked as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-increments ID for each new student
    private Long id;
    //Column named first_name can't be empty
    @Column(name = "first_name", nullable = false)
    private String firstName;
    //Column named last_name can't be empty
    @Column(name = "last_name", nullable = false)
    private String lastName;
    // Column named faculty_name can't be empty
    @Column(name="faculty_name",nullable = false)
    private String facultyName;
    // Column named program_name can't be empty
    @Column(name="program_name",nullable = false)
    private String programName;
    //Can't be empty students can't have the same email
    @Column(nullable = false, unique = true)
    private String email;
    //Column named student_number can't be empty, students can't have same student number
    @Column(name = "student_number", nullable = false, unique = true)
    private String studentNumber;

    //constructor
    public Student() {}

    //constructor
    public Student(String firstName, String lastName, String email, String studentNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentNumber = studentNumber;
    }

    //Getters and setters
    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getStudentNumber() {
        return studentNumber;
    }
    public String getEmail() {
        return email;
    }
    public String getFacultyName() {
        return facultyName;
    }
    public String getProgramName() {
        return programName;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
    public void setProgramName(String programName) {
        this.programName = programName;
    }
}