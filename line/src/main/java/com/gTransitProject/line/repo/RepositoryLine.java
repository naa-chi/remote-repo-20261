package com.gTransitProject.line.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gTransitProject.line.model.Line;

@Repository
public interface RepositoryLine extends JpaRepository<Line, Integer>{

    Optional<Line> findByLineNumber(Integer lineNumber);

}