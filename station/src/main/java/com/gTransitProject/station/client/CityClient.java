package com.gTransitProject.station.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.gTransitProject.station.dto.CityDTO;

@Component
public class CityClient {

    private final RestTemplate restTemplate =
            new RestTemplate();

    public CityDTO getCityByCode(String code){

        String url =
            "http://localhost:7777/api/city/code/" + code;

        return restTemplate.getForObject(
                url,
                CityDTO.class);
    }
}