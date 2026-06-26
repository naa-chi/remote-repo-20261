package com.gTransitProject.auth.service;

import com.gTransitProject.auth.exception.resourceNotFoundException;
import com.gTransitProject.auth.model.Auth;
import com.gTransitProject.auth.repo.AuthRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auth Service Tests")
class AuthServiceTest {

@Mock
private AuthRepository authRepository;

@Mock
private SupervisorClientService supervisorClientService;

@Mock
private PasswordEncoder passwordEncoder;

@InjectMocks
private AuthService authService;

@Test
void shouldFindAuthById() {

    Auth auth = new Auth();
    auth.setAuthId(1);

    when(authRepository.findById(1))
            .thenReturn(Optional.of(auth));

    Auth result = authService.getAuthById(1);

    assertNotNull(result);
    assertEquals(1, result.getAuthId());
}

@Test
void shouldThrowWhenAuthNotFound() {

    when(authRepository.findById(999))
            .thenReturn(Optional.empty());

    assertThrows(
            resourceNotFoundException.class,
            () -> authService.getAuthById(999)
    );
}

@Test
void shouldFindAuthByCode() {

    Auth auth = new Auth();
    auth.setAuthCode("AUTH001");

    when(authRepository.findByAuthCode("AUTH001"))
            .thenReturn(Optional.of(auth));

    Auth result = authService.findByCode("AUTH001");

    assertEquals("AUTH001", result.getAuthCode());
}

@Test
void shouldSaveAuthorizedAuth() {

    Auth auth = new Auth();
    auth.setSupervisorCode("SUP-STG-01");

    when(supervisorClientService
            .validateSupervisor("SUP-STG-01"))
            .thenReturn(true);

    when(passwordEncoder
            .encode("SUP-STG-01"))
            .thenReturn("HASHED_SUP-STG-01");

    when(authRepository.save(any(Auth.class)))
            .thenAnswer(invocation ->
                    invocation.getArgument(0));

    Auth result = authService.saveAuth(auth);

    assertTrue(result.getAuthorized());

    assertEquals(
            "HASHED_SUP-STG-01",
            result.getSupervisorCode()
    );
}

@Test
void shouldSaveUnauthorizedAuth() {

    Auth auth = new Auth();
    auth.setSupervisorCode("SUP-FAKE");

    when(supervisorClientService
            .validateSupervisor("SUP-FAKE"))
            .thenReturn(false);

    when(passwordEncoder
            .encode("SUP-FAKE"))
            .thenReturn("HASHED_SUP-FAKE");

    when(authRepository.save(any(Auth.class)))
            .thenAnswer(invocation ->
                    invocation.getArgument(0));

    Auth result = authService.saveAuth(auth);

    assertFalse(result.getAuthorized());

    assertEquals(
            "HASHED_SUP-FAKE",
            result.getSupervisorCode()
    );
}


}
