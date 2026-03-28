package com.myadvice.controller;

import com.myadvice.model.Course;
import com.myadvice.model.Transcript;
import com.myadvice.service.AdvisingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController //Marks class as REST controller (handles incoming requests from UI)
@RequestMapping("/advising") //All endpoints start with /advising
public class AdvisingController {

    @Autowired //Spring automatically injects AdvisingService
    private AdvisingService advisingService;

    //Gets student program (GET /advising/program/{studentId})
    @GetMapping("/program/{studentId}")
    public String getStudentProgram(@PathVariable("studentId") Long studentId) {
        //@PathVariable gets studentId from URL and sends it to service
        return advisingService.getStudentProgram(studentId);
    }

    //Gets all completed courses for a student (GET /advising/completed/{studentId})
    @GetMapping("/completed/{studentId}")
    public List<Transcript> getCompletedCourses(@PathVariable("studentId") Long studentId) {
        //@PathVariable gets studentId from URL and sends it to service
        return advisingService.getCompletedCourses(studentId);
    }

    //Gets all remaining courses a student needs to complete (GET /advising/remaining/{studentId})
    @GetMapping("/remaining/{studentId}")
    public List<Course> getRemainingCourses(@PathVariable("studentId") Long studentId) {
        //@PathVariable gets studentId from URL and sends it to service
        return advisingService.getRemainingCourses(studentId);
    }

    //Checks if student meets prerequisites for a course (GET /advising/prerequisites/{studentId}/{courseId})
    //Returns true if student can take the course, false if not
    @GetMapping("/prerequisites/{studentId}/{courseId}")
    public boolean checkPrerequisites(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId) {
        //@PathVariable gets both IDs from URL and sends them to service
        return advisingService.checkPrerequisites(studentId, courseId);
    }
}
