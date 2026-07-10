package com.transit.clients.repository;

import com.transit.clients.model.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientModel, Long> {
    Optional<ClientModel> findByCode(String code);
    Optional<ClientModel> findByEmail(String email);
}