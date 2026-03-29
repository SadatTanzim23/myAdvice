package com.myadvice.controller;

import com.myadvice.dto.EnrollmentRequest;
import com.myadvice.model.Course;
import com.myadvice.model.Enrollment;
import com.myadvice.model.Faculty;
import com.myadvice.model.Section;
import com.myadvice.model.Student;
import com.myadvice.model.Transcript;
import com.myadvice.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
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
    public void addPrerequisite(@PathVariable("courseId") Long courseId, @PathVariable("prerequisiteId") Long prerequisiteId){
        adminService.addPrerequisite(courseId, prerequisiteId);
    }

    // Endpoint to remove a prerequisite from a course
    @DeleteMapping("/courses/{courseId}/prerequisites/remove/{prerequisiteId}")
    public void removePrerequisite(@PathVariable("courseId") Long courseId, @PathVariable("prerequisiteId") Long prerequisiteId){
        adminService.removePrerequisite(courseId, prerequisiteId);
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
    @GetMapping("/faculty")
    public List<Faculty> viewFaculty(){
        return adminService.viewFaculty();
    }

    @PostMapping("/faculty/add")
    public Faculty addFaculty(@RequestBody Faculty faculty){
        try {
            return adminService.addFaculty(faculty);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/faculty/edit/{id}")
    public Faculty editFaculty(@PathVariable("id") Long id, @RequestBody Faculty faculty){
        try {
            return adminService.editFaculty(id, faculty);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/faculty/delete/{id}")
    public void deleteFaculty(@PathVariable("id") Long id){
        try {
            adminService.removeFaculty(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/students")
    public List<Student> viewStudents(){
        return adminService.viewStudents();
    }

    @PostMapping("/students/add")
    public Student addStudent(@RequestBody Student student){
        try {
            return adminService.addStudent(student);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/students/edit/{id}")
    public Student editStudent(@PathVariable("id") Long id, @RequestBody Student student){
        try {
            return adminService.editStudent(id, student);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/students/delete/{id}")
    public void deleteStudent(@PathVariable("id") Long id){
        try {
            adminService.removeStudent(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    // Transcript management endpoints
    @GetMapping("/transcripts")
    public List<Transcript> viewAllTranscripts(){
        return adminService.viewAllTranscripts();
    }

    @GetMapping("/transcripts/student/{studentId}")
    public List<Transcript> viewTranscriptByStudent(@PathVariable("studentId") Long studentId){
        return adminService.viewTranscript(studentId);
    }

    @PostMapping("/transcripts/add")
    public Transcript addTranscript(@RequestBody Transcript transcript){
        try {
            return adminService.addTranscript(transcript.getStudent(), transcript.getCourse(), transcript.getGrade(), transcript.getTerm());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/transcripts/edit/{id}")
    public Transcript editTranscript(@PathVariable("id") Long id, @RequestBody Transcript transcript){
        try {
            return adminService.editTranscript(id, transcript);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/transcripts/delete/{id}")
    public void deleteTranscript(@PathVariable("id") Long id){
        try {
            adminService.removeTranscript(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/sections")
    public List<Section> viewSections(){
        return adminService.viewSections();
    }

    @PostMapping("/sections/add")
    public Section addSection(@RequestBody Section section){
        try {
            return adminService.addSection(
                    section.getCourse(),
                    section.getFaculty(),
                    section.getSectionNumber(),
                    section.getCapacity(),
                    section.getEnrolledCount(),
                    section.getInstructorName(),
                    section.getDayOfWeek(),
                    section.getLabDayOfWeek(),
                    section.getLabTime(),
                    section.getRoom()
            );
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/sections/edit/{id}")
    public Section editSection(@PathVariable("id") Long id, @RequestBody Section section){
        try {
            return adminService.editSection(id, section);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/sections/delete/{id}")
    public Section deleteSection(@PathVariable("id") Long id){
        return adminService.removeSection(id);
    }

    @PostMapping("/timetable/enroll")
    public Enrollment enrollStudentInSectionAndLab(@RequestBody EnrollmentRequest request) {
        try {
            return adminService.enrollStudentInSectionAndLab(
                    request.getStudentId(),
                    request.getCourseId(),
                    request.getSectionId(),
                    request.getLabDayOfWeek(),
                    request.getLabTime()
            );
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/timetable/student/{studentId}/courses")
    public List<Enrollment> viewStudentCourses(@PathVariable("studentId") Long studentId) {
        try {
            return adminService.viewStudentCourses(studentId);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/timetable/student/{studentId}/courses/{courseId}")
    public Enrollment deleteStudentCourse(@PathVariable("studentId") Long studentId,
                                          @PathVariable("courseId") Long courseId) {
        try {
            return adminService.removeStudentCourse(studentId, courseId);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
