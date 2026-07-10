package com.transit.managers.repository;

import com.transit.managers.model.ManagerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<ManagerModel, Long> {
    Optional<ManagerModel> findByCode(String code);
}