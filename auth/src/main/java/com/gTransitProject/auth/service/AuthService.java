package com.gTransitProject.auth.service;

import com.gTransitProject.auth.exception.resourceNotFoundException;
import com.gTransitProject.auth.model.Auth;
import com.gTransitProject.auth.repo.AuthRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class AuthService {

@Autowired
private AuthRepository authRepository;

@Autowired
private SupervisorClientService supervisorClientService;

@Autowired
private PasswordEncoder passwordEncoder;

public List<Auth> getAllAuths() {
    return authRepository.findAll();
}

public Auth saveAuth(Auth auth) {

    Boolean validSupervisor =
            supervisorClientService
                    .validateSupervisor(
                            auth.getSupervisorCode());

    System.out.println("VALID SUPERVISOR = " + validSupervisor);

    auth.setAuthorized(validSupervisor);

    auth.setSupervisorCode(
            passwordEncoder.encode(
                    auth.getSupervisorCode()));

    System.out.println("AUTHORIZED FIELD = " + auth.getAuthorized());

    return authRepository.save(auth);
}

public Auth getAuthById(Integer id) {
    return authRepository.findById(id)
            .orElseThrow(() ->
                    new resourceNotFoundException(
                            "Autorizacion no encontrada"));
}

public Auth findByCode(String code) {
    return authRepository.findByAuthCode(code)
            .orElseThrow(() ->
                    new resourceNotFoundException(
                            "Codigo de autorizacion no encontrado"));
}

public void deleteAuth(Integer id) {
    authRepository.deleteById(id);
}

public Auth updateAuth(
        Integer id,
        Auth newAuth) {

    Auth auth =
            getAuthById(id);

    auth.setRequestDescription(
            newAuth.getRequestDescription());

    auth.setOriginCity(
            newAuth.getOriginCity());

    auth.setDestinationCity(
            newAuth.getDestinationCity());

    auth.setAuthCode(
            newAuth.getAuthCode());

    auth.setSupervisorCode(
            passwordEncoder.encode(
                    newAuth.getSupervisorCode()));

    auth.setAuthorized(
            newAuth.getAuthorized());

    return authRepository.save(auth);
}

}
