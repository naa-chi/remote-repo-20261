package com.gTransitProject.auth.controller;

import com.gTransitProject.auth.model.Auth;
import com.gTransitProject.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Auth",
        description = "Gestion de autorizaciones del sistema"
)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Obtener todas las autorizaciones")
    @GetMapping
    public ResponseEntity<List<Auth>> getAllAuths() {
        return ResponseEntity.ok(
                authService.getAllAuths());
    }

    @Operation(summary = "Crear una nueva autorizacion")
    @PostMapping
    public ResponseEntity<Auth> createAuth(
            @RequestBody Auth auth) {

        return ResponseEntity.ok(
                authService.saveAuth(auth));
    }

    @Operation(summary = "Buscar autorizacion por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Auth> getAuthById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                authService.getAuthById(id));
    }

    @Operation(summary = "Eliminar autorizacion")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuth(
            @PathVariable Integer id) {

        authService.deleteAuth(id);

        return ResponseEntity.ok(
                "Autorizacion eliminada");
    }

    @Operation(summary = "Validar codigo de autorizacion")
    @GetMapping("/validate/{code}")
    public ResponseEntity<Boolean> validateAuth(
            @PathVariable String code) {

        Auth auth =
                authService.findByCode(code);

        return ResponseEntity.ok(
                auth.getAuthorized());
    }

    @Operation(summary = "Actualizar autorizacion")
    @PutMapping("/{id}")
    public ResponseEntity<Auth> updateAuth(
            @PathVariable Integer id,
            @RequestBody Auth auth) {

        return ResponseEntity.ok(
                authService.updateAuth(
                        id,
                        auth));
    }
}