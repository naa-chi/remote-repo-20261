package com.gTransitProject.supervisor.repo;

import com.gTransitProject.supervisor.model.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor, Integer> {

    Optional<Supervisor> findBySupervisorCode(String supervisorCode);

}