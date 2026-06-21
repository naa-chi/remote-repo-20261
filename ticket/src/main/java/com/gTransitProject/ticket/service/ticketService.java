package com.gTransitProject.ticket.service;

import com.gTransitProject.ticket.model.ticketModel;
import com.gTransitProject.ticket.repo.ticketRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ticketService {
    private final ticketRepo repo;

    public List<ticketModel> getAll() {
        log.info("Fetching all tickets");
        return repo.findAll();
    }

    public ticketModel getById(Integer id) {
        log.info("Fetching ticket with id: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to find ticket with id: {}", id);
                    return new RuntimeException("Ticket not found with id: " + id);
                });
    }

    public ticketModel getByCode(String code) {
        log.info("Fetching ticket by code: {}", code);
        return repo.findAll().stream()
                .filter(ticket -> ticket.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Failed to find ticket with code: {}", code);
                    return new RuntimeException("Ticket not found with code: " + code);
                });
    }

    public ticketModel getCityCodeOrigin(String cityCodeOrigin) {
        log.info("Fetching ticket by origin city code: {}", cityCodeOrigin);
        return repo.findAll().stream()
                .filter(ticket -> ticket.getCityCodeOrigin().equals(cityCodeOrigin))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Failed to find ticket with origin city code: {}", cityCodeOrigin);
                    return new RuntimeException("Ticket not found with origin city code: " + cityCodeOrigin);
                });
    }

    public ticketModel getCityCodeDestination(String cityCodeDestination) {
        log.info("Fetching ticket by destination city code: {}", cityCodeDestination);
        return repo.findAll().stream()
                .filter(ticket -> ticket.getCityCodeDestination().equals(cityCodeDestination))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Failed to find ticket with destination city code: {}", cityCodeDestination);
                    return new RuntimeException("Ticket not found with destination city code: " + cityCodeDestination);
                });
    }

    public ticketModel getByClientId(Integer clientId) {
        log.info("Fetching ticket by client ID: {}", clientId);
        return repo.findAll().stream()
                .filter(ticket -> ticket.getClientId().equals(clientId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Failed to find ticket with client ID: {}", clientId);
                    return new RuntimeException("Ticket not found with client ID: " + clientId);
                });
    }

    public ticketModel getByTrainCode(String trainCode) {
        log.info("Fetching ticket by train code: {}", trainCode);
        return repo.findAll().stream()
                .filter(ticket -> ticket.getTrainCode().equals(trainCode))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Failed to find ticket with train code: {}", trainCode);
                    return new RuntimeException("Ticket not found with train code: " + trainCode);
                });
    }

    public ticketModel create(ticketModel model) {
        log.info("Creating new ticket: {}", model);
        return repo.save(model);
    }

    public void delete(Integer id) {
        log.info("Deleting ticket with id: {}", id);
        repo.deleteById(id);
    }

    public ticketModel update(Integer id, ticketModel model) {
        log.info("Updating ticket with id: {}", id);
        return repo.findById(id)
                .map(existing -> {
                    existing.setCode(model.getCode());
                    existing.setCityCodeOrigin(model.getCityCodeOrigin());
                    existing.setCityCodeDestination(model.getCityCodeDestination());
                    existing.setTrainCode(model.getTrainCode());
                    existing.setClientId(model.getClientId());
                    existing.setDepartureTime(model.getDepartureTime());
                    existing.setArrivalTime(model.getArrivalTime());
                    return repo.save(existing);
                })
                .orElseThrow(() -> {
                    log.error("Failed to find ticket with id: {} for update", id);
                    return new RuntimeException("Ticket not found with id: " + id);
                });
    }
}
