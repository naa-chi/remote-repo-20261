package com.gTransitProject.client.service;

import com.gTransitProject.client.exception.resourceNotFoundException;
import com.gTransitProject.client.model.client;
import com.gTransitProject.client.repo.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<client> getAllClients() {
        return clientRepository.findAll();
    }

    public client saveClient(client clientb) {
        return clientRepository.save(clientb);
    }

    public client getClientById(Integer id) {

        return clientRepository.findById(id)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Cliente no encontrado"));
    }

    public void deleteClient(Integer id) {
        clientRepository.deleteById(id);
    }

    public client findByAuthCode(
            String authCode){

        return clientRepository
                .findByAuthCode(authCode)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Codigo no encontrado"));
    }
}