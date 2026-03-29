package com.myadvice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity //tells JPA this class is a database table
public class Faculty {
    @Id //field is marked as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increments ID for each new faculty in database
    private Long id;

    @Column (name = "first_name", nullable = false)//names column first_name in database
    private String firstName;

    @Column(name = "last_name", nullable = false)//name in DB, and can't be empty
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String department;

    public Faculty() {}//empty constructor JPA needs to create Faculty objects

    //full constructor
    public Faculty(String firstName, String lastName, String email, String department) { //Creates new faculty with data
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }







}