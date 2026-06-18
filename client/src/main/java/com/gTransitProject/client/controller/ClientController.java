package com.gTransitProject.client.controller;

import com.gTransitProject.client.model.client;
import com.gTransitProject.client.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<client>>
    getAllClients(){

        return ResponseEntity.ok(
                clientService.getAllClients());
    }

    @PostMapping
    public ResponseEntity<client>
    createClient(
            @RequestBody client clientb){

        return ResponseEntity.ok(
                clientService.saveClient(clientb));
    }

    @GetMapping("/{id}")
    public ResponseEntity<client>
    getClientById(
            @PathVariable Integer id){

        return ResponseEntity.ok(
                clientService.getClientById(id));
    }
        @PutMapping("/{id}")
        public ResponseEntity<client>
        updateClient(
                @PathVariable Integer id,
                @RequestBody client clientb){

        return ResponseEntity.ok(
                clientService.updateClient(
                        id,
                        clientb));
}

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteClient(
            @PathVariable Integer id){

        clientService.deleteClient(id);

        return ResponseEntity.ok(
                "Cliente eliminado");
    }
@GetMapping("/validate/{code}")
public ResponseEntity<String>
validateClient(
        @PathVariable String code){

    client clientb =
            clientService
                    .findByAuthCode(code);

    return ResponseEntity.ok(
            clientb.getStatus());
}
}