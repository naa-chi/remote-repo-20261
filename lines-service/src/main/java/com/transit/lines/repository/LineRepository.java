package com.transit.lines.repository;

import com.transit.lines.model.LineModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LineRepository extends JpaRepository<LineModel, Long> {
    Optional<LineModel> findByLineCode(Integer lineCode);
}