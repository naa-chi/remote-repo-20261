package com.transit.stations.client;

import com.transit.stations.dto.LineDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "lines-service")
public interface LineFeignClient {
    @GetMapping("/api/lines/code/{lineCode}")
    LineDTO getLineByCode(@PathVariable("lineCode") Integer lineCode);
}