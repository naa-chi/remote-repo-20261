package com.transit.stations.service;

import com.transit.stations.client.CityFeignClient;
import com.transit.stations.client.LineFeignClient;
import com.transit.stations.dto.CityDTO;
import com.transit.stations.dto.LineDTO;
import com.transit.stations.dto.mapper.StationMapper;
import com.transit.stations.dto.request.StationRequestDTO;
import com.transit.stations.dto.response.StationResponseDTO;
import com.transit.stations.fallback.StationServiceFallback;
import com.transit.stations.model.StationModel;
import com.transit.stations.repository.StationRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository repository;
    private final StationMapper mapper;
    private final StationServiceFallback fallback;
    private final CityFeignClient cityFeign;
    private final LineFeignClient lineFeign;

    public StationService(StationRepository repository,
                          StationMapper mapper,
                          StationServiceFallback fallback,
                          CityFeignClient cityFeign,
                          LineFeignClient lineFeign) {
        this.repository = repository;
        this.mapper = mapper;
        this.fallback = fallback;
        this.cityFeign = cityFeign;
        this.lineFeign = lineFeign;
    }

    @CircuitBreaker(name = "stationService", fallbackMethod = "handleGetStationFallback")
    public StationResponseDTO getStationById(Long id) {
        StationModel model = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found with id: " + id));
        StationResponseDTO dto = mapper.toResponse(model);
        enrichWithFeign(dto);
        return dto;
    }

    @CircuitBreaker(name = "stationService", fallbackMethod = "handleGetStationByCodeFallback")
    public StationResponseDTO getStationByCode(String stationCode) {
        StationModel model = repository.findByStationCode(stationCode)
                .orElseThrow(() -> new RuntimeException("Station not found with code: " + stationCode));
        StationResponseDTO dto = mapper.toResponse(model);
        enrichWithFeign(dto);
        return dto;
    }

    @CircuitBreaker(name = "stationService", fallbackMethod = "handleGetStationsByCityFallback")
    public List<StationResponseDTO> getStationsByCity(String cityCode) {
        return repository.findByCityCode(cityCode).stream()
                .map(mapper::toResponse)
                .peek(this::enrichWithFeign)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "stationService", fallbackMethod = "handleGetStationsByLineFallback")
    public List<StationResponseDTO> getStationsByLine(Integer lineCode) {
        return repository.findByAnyLineCode(lineCode).stream()   // <-- changed
                .map(mapper::toResponse)
                .peek(this::enrichWithFeign)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "stationService", fallbackMethod = "handleGetStationFallback")
    public StationResponseDTO createStation(StationRequestDTO request) {
        StationModel entity = mapper.toEntity(request);
        StationModel saved = repository.save(entity);
        StationResponseDTO dto = mapper.toResponse(saved);
        enrichWithFeign(dto);
        return dto;
    }

    @CircuitBreaker(name = "stationService", fallbackMethod = "handleGetStationFallback")
    public StationResponseDTO updateStation(Long id, StationRequestDTO request) {
        return repository.findById(id).map(existing -> {
            StationModel updated = mapper.toEntity(request);
            updated.setId(id);
            StationModel saved = repository.save(updated);
            StationResponseDTO dto = mapper.toResponse(saved);
            enrichWithFeign(dto);
            return dto;
        }).orElseThrow(() -> new RuntimeException("Station not found with id: " + id));
    }

    @CircuitBreaker(name = "stationService", fallbackMethod = "handleGetStationFallback")
    public void deleteStation(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Station not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @CircuitBreaker(name = "stationService", fallbackMethod = "handleGetStationsFallbackList")
    public List<StationResponseDTO> getAllStations() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .peek(this::enrichWithFeign)
                .collect(Collectors.toList());
    }

    // ---- Feign enrichment ----

    private void enrichWithFeign(StationResponseDTO dto) {
        // Enrich city name
        try {
            CityDTO city = cityFeign.getCityByCode(dto.getCityCode());
            dto.setCityName(city != null ? city.getFullCityName() : "UNKNOWN");
        } catch (Exception e) {
            dto.setCityName("FALLBACK_CITY");
        }

        // Enrich each line
        enrichLine(dto, dto.getLineCode1(), "1");
        enrichLine(dto, dto.getLineCode2(), "2");
        enrichLine(dto, dto.getLineCode3(), "3");
        enrichLine(dto, dto.getLineCode4(), "4");
    }

    private void enrichLine(StationResponseDTO dto, Integer lineCode, String suffix) {
        if (lineCode != null) {
            try {
                LineDTO line = lineFeign.getLineByCode(lineCode);
                if (line != null) {
                    setLineFields(dto, suffix, line);
                } else {
                    setFallbackLine(dto, suffix);
                }
            } catch (Exception e) {
                setFallbackLine(dto, suffix);
            }
        }
    }

    private void setLineFields(StationResponseDTO dto, String suffix, LineDTO line) {
        switch (suffix) {
            case "1":
                dto.setLine1Length(line.getLineLengthKM());
                dto.setLine1PeopleServed(line.getPeopleServedMonthlyEstimate());
                break;
            case "2":
                dto.setLine2Length(line.getLineLengthKM());
                dto.setLine2PeopleServed(line.getPeopleServedMonthlyEstimate());
                break;
            case "3":
                dto.setLine3Length(line.getLineLengthKM());
                dto.setLine3PeopleServed(line.getPeopleServedMonthlyEstimate());
                break;
            case "4":
                dto.setLine4Length(line.getLineLengthKM());
                dto.setLine4PeopleServed(line.getPeopleServedMonthlyEstimate());
                break;
        }
    }

    private void setFallbackLine(StationResponseDTO dto, String suffix) {
        // leave as 0 or null – we can set 0L explicitly
        switch (suffix) {
            case "1":
                dto.setLine1Length(0L);
                dto.setLine1PeopleServed(0L);
                break;
            case "2":
                dto.setLine2Length(0L);
                dto.setLine2PeopleServed(0L);
                break;
            case "3":
                dto.setLine3Length(0L);
                dto.setLine3PeopleServed(0L);
                break;
            case "4":
                dto.setLine4Length(0L);
                dto.setLine4PeopleServed(0L);
                break;
        }
    }

    // ---- Fallback methods for CircuitBreaker ----

    public StationResponseDTO handleGetStationFallback(Long id, Throwable t) {
        return fallback.getStationFallback(id, t);
    }

    public StationResponseDTO handleGetStationByCodeFallback(String code, Throwable t) {
        return fallback.getStationFallback(code, t);
    }

    public List<StationResponseDTO> handleGetStationsByCityFallback(String cityCode, Throwable t) {
        return fallback.getStationsFallbackList(t);
    }

    public List<StationResponseDTO> handleGetStationsByLineFallback(Integer lineCode, Throwable t) {
        return fallback.getStationsFallbackList(t);
    }

    public List<StationResponseDTO> handleGetStationsFallbackList(Throwable t) {
        return fallback.getStationsFallbackList(t);
    }
}