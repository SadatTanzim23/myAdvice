package com.myadvice.model;

import jakarta.persistence.*;

@Entity //Class is a database table
public class Section {
    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-increments
    private Long id;

    @ManyToOne //Many sections can be linked to same course
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne //Many sections can be linked to same faculty
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    //Column name, can't be empty
    @Column(name = "section_number", nullable = false)
    private String sectionNumber;

    //Can't be empty
    @Column(nullable = false)
    private Integer capacity;

    //Column name, can't be empty
    @Column(name = "enrolled_count", nullable = false)
    private Integer enrolledCount;

    //Empty constructor
    public Section() {}

    //Full constructor
    public Section(Course course, Faculty faculty, String sectionNumber, Integer capacity, Integer enrolledCount) {
        this.course = course;
        this.faculty = faculty;
        this.sectionNumber = sectionNumber;
        this.capacity = capacity;
        this.enrolledCount = enrolledCount;
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

}