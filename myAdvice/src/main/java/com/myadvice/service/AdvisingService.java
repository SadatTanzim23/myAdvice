package com.myadvice.service;

import com.myadvice.model.*;
import com.myadvice.repository.CourseRepository;
import com.myadvice.repository.StudentRepository;
import com.myadvice.repository.TranscriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.List;

@Service //Marks class as service (business logic)
public class AdvisingService {
    //No need to create any of these repositories manually
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TranscriptRepository transcriptRepository;

    @Autowired
    private CourseRepository courseRepository;

    //Gets and returns the student's program name with student ID
    public String getStudentProgram(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("student not found"));
        return student.getProgramName();
    }

    //Gets completed courses with student ID
    public List<Transcript> getCompletedCourses(Long studentId) {
        studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("student not found"));
        return transcriptRepository.findByStudentId(studentId);
    }

    //Gets all courses student still needs to complete for their program
    //Compares all courses with completed courses and returns difference
    public List<Course> getRemainingCourses(Long studentId) {
        studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("student not found"));
        List<Course> allCourses = courseRepository.findAll();
        List<Course> completedCourses = transcriptRepository.findByStudentId(studentId)
                .stream()
                .map(transcript -> transcript.getCourse())
                .collect(Collectors.toList());
        allCourses.removeAll(completedCourses);
        return allCourses;
    }


    //Checks if student has completed prerequisite for course
    //Returns true if prerequisite is completed or no prerequisite exists
    //Returns false if it exists but hasn't been completed yet
    public boolean checkPrerequisites(Long studentId, Long courseId) {
        studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("student not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("course not found"));

        List<Course> prerequisites = course.getPrerequisites();
        if (prerequisites == null || prerequisites.isEmpty()) {
            return true; //No prerequisite needed, student can take it
        }

        List<Course> completedCourses = transcriptRepository.findByStudentId(studentId)
                .stream()
                .map(transcript -> transcript.getCourse())
                .collect(Collectors.toList());

        // Student can take the course only if all prerequisites are completed.
        return completedCourses.containsAll(prerequisites);
    }

}
