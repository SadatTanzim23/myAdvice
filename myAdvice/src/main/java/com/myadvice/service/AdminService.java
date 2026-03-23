package com.myadvice.service;

import com.myadvice.model.Course;
import com.myadvice.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private CourseRepository courseRepository;

    // Add a new course to the database
    public Course addCourse(Course course){
        // Check if course already exists
        if(courseRepository.findAll().stream().anyMatch(c -> c.getCourseCode().equals(course.getCourseCode()))){
            throw new RuntimeException("Course already exists");
        }
        // Save course if it doesn't already exist
        return courseRepository.save(course);
    }
}
