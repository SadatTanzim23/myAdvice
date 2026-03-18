package com.myadvice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity //Class is a database table
public class Appointment {
    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-increments
    private Long id;

    @ManyToOne //Many appointments can be connected to one student
    @JoinColumn(name = "student_id", nullable = false) //Foreign key column in appointment table in DB
    private Student student; //Stores whole Student object, not just ID

    @ManyToOne //Many appointments can belong to one faculty member
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime; //LocalDateTime holds both date and time together ex. 2026-03-17 15:30

    //Appointment status (Ex. "cancelled")
    @Column(nullable = false)
    private String status;

    //Empty constructor
    public Appointment() {}

    //Full constructor
    public Appointment(Student student, Faculty faculty, LocalDateTime dateTime, String status) {
        this.student = student;
        this.faculty = faculty;
        this.dateTime = dateTime;
        this.status = status;
    }

    //Setters and Getters
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

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
