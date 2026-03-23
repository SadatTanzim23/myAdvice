package com.myadvice.repository;

import com.myadvice.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

//Provides built in database methods
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}