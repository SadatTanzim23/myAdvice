package com.myadvice.repository;

import com.myadvice.model.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Provides built in database methods
public interface TranscriptRepository extends JpaRepository<Transcript,Long> {
    List<Transcript> findByStudentId(Long studentId);

}