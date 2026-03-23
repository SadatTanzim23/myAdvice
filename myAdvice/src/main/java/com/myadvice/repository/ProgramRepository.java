package com.myadvice.repository;

import com.myadvice.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

//Provides built in database methods
public interface ProgramRepository extends JpaRepository<Program, Long> {

}