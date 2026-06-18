package com.gTransitProject.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SupervisorClientService {

    @Autowired
    private RestTemplate restTemplate;

    public Boolean validateSupervisor(String code) {

        String url =
                "http://localhost:7781/api/supervisor/validate/"
                        + code;

        return restTemplate.getForObject(
                url,
                Boolean.class
        );
    }
}