package com.myadvice.repository;

import com.myadvice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

//Provides built in database methods
public interface StudentRepository extends JpaRepository<Student, Long> {

}