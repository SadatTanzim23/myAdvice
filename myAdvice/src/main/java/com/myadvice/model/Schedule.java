package com.myadvice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity //Class is a database table
public class Schedule {

    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-increments
    private Long id;

    @ManyToOne //Many schedules can be connected to the same course
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    //Column name, can't be empty
    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    //Column name, can't be empty
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    //Column name, can't be empty
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    //Column name, can't be empty
    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    //Can't be empty
    @Column(nullable = false)
    private String term;

    //Empty constructor
    public Schedule() {}

    //Full constructor
    public Schedule(String dayOfWeek, LocalTime startTime, LocalTime endTime, String roomNumber, String term) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomNumber = roomNumber;
        this.term = term;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}