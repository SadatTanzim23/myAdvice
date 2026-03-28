package com.myadvice.controller;

import com.myadvice.model.Schedule;
import com.myadvice.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController //Marks class as REST controller (handles incoming requests from UI)
@RequestMapping("/schedule") //All endpoints start with /schedule
public class ScheduleController {

    @Autowired //Spring automatically injects ScheduleService
    private ScheduleService scheduleService;

    //Gets all schedules in the system (GET /schedule/all)
    @GetMapping("/all")
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    //Gets all schedules for a specific course (GET /schedule/course/{courseId})
    @GetMapping("/course/{courseId}")
    public List<Schedule> getCourseSchedule(@PathVariable("courseId") Long courseId) {
        //@PathVariable gets courseId from URL and sends it to service
        return scheduleService.getCourseSchedule(courseId);
    }

    //Checks if two schedules conflict (GET /schedule/conflict/{scheduleId1}/{scheduleId2})
    //Returns true if conflict exists, false if no conflict
    @GetMapping("/conflict/{scheduleId1}/{scheduleId2}")
    public boolean checkConflict(
            @PathVariable("scheduleId1") Long scheduleId1,
            @PathVariable("scheduleId2") Long scheduleId2) {
        //@PathVariable gets both schedule IDs from URL and sends them to service
        return scheduleService.checkConflict(scheduleId1, scheduleId2);
    }

    //Gets all schedules for a specific day (GET /schedule/day/{dayOfWeek})
    @GetMapping("/day/{dayOfWeek}")
    public List<Schedule> getScheduleByDay(@PathVariable("dayOfWeek") String dayOfWeek) {
        //@PathVariable gets day from URL and sends it to service
        return scheduleService.getScheduleByDay(dayOfWeek);
    }
}
