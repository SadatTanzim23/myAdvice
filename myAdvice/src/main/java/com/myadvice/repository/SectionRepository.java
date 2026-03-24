package com.myadvice.repository;

import com.myadvice.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

//Provides built in database methods
public interface SectionRepository extends JpaRepository<Section, Long> {
}