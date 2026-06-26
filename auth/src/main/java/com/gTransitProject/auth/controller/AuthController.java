package com.gTransitProject.auth.controller;

import com.gTransitProject.auth.model.Auth;
import com.gTransitProject.auth.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<List<Auth>> getAllAuths() {
        return ResponseEntity.ok(
                authService.getAllAuths());
    }

    @PostMapping
    public ResponseEntity<Auth> createAuth(
            @RequestBody Auth auth) {

        Auth newAuth = authService.saveAuth(auth);
        return ResponseEntity.status(201).body(newAuth);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auth> getAuthById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                authService.getAuthById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuth(
            @PathVariable Integer id) {

        authService.deleteAuth(id);

        return ResponseEntity.ok(
                "Autorizacion eliminada");
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<Boolean> validateAuth(
            @PathVariable String code) {

        Auth auth =
                authService.findByCode(code);

        return ResponseEntity.ok(
                auth.getAuthorized());
    }
    @PutMapping("/{id}")
public ResponseEntity<Auth>
updateAuth(
        @PathVariable Integer id,
        @RequestBody Auth auth){

    return ResponseEntity.ok(
            authService.updateAuth(
                    id,
                    auth));
}
}