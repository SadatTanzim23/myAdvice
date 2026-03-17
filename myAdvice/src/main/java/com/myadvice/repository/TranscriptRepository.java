package com.myadvice.repository;

import com.myadvice.model.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranscriptRepository extends JpaRepository<Transcript,Long> {
}
