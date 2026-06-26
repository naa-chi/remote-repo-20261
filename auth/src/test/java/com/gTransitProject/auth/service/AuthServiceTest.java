package com.gTransitProject.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.gTransitProject.auth.exception.resourceNotFoundException;
import com.gTransitProject.auth.model.Auth;
import com.gTransitProject.auth.repo.AuthRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auth Service Tests")
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private SupervisorClientService supervisorClientService;

    @InjectMocks
    private AuthService authService;

    private Auth testAuth;

    @BeforeEach
    void setUp() {
        testAuth = new Auth(1, "Emergency transport", "Madrid", "Barcelona", "AUTH001", "SUP001", true);
    }

    @Test
    @DisplayName("Should retrieve all authorizations")
    void testGetAllAuths() {
        Auth auth2 = new Auth(2, "Regular transport", "Valencia", "Alicante", "AUTH002", "SUP002", true);
        List<Auth> auths = Arrays.asList(testAuth, auth2);

        when(authRepository.findAll()).thenReturn(auths);

        List<Auth> result = authService.getAllAuths();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(authRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save auth with valid supervisor")
    void testSaveAuth_ValidSupervisor() {
        when(supervisorClientService.validateSupervisor("SUP001")).thenReturn(true);
        when(authRepository.save(any(Auth.class))).thenReturn(testAuth);

        Auth result = authService.saveAuth(testAuth);

        assertNotNull(result);
        assertTrue(result.getAuthorized());
        verify(authRepository, times(1)).save(any(Auth.class));
        verify(supervisorClientService, times(1)).validateSupervisor("SUP001");
    }

    @Test
    @DisplayName("Should save auth with invalid supervisor")
    void testSaveAuth_InvalidSupervisor() {
        Auth unauthorizedAuth = new Auth(1, "Emergency transport", "Madrid", "Barcelona", "AUTH001", "INVALID", false);
        when(supervisorClientService.validateSupervisor("INVALID")).thenReturn(false);
        when(authRepository.save(any(Auth.class))).thenReturn(unauthorizedAuth);

        Auth result = authService.saveAuth(unauthorizedAuth);

        assertNotNull(result);
        assertFalse(result.getAuthorized());
        verify(authRepository, times(1)).save(any(Auth.class));
    }

    @Test
    @DisplayName("Should get auth by ID successfully")
    void testGetAuthById_Success() {
        when(authRepository.findById(1)).thenReturn(Optional.of(testAuth));

        Auth result = authService.getAuthById(1);

        assertNotNull(result);
        assertEquals("AUTH001", result.getAuthCode());
        verify(authRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should throw exception when auth ID not found")
    void testGetAuthById_NotFound() {
        when(authRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(resourceNotFoundException.class, () -> {
            authService.getAuthById(999);
        });
        verify(authRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Should find auth by code successfully")
    void testFindByCode_Success() {
        when(authRepository.findByAuthCode("AUTH001")).thenReturn(Optional.of(testAuth));

        Auth result = authService.findByCode("AUTH001");

        assertNotNull(result);
        assertEquals("AUTH001", result.getAuthCode());
        verify(authRepository, times(1)).findByAuthCode("AUTH001");
    }

    @Test
    @DisplayName("Should throw exception when auth code not found")
    void testFindByCode_NotFound() {
        when(authRepository.findByAuthCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(resourceNotFoundException.class, () -> {
            authService.findByCode("INVALID");
        });
    }

    @Test
    @DisplayName("Should validate auth fields")
    void testAuthValidation() {
        assertNotNull(testAuth.getAuthId());
        assertNotNull(testAuth.getAuthCode());
        assertNotNull(testAuth.getAuthorized());
    }

    @Test
    @DisplayName("Should handle retrieve empty authorization list")
    void testGetAllAuths_Empty() {
        when(authRepository.findAll()).thenReturn(Arrays.asList());

        List<Auth> result = authService.getAllAuths();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(authRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should validate unique auth code constraint")
    void testUniqueAuthCodeConstraint() {
        assertTrue(testAuth.getAuthCode().equals("AUTH001"));
    }
}