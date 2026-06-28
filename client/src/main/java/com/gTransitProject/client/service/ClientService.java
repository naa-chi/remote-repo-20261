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

    @Autowired
    private AuthClientService authClientService;

    public List<client> getAllClients() {
        return clientRepository.findAll();
    }

    public client saveClient(client clientb) {

        Boolean validAuth =
                authClientService
                        .validateAuth(
                                clientb.getAuthCode());

        if (Boolean.TRUE.equals(validAuth)) {
            clientb.setStatus("APROBADO");
        } else {
            clientb.setStatus("RECHAZADO");
        }

        return clientRepository.save(clientb);
    }

    public client getClientById(Integer id) {

        return clientRepository.findById(id)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Cliente no encontrado"));
    }

    public client updateClient(
            Integer id,
            client newClient) {

        client clientb =
                getClientById(id);

        clientb.setClientName(
                newClient.getClientName());

        clientb.setRequestCity(
                newClient.getRequestCity());

        clientb.setProviderCity(
                newClient.getProviderCity());

        clientb.setRequestedResource(
                newClient.getRequestedResource());

        clientb.setOfferedReward(
                newClient.getOfferedReward());

        clientb.setAuthCode(
                newClient.getAuthCode());

        clientb.setStatus(
                newClient.getStatus());

        return clientRepository.save(clientb);
    }

    public void deleteClient(Integer id) {
        clientRepository.deleteById(id);
    }

    public client findByAuthCode(
            String authCode) {

        return clientRepository
                .findByAuthCode(authCode)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Codigo no encontrado"));
    }
}