package com.gTransitProject.ticket.repo;
import com.gTransitProject.ticket.model.ticketModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ticketRepo extends JpaRepository<ticketModel, Integer> {

}
