package com.transit.tickets.repository;

import com.transit.tickets.model.TicketModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketsRepository extends JpaRepository<TicketModel, Long> {
    Optional<TicketModel> findByCode(String code);
    List<TicketModel> findByCityCodeOrigin(String cityCodeOrigin);
    List<TicketModel> findByCityCodeDestination(String cityCodeDestination);
}