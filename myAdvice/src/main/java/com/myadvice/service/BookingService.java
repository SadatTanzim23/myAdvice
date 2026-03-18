package com.myadvice.service;

import com.myadvice.model.Appointment;
import com.myadvice.model.Faculty;
import com.myadvice.model.Student;
import com.myadvice.repository.AppointmentRepository;
import com.myadvice.repository.FacultyRepository;
import com.myadvice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import java.time.LocalDateTime;

@Service //Marks class as a service (business logic)
public class BookingService {

    @Autowired //No need to create AppointmentRepository manually (Spring does it auto)
    private AppointmentRepository appointmentRepository;

    @Autowired //No need to create StudentRepository manually
    private StudentRepository studentRepository;

    @Autowired //No need to create FacultyRepository manually
    private FacultyRepository facultyRepository;

    //Creates new appointment between a student and faculty member
    public Appointment bookAppointment(Long studentId, Long facultyId, LocalDateTime dateTime, String status) {
        //Look up student by ID (error if not found)
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        //Look up faculty by ID (error if not found)
        Faculty faculty = facultyRepository.findById(facultyId).orElseThrow(() -> new RuntimeException("Faculty not found"));
        //Check if faculty already has an appointment at exact date and time
        if (appointmentRepository.existsByFacultyAndDateTime(faculty, dateTime)) {
            throw new RuntimeException("Appointment already exists");
        }
        //Create new appointment, save it and return it
        return  appointmentRepository.save(new Appointment(student, faculty, dateTime, status));

    }

    //Return all appointments belonging to a specific student (upcoming and past appointments)
    public List<Appointment> getStudentAppointments(Long studentId) {
        //Look up student by ID (error if not found)
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        //Get and return all appointments linked to this student
        return appointmentRepository.findByStudent(student);
    }

    //Return all appointments a faculty member has (if faculty member is available or busy)
    public List<Appointment> getFacultyAvailability(Long facultyId) {
        //Look up faculty by ID (error if not found)
        Faculty faculty = facultyRepository.findById(facultyId).orElseThrow(() -> new RuntimeException("Faculty not found"));
        //Get and return all appointments linked to faculty member
        return appointmentRepository.findByFaculty(faculty);
    }

    //Cancels an existing appointment by changing its status to "cancelled"
    public void cancelAppointment(Long appointmentId) {
        //Look up appointment by ID (error if not found)
        Appointment appointment =  appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment not found"));
        //Update status to cancelled
        appointment.setStatus("cancelled");
        //Save updated appointment back to database
        appointmentRepository.save(appointment);
    }

}
