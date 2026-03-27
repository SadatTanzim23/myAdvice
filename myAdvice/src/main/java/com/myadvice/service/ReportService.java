package com.myadvice.service;

import com.myadvice.dto.AppointmentCountsDto;
import com.myadvice.dto.EntityAppointmentCountDto;
import com.myadvice.model.Faculty;
import com.myadvice.model.Student;
import com.myadvice.repository.AppointmentRepository;
import com.myadvice.repository.FacultyRepository;
import com.myadvice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private FacultyRepository facultyRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	public List<Faculty> getAllFaculty() {
		return facultyRepository.findAll();
	}

	public List<EntityAppointmentCountDto> getStudentsWithMostAppointments() {
		List<Object[]> counts = appointmentRepository.countAppointmentsByStudent();
		if (counts.isEmpty()) {
			return List.of();
		}

		long maxCount = ((Number) counts.get(0)[1]).longValue();
		List<Long> studentIds = new ArrayList<>();
		for (Object[] row : counts) {
			long currentCount = ((Number) row[1]).longValue();
			if (currentCount != maxCount) {
				break;
			}
			studentIds.add(((Number) row[0]).longValue());
		}

		Map<Long, Student> studentsById = new LinkedHashMap<>();
		for (Student student : studentRepository.findAllById(studentIds)) {
			studentsById.put(student.getId(), student);
		}

		List<EntityAppointmentCountDto> result = new ArrayList<>();
		for (Long studentId : studentIds) {
			Student student = studentsById.get(studentId);
			if (student != null) {
				result.add(new EntityAppointmentCountDto(student.getId(), student.getFirstName(), student.getLastName(), maxCount));
			}
		}
		return result;
	}

	public List<EntityAppointmentCountDto> getFacultyWithMostAppointments() {
		List<Object[]> counts = appointmentRepository.countAppointmentsByFaculty();
		if (counts.isEmpty()) {
			return List.of();
		}

		long maxCount = ((Number) counts.get(0)[1]).longValue();
		List<Long> facultyIds = new ArrayList<>();
		for (Object[] row : counts) {
			long currentCount = ((Number) row[1]).longValue();
			if (currentCount != maxCount) {
				break;
			}
			facultyIds.add(((Number) row[0]).longValue());
		}

		Map<Long, Faculty> facultyById = new LinkedHashMap<>();
		for (Faculty faculty : facultyRepository.findAllById(facultyIds)) {
			facultyById.put(faculty.getId(), faculty);
		}

		List<EntityAppointmentCountDto> result = new ArrayList<>();
		for (Long facultyId : facultyIds) {
			Faculty faculty = facultyById.get(facultyId);
			if (faculty != null) {
				result.add(new EntityAppointmentCountDto(faculty.getId(), faculty.getFirstName(), faculty.getLastName(), maxCount));
			}
		}
		return result;
	}

	public AppointmentCountsDto getAppointmentCounts() {
		long totalAppointments = appointmentRepository.count();
		long cancelledAppointments = appointmentRepository.countByStatusIgnoreCase("cancelled");
		long activeAppointments = Math.max(totalAppointments - cancelledAppointments, 0L);

		return new AppointmentCountsDto(totalAppointments, activeAppointments, cancelledAppointments);
	}
}
