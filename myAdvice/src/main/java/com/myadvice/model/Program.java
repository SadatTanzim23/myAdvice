package com.myadvice.model;

import jakarta.persistence.*;

@Entity //Marks class as a database table
public class Program {
    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-increments ID for each new program
    private Long id;

    //Can't be empty
    @Column(nullable = false)
    private String name;

    //Can't be empty
    @Column(nullable = false)
    private String department;

    //Column named total_credits
    @Column(name = "total_credits")
    private Integer totalCredits;

    //Empty constructor needed by JPA
    public Program() {}

    //Full constructor
    public Program(String name, String department, Integer totalCredits) {
        this.name = name;
        this.department = department;
        this.totalCredits = totalCredits;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Integer totalCredits) {
        this.totalCredits = totalCredits;
    }
}
