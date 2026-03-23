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

    // Edit an existing course
    public Course editCourse(Long id, Course updatedCourse){
        // Check if course exists
        Course existing = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        // Update course details
        existing.setCourseCode(updatedCourse.getCourseCode());
        existing.setCourseName(updatedCourse.getCourseName());
        existing.setCredits(updatedCourse.getCredits());
        existing.setDescription(updatedCourse.getDescription());

        // Save updated course to the database
        return courseRepository.save(existing);
    }

    public void deleteCourse(Long id){
        // Check if course exists, or throw an exception if not
        courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        // Delete course from the database
        courseRepository.deleteById(id);
    }
}
