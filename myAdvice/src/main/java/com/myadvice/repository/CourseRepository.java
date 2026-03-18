package com.myadvice.repository;

import com.myadvice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

//Provides built in database methods
public interface CourseRepository extends JpaRepository<Course, Long> {

}
