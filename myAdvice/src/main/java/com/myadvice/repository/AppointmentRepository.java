package com.myadvice.repository;

import com.myadvice.model.Appointment;
import com.myadvice.model.Faculty;
import com.myadvice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    //exists = True or False, By = where, Faculty field, and, dateTime field
    boolean existsByFacultyAndDateTime(Faculty faculty, LocalDateTime date);
    //Gets all appointments for a specific student
    List<Appointment> findByStudent(Student student);
    //Gets all appointments for a specific faculty member
    List<Appointment> findByFaculty(Faculty faculty);
}
