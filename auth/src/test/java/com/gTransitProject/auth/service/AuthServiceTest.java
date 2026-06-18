package com.gTransitProject.auth.service;

import com.gTransitProject.auth.model.Auth;
import com.gTransitProject.auth.repo.AuthRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private SupervisorClientService supervisorClientService;

    @InjectMocks
    private AuthService authService;

    @Test
    void saveAuthShouldAuthorizeWhenSupervisorExists() {

        Auth auth = new Auth();
        auth.setSupervisorCode("SUP-STG-01");

        when(supervisorClientService
                .validateSupervisor("SUP-STG-01"))
                .thenReturn(true);

        when(authRepository.save(any(Auth.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Auth result = authService.saveAuth(auth);

        assertTrue(result.getAuthorized());

        verify(authRepository, times(1))
                .save(any(Auth.class));
    }
}