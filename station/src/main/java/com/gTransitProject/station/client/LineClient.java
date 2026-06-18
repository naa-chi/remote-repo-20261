package com.gTransitProject.station.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.gTransitProject.station.dto.LineDTO;

@Component
public class LineClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public LineDTO getLineById(Integer id){

        String url = "http://localhost:7774/api/lines/" + id;

        return restTemplate.getForObject(url, LineDTO.class);
    }

    public LineDTO getLineByNumber(Integer lineNumber){

    String url =
        "http://localhost:7774/api/lines/number/" + lineNumber;

    return restTemplate.getForObject(
            url,
            LineDTO.class);
}
}