package com.myadvice.controller;

import com.myadvice.model.Course;
import com.myadvice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController //Marks it as a REST controller (handle incoming requests from UI)
@RequestMapping("/admin") // Set base URL for all endpoints to start with
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

}
