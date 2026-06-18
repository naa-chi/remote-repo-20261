package com.gTransitProject.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthClientService {

    @Autowired
    private RestTemplate restTemplate;

    public Boolean validateAuth(String code){

        String url =
                "http://localhost:7780/api/auth/validate/"
                        + code;

        return restTemplate.getForObject(
                url,
                Boolean.class
        );
    }
}