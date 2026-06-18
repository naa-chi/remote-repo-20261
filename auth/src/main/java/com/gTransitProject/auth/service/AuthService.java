package com.gTransitProject.auth.service;

import com.gTransitProject.auth.exception.resourceNotFoundException;
import com.gTransitProject.auth.model.Auth;
import com.gTransitProject.auth.repo.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gTransitProject.auth.service.SupervisorClientService;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    public List<Auth> getAllAuths() {
        return authRepository.findAll();
    }

    @Autowired
private SupervisorClientService supervisorClientService;

public Auth saveAuth(Auth auth) {

    Boolean validSupervisor =
            supervisorClientService
                    .validateSupervisor(
                            auth.getSupervisorCode());

    System.out.println("VALID SUPERVISOR = " + validSupervisor);

    auth.setAuthorized(validSupervisor);

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
}