package com.transit.stations.client;

import com.transit.stations.dto.CityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cities-service")
public interface CityFeignClient {
    @GetMapping("/api/cities/code/{cityCode}")
    CityDTO getCityByCode(@PathVariable("cityCode") String cityCode);
}