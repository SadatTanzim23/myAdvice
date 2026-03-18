package com.myadvice.model;

import jakarta.persistence.*;

@Entity //Tells JPA this class is a database table
public class Student {

    @Id //Field is marked as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-increments ID for each new student
    private Long id;
    //Column named first_name, can't be empty
    @Column(name = "first_name", nullable = false)
    private String firstName;
    //Column named last_name, can't be empty
    @Column(name = "last_name", nullable = false)
    private String lastName;
    //Can't be empty, students can't have the same email
    @Column(nullable = false, unique = true)
    private String email;
    //Column named student_number, can't be empty, students can't have same student number
    @Column(name = "student_number", nullable = false, unique = true)
    private String studentNumber;

    //Empty constructor required for JPA to create Student object
    public Student() {}

    //Full constructor to create new Student with all data
    public Student(String firstName, String lastName, String email, String studentNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentNumber = studentNumber;
    }

    //Getters
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

    //Setters
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
}
