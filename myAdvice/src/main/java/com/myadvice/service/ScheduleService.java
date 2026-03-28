package com.myadvice.service;

import com.myadvice.model.Schedule;
import com.myadvice.model.Course;
import com.myadvice.repository.ScheduleRepository;
import com.myadvice.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service //Marks class as service (business logic)
public class ScheduleService {

    //No need to create repositories manually
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    //Gets all schedules in the system
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    //Gets all schedules for a specific course using course ID
    public List<Schedule> getCourseSchedule(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        return scheduleRepository.findByCourse(course);
    }

    //Checks if two schedules conflict (same day, overlapping times)
    //Returns true if there is a conflict, false if no conflict
    public boolean checkConflict(Long scheduleId1, Long scheduleId2) {
        //Find both schedules
        Schedule schedule1 = scheduleRepository.findById(scheduleId1).orElseThrow(() -> new RuntimeException("Schedule not found"));
        Schedule schedule2 = scheduleRepository.findById(scheduleId2).orElseThrow(() -> new RuntimeException("Schedule not found"));
        //Check if they are on the same day
        if (!schedule1.getDayOfWeek().equals(schedule2.getDayOfWeek())) {
            return false; //Different days, no conflict
        }
        //Check if times overlap
        boolean overlap = schedule1.getStartTime().isBefore(schedule2.getEndTime()) &&
                schedule2.getStartTime().isBefore(schedule1.getEndTime());
        return overlap;
    }

    //Gets all schedules for a specific day of the week
    public List<Schedule> getScheduleByDay(String dayOfWeek) {
        return scheduleRepository.findByDayOfWeek(dayOfWeek);
    }
}