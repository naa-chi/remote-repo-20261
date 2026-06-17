package com.gTransitProject.client.repo;

import com.gTransitProject.client.model.client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository
        extends JpaRepository<client, Integer> {

    Optional<client> findByAuthCode(String authCode);

}