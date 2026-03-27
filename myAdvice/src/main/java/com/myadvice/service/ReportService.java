package com.myadvice.service;

import com.myadvice.model.Faculty;
import com.myadvice.model.Student;
import com.myadvice.repository.AppointmentRepository;
import com.myadvice.repository.FacultyRepository;
import com.myadvice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<Map<String, Object>> getStudentsWithMostAppointments() {
        List<Object[]> counts = appointmentRepository.countAppointmentsByStudent();
        if (counts.isEmpty()) return List.of();

        long maxCount = ((Number) counts.get(0)[1]).longValue();
        List<Long> studentIds = new ArrayList<>();
        for (Object[] row : counts) {
            long currentCount = ((Number) row[1]).longValue();
            if (currentCount != maxCount) break;
            studentIds.add(((Number) row[0]).longValue());
        }

        Map<Long, Student> studentsById = new LinkedHashMap<>();
        for (Student s : studentRepository.findAllById(studentIds)) {
            studentsById.put(s.getId(), s);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Long id : studentIds) {
            Student s = studentsById.get(id);
            if (s != null) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("entityId", s.getId());
                row.put("firstName", s.getFirstName());
                row.put("lastName", s.getLastName());
                row.put("appointmentCount", maxCount);
                result.add(row);
            }
        }
        return result;
    }

    public List<Map<String, Object>> getFacultyWithMostAppointments() {
        List<Object[]> counts = appointmentRepository.countAppointmentsByFaculty();
        if (counts.isEmpty()) return List.of();

        long maxCount = ((Number) counts.get(0)[1]).longValue();
        List<Long> facultyIds = new ArrayList<>();
        for (Object[] row : counts) {
            long currentCount = ((Number) row[1]).longValue();
            if (currentCount != maxCount) break;
            facultyIds.add(((Number) row[0]).longValue());
        }

        Map<Long, Faculty> facultyById = new LinkedHashMap<>();
        for (Faculty f : facultyRepository.findAllById(facultyIds)) {
            facultyById.put(f.getId(), f);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Long id : facultyIds) {
            Faculty f = facultyById.get(id);
            if (f != null) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("entityId", f.getId());
                row.put("firstName", f.getFirstName());
                row.put("lastName", f.getLastName());
                row.put("appointmentCount", maxCount);
                result.add(row);
            }
        }
        return result;
    }

    public Map<String, Long> getAppointmentCounts() {
        long totalAppointments = appointmentRepository.count();
        long cancelledAppointments = appointmentRepository.countByStatusIgnoreCase("cancelled");
        long activeAppointments = Math.max(totalAppointments - cancelledAppointments, 0L);

        Map<String, Long> result = new LinkedHashMap<>();
        result.put("totalAppointments", totalAppointments);
        result.put("activeAppointments", activeAppointments);
        result.put("cancelledAppointments", cancelledAppointments);
        return result;
    }
}