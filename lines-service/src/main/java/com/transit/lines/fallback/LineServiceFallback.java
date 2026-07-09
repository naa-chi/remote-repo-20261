package com.transit.lines.fallback;

import com.transit.lines.dto.response.LineResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class LineServiceFallback {

    public LineResponseDTO getLineFallback(Long id, Throwable t) {
        System.err.println("CircuitBreaker triggered for Line ID " + id + ". Error: " + t.getMessage());
        return createFallbackLine();
    }

    public LineResponseDTO getLineFallback(Integer lineCode, Throwable t) {
        System.err.println("CircuitBreaker triggered for Line code " + lineCode + ". Error: " + t.getMessage());
        return createFallbackLine();
    }

    public List<LineResponseDTO> getLinesFallbackList(Throwable t) {
        System.err.println("CircuitBreaker triggered for lines list. Error: " + t.getMessage());
        return Collections.singletonList(createFallbackLine());
    }

    private LineResponseDTO createFallbackLine() {
        LineResponseDTO fallback = new LineResponseDTO();
        fallback.setId(-1L);
        fallback.setLineCode(-1);
        fallback.setLineLengthKM(0L);
        fallback.setPeopleServedMonthlyEstimate(0L);
        return fallback;
    }
}