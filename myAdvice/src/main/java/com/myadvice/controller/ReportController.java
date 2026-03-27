package com.myadvice.controller;

import com.myadvice.model.Faculty;
import com.myadvice.model.Student;
import com.myadvice.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return reportService.getAllStudents();
    }

    @GetMapping("/faculty")
    public List<Faculty> getAllFaculty() {
        return reportService.getAllFaculty();
    }

    @GetMapping("/students/most-appointments")
    public List<Map<String, Object>> getStudentsWithMostAppointments() {
        return reportService.getStudentsWithMostAppointments();
    }

    @GetMapping("/faculty/most-appointments")
    public List<Map<String, Object>> getFacultyWithMostAppointments() {
        return reportService.getFacultyWithMostAppointments();
    }

    @GetMapping("/appointments/counts")
    public Map<String, Long> getAppointmentCounts() {
        return reportService.getAppointmentCounts();
    }
}