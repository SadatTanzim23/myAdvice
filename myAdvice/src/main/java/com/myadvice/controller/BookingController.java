package com.myadvice.controller;

import com.myadvice.dto.BookingRequest;
import com.myadvice.model.Appointment;
import com.myadvice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController //Marks it as a REST controller (handle incoming requests from UI)
@RequestMapping("/bookings") //Set base URL for all endpoints to start with
public class BookingController {

    @Autowired //No need to create it manually (Spring does it)
    private BookingService bookingService;

    //Endpoint to book a new appointment (POST /bookings/book)
    //Send JSON object with those 4 parameters and returns new created appointment
    @PostMapping("/book")
    public Appointment bookAppointment(@RequestBody BookingRequest request){
        //@RequestBody tells Spring to convert the incoming JSON object into a BookingRequest object
        return bookingService.bookAppointment(
                request.getStudentId(),
                request.getFacultyId(),
                request.getDateTime(),
                request.getStatus());
    }

    //Endpoint to get all appointments for a specific student (GET /bookings/student/{studentId}
    //{studentId} is replaced with actual student ID
    @GetMapping("/student/{studentId}")
    public List<Appointment> getStudentAppointments(@PathVariable("studentId") Long studentId){
        //@PathVariable gets studentId from URL and sends it to service
        //Returns list of all appointments linked to that student
        return bookingService.getStudentAppointments(studentId);
    }

    //Endpoint to get all appointments a faculty member has (GET /bookings/faculty/{facultyId}
    //Shows if faculty member is busy or available to students
    @GetMapping("/faculty/{facultyId}")
    public List<Appointment> getFacultyAvailability(@PathVariable("facultyId") Long facultyId) {
        //@PathVariable gets facultyId from URL and sends it to service
        //Returns list of all appointments linked to that faculty member
        return bookingService.getFacultyAvailability(facultyId);
    }

    //Endpoint to cancel an existing appointment (DELETE /bookings/cancel/{appointmentId}
    //@DeleteMapping to remove/modify data
    @DeleteMapping("/cancel/{appointmentId}")
    public void cancelAppointment(@PathVariable("appointmentId") Long appointmentId){
        //@PathVariable gets appointmentID from URL and sends it to service
        //No return (only updates status to "cancelled")
        bookingService.cancelAppointment(appointmentId);
    }

    //Endpoint to reschedule existing appointment with new date and time (PUT /bookings/reschedule/{appointmentId}
    //Handles PUT requests to update existing data
    @PutMapping("/reschedule/{appointmentId}")
    public Appointment rescheduleAppointment(@PathVariable("appointmentId") Long appointmentId, //Gets appointment ID from URL
                                             @RequestBody LocalDateTime newDateTime){ //Receives new date and time from request body
        return bookingService.rescheduleAppointment(appointmentId, newDateTime); //Returns updated appointment
    }
}