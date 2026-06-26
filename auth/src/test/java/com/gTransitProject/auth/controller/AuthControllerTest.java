package com.gTransitProject.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gTransitProject.auth.model.Auth;
import com.gTransitProject.auth.service.AuthService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllAuths() throws Exception {

        Auth auth = new Auth();
        auth.setAuthId(1);
        auth.setAuthCode("AUTH001");

        when(authService.getAllAuths())
                .thenReturn(List.of(auth));

        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateAuth() throws Exception {

        Auth auth = new Auth();
        auth.setRequestDescription("Prueba");
        auth.setOriginCity("Santiago");
        auth.setDestinationCity("Rancagua");
        auth.setAuthCode("AUTH001");
        auth.setSupervisorCode("SUP-STG-01");
        auth.setAuthorized(true);

        when(authService.saveAuth(any(Auth.class)))
                .thenReturn(auth);

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auth)))
                .andExpect(status().isOk()); // <- NO es Created
    }
}