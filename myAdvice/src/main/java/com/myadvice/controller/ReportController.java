package com.myadvice.controller;

import com.myadvice.dto.AppointmentCountsDto;
import com.myadvice.dto.EntityAppointmentCountDto;
import com.myadvice.model.Faculty;
import com.myadvice.model.Student;
import com.myadvice.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
	public List<EntityAppointmentCountDto> getStudentsWithMostAppointments() {
		return reportService.getStudentsWithMostAppointments();
	}

	@GetMapping("/faculty/most-appointments")
	public List<EntityAppointmentCountDto> getFacultyWithMostAppointments() {
		return reportService.getFacultyWithMostAppointments();
	}

	@GetMapping("/appointments/counts")
	public AppointmentCountsDto getAppointmentCounts() {
		return reportService.getAppointmentCounts();
	}
}
