package com.myadvice.service;

import com.myadvice.model.Course;
import com.myadvice.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Course existingCourse = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));

        // Update course details
        existingCourse.setCourseCode(updatedCourse.getCourseCode());
        existingCourse.setCourseName(updatedCourse.getCourseName());
        existingCourse.setCredits(updatedCourse.getCredits());
        existingCourse.setDescription(updatedCourse.getDescription());

        // Save updated course to the database
        return courseRepository.save(existingCourse);
    }

    // Delete an existing course
    public void deleteCourse(Long id){
        // Check if course exists, or throw an exception if not
        courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        // Delete course from the database
        courseRepository.deleteById(id);
    }

    public Course addPrerequisite(Long courseId, Long prerequisiteId){
        // Find the course and prerequisite by ID
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Course prerequisite = courseRepository.findById(prerequisiteId).orElseThrow(() -> new RuntimeException("Prerequisite requested is not found"));

        // Prevent adding a prerequisite to itself
        if(courseId.equals(prerequisiteId)){
            throw new RuntimeException("Prerequisite cannot be the same as the course");
        }

        // Prevent adding a prerequisite that already exists
        if(course.getPrerequisites().contains(prerequisite)){
            throw new RuntimeException("Prerequisite already exists");
        }

        // Add the prerequisite to the course's prerequisites list
        course.getPrerequisites().add(prerequisite);

        // Save the updated course back to the database
        return courseRepository.save(course);

    }

    public Course removePrerequisite(Long courseId, Long prerequisiteId){
        // Find the course and prerequisite by ID
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Course prerequisite = courseRepository.findById(prerequisiteId).orElseThrow(() -> new RuntimeException("Prerequisite requested is not found"));

        // Message to send if prerequisite does not exist
        if(!course.getPrerequisites().contains(prerequisite)){
            throw new RuntimeException("Prerequisite does not exist");
        }

        // Remove the prerequisite from the course's prerequisite list
        course.getPrerequisites().remove(prerequisite);

        // Save the updated course back to the database
        return courseRepository.save(course);
    }

    public List<Course> viewPrerequisites(Long courseId){
        // Find the course by ID
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        // Return the list of prerequisites for the course
        return course.getPrerequisites();
    }
}
