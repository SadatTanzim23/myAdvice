package com.myadvice.dto;

import java.time.LocalDateTime;

//No Database stuff (messenger)
public class BookingRequest {

    //Variables
    private Long studentId;
    private Long facultyId;
    private LocalDateTime dateTime;

    //Empty constructor
    public BookingRequest() {}

    //Full constructor
    public BookingRequest(Long studentId, Long facultyId, LocalDateTime dateTime) {
        this.studentId = studentId;
        this.facultyId = facultyId;
        this.dateTime = dateTime;
    }

    //Getters and Setters
    public Long getStudentId() {
        return studentId;
    }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }


}