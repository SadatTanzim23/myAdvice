package com.myadvice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.myadvice.model.Appointment;
import com.myadvice.model.Faculty;
import com.myadvice.model.Student;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    //exists True or False by where faculty field, and, dateTime field
    boolean existsByFacultyAndDateTime(Faculty faculty, LocalDateTime date);
    //Gets all appointments for a specific student
    List<Appointment> findByStudent(Student student);
    //Gets all appointments for a specific faculty member
    List<Appointment> findByFaculty(Faculty faculty);

    @Query("SELECT a.student.id, COUNT(a) FROM Appointment a GROUP BY a.student.id ORDER BY COUNT(a) DESC")
    List<Object[]> countAppointmentsByStudent();

    @Query("SELECT a.faculty.id, COUNT(a) FROM Appointment a GROUP BY a.faculty.id ORDER BY COUNT(a) DESC")
    List<Object[]> countAppointmentsByFaculty();

    long countByStatusIgnoreCase(String status);
}