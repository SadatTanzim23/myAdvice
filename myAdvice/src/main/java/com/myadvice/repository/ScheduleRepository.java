package com.myadvice.repository;

import com.myadvice.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Provides built in database methods
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCourseCode(Long courseCode);
}