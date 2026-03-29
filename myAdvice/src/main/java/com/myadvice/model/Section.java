package com.myadvice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity//class is a database table
public class Section {
    @Id//primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-increments
    private Long id;

    @ManyToOne//many sections can be linked to same course
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne//many sections can be linked to same faculty
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    //column name can't be empty
    @Column(name = "section_number", nullable = false)
    private String sectionNumber;

    //Can't be empty
    @Column(nullable = false)
    private Integer capacity;

    //column name can't be empty
    @Column(name = "enrolled_count", nullable = false)
    private Integer enrolledCount;

    @Column(name = "instructor_name", nullable = false)
    private String instructorName;

    @Column(name = "room")
    private String room;

    @Column(name="day_of_week", nullable = false)
    private String dayOfWeek;

    @Column(name = "lab_day_of_week")
    private String labDayOfWeek;

    @Column(name = "lab_time")
    private String labTime;
    //Empty constructor
    public Section() {}

    //constructor
    public Section(Course course, Faculty faculty, String sectionNumber, Integer capacity, Integer enrolledCount, String instructorName, String dayOfWeek) {
        this.course = course;
        this.faculty = faculty;
        this.sectionNumber = sectionNumber;
        this.capacity = capacity;
        this.enrolledCount = enrolledCount;
        this.instructorName = instructorName;
        this.dayOfWeek = dayOfWeek;
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

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(Integer enrolledCount) {
        this.enrolledCount = enrolledCount;
    }

    public String getInstructorName() {
        return instructorName;
    }
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getLabDayOfWeek() {
        return labDayOfWeek;
    }

    public void setLabDayOfWeek(String labDayOfWeek) {
        this.labDayOfWeek = labDayOfWeek;
    }

    public String getLabTime() {
        return labTime;
    }

    public void setLabTime(String labTime) {
        this.labTime = labTime;
    }



}