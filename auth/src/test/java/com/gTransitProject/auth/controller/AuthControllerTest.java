package com.gTransitProject.auth.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; // ✅ Correct!
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gTransitProject.auth.controller.AuthController;
import com.gTransitProject.auth.exception.resourceNotFoundException;
import com.gTransitProject.auth.model.Auth;
import com.gTransitProject.auth.service.AuthService;

@WebMvcTest(AuthController.class)
@DisplayName("Auth Controller Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private Auth testAuth;

    @BeforeEach
    void setUp() {
        testAuth = new Auth(1, "Emergency transport", "Madrid", "Barcelona", "AUTH001", "SUP001", true);
    }

    @Test
    @DisplayName("GET /api/auth should return all authorizations")
    void testGetAllAuths() throws Exception {
        Auth auth2 = new Auth(2, "Regular transport", "Valencia", "Alicante", "AUTH002", "SUP002", true);
        List<Auth> auths = Arrays.asList(testAuth, auth2);

        when(authService.getAllAuths()).thenReturn(auths);

        mockMvc.perform(get("/api/auth")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(authService, times(1)).getAllAuths();
    }

    @Test
    @DisplayName("POST /api/auth should create a new authorization")
    void testCreateAuth() throws Exception {
        when(authService.saveAuth(any(Auth.class))).thenReturn(testAuth);

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAuth)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authCode").value("AUTH001"))
                .andExpect(jsonPath("$.authorized").value(true));

        verify(authService, times(1)).saveAuth(any(Auth.class));
    }

    @Test
    @DisplayName("GET /api/auth/{id} should return auth by ID")
    void testGetAuthById() throws Exception {
        when(authService.getAuthById(1)).thenReturn(testAuth);

        mockMvc.perform(get("/api/auth/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authCode").value("AUTH001"));

        verify(authService, times(1)).getAuthById(1);
    }

    @Test
    @DisplayName("GET /api/auth/{id} should return 500 when not found")
    void testGetAuthById_NotFound() throws Exception {
        when(authService.getAuthById(999))
                .thenThrow(new resourceNotFoundException("Autorizacion no encontrada"));

        mockMvc.perform(get("/api/auth/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(authService, times(1)).getAuthById(999);
    }

    @Test
    @DisplayName("GET /api/auth/code/{code} should return auth by code")
    void testFindByCode() throws Exception {
        when(authService.findByCode("AUTH001")).thenReturn(testAuth);

        mockMvc.perform(get("/api/auth/code/AUTH001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authCode").value("AUTH001"));

        verify(authService, times(1)).findByCode("AUTH001");
    }

    @Test
    @DisplayName("PUT /api/auth/{id} should update authorization")
    void testUpdateAuth() throws Exception {
        Auth updatedAuth = new Auth(1, "Updated transport", "Madrid", "Barcelona", "AUTH001", "SUP001", false);
        when(authService.updateAuth(1, testAuth)).thenReturn(updatedAuth);

        mockMvc.perform(put("/api/auth/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAuth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestDescription").value("Updated transport"));

        verify(authService, times(1)).updateAuth(1, testAuth);
    }

    @Test
    @DisplayName("DELETE /api/auth/{id} should delete authorization")
    void testDeleteAuth() throws Exception {
        doNothing().when(authService).deleteAuth(1);

        mockMvc.perform(delete("/api/auth/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(authService, times(1)).deleteAuth(1);
    }

    @Test
    @DisplayName("GET /api/auth should handle empty list")
    void testGetAllAuths_Empty() throws Exception {
        when(authService.getAllAuths()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/auth")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(authService, times(1)).getAllAuths();
    }

    @Test
    @DisplayName("POST /api/auth should validate required fields")
    void testCreateAuth_InvalidData() throws Exception {
        Auth invalidAuth = new Auth(null, null, null, null, null, null, null);

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidAuth)))
                .andExpect(status().isBadRequest());
    }
}