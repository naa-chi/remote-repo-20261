package com.gTransitProject.auth.controller;

import com.gTransitProject.auth.model.Auth;
import com.gTransitProject.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private MockMvc mockMvc;

    private AuthService authService;

    @BeforeEach
    void setup() {
        authService = Mockito.mock(AuthService.class);

        AuthController controller =
                new AuthController();

        org.springframework.test.util.ReflectionTestUtils
                .setField(
                        controller,
                        "authService",
                        authService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void getAllAuthsShouldReturn200() throws Exception {

        when(authService.getAllAuths())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isOk());
    }
}