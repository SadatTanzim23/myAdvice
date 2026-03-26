package com.myadvice.controller;

import com.myadvice.model.Course;
import com.myadvice.model.Section;
import com.myadvice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //Marks it as a REST controller (handle incoming requests from UI)
@RequestMapping("/admin") // Set base URL for all endpoints to start with
@CrossOrigin(origins = "*") // Allows Swing frontend to call it
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Endpoint to add a new course (POST   /admin/courses/add)
    // Recieves a Course JSON object and returns the saved course
    @PostMapping("/courses/add")
    public Course addCourse(@RequestBody Course course){
        // @RequestBody converts the incoming JSON object into a Course object
        return adminService.addCourse(course);
    }

    // Endpoint to edit an existing course (PUT    /admin/courses/edit/{id})
    // Recieves a Course JSON object and updates the existing course
    @PutMapping("/courses/edit/{id}")
    public Course editCourse(@PathVariable("id") Long id, @RequestBody Course updatedCourse) {
        // @PathVariable gets the course ID from the URL
        // @RequestBody converts the incoming JSON object into a Course object
        return adminService.editCourse(id, updatedCourse);
    }

    // Endpoint to delete an existing course (DELETE /admin/courses/delete/{id})
    @DeleteMapping("/courses/delete/{id}")
    public void deleteCourse(@PathVariable("id") Long id){
        // @PathVariable gets the course ID from the URL
        // No return value, as the course is deleted directly
        adminService.deleteCourse(id);
    }

    // Endpoint to add a prerequisite to a course
    @PostMapping("/courses/{courseId}/prerequisites/add/{prerequisiteId}")
    public Course addPrerequisite(@PathVariable("courseId") Long courseId, @PathVariable("prerequisiteId") Long prerequisiteId){
        return adminService.addPrerequisite(courseId, prerequisiteId);
    }

    // Endpoint to remove a prerequisite from a course
    @DeleteMapping("/courses/{courseId}/prerequisites/remove/{prerequisiteId}")
    public Course removePrerequisite(@PathVariable("courseId") Long courseId, @PathVariable("prerequisiteId") Long prerequisiteId){
        return adminService.removePrerequisite(courseId, prerequisiteId);
    }

    // Endpoint to view all prerequisites for a course
    @GetMapping("/courses/{courseId}/prerequisites")
    public List<Course> viewPrerequisites(@PathVariable("courseId") Long courseId){
        return adminService.viewPrerequisites(courseId);
    }

    // Endpoint to view all courses
    @GetMapping("/courses")
    public List<Course> getAllCourses(){
        return adminService.getAllCourses();
    }

    // Section management endpoints
    @GetMapping("/sections")
    public List<Section> viewSections(){
        return adminService.viewSections();
    }

    @PostMapping("/sections/add")
    public Section addSection(@RequestBody Section section){
        return adminService.addSection(section.getCourse(), section.getFaculty(), section.getSectionNumber(), section.getCapacity(), section.getEnrolledCount(), section.getInstructorName(), section.getDayOfWeek());
    }

    @PutMapping("/sections/edit/{id}")
    public Section editSection(@PathVariable("id") Long id, @RequestBody Section section){
        return adminService.editSection(section.getId(), section);
    }

    @DeleteMapping("/sections/delete/{id}")
    public Section deleteSection(@PathVariable("id") Long id){
        return adminService.removeSection(id);
    }

}
