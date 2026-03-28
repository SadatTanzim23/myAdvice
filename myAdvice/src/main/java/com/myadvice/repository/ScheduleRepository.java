package com.myadvice.repository;

import com.myadvice.model.Course;
import com.myadvice.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Provides built in database methods
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCourseCourseCode(String courseCode);
    //Custom methods for ScheduleService
    List<Schedule> findByCourse(Course course);
    List<Schedule> findByDayOfWeek(String dayOfWeek);
}