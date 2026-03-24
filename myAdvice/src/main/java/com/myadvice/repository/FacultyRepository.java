package com.myadvice.repository;
import com.myadvice.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

//Provides built in database methods
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

}