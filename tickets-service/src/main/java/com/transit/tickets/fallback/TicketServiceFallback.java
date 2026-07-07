package com.transit.tickets.fallback;

import com.transit.tickets.dto.response.TicketResponseDTO;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class TicketServiceFallback {

    public TicketResponseDTO getTicketFallback(Long id, Throwable t) {
        return createDefaultFallbackTicket(id != null ? id : -1L);
    }

    public TicketResponseDTO getTicketByCodeFallback(String code, Throwable t) {
        return createDefaultFallbackTicket(-1L);
    }

    public List<TicketResponseDTO> getTicketsFallbackList(Throwable t) {
        return Collections.singletonList(createDefaultFallbackTicket(-1L));
    }

    private TicketResponseDTO createDefaultFallbackTicket(Long id) {
        TicketResponseDTO fallbackDto = new TicketResponseDTO();
        fallbackDto.setId(id);
        fallbackDto.setCode("ERROR");
        fallbackDto.setCityCodeOrigin("ERR");
        fallbackDto.setCityCodeDestination("ERR");
        fallbackDto.setPrice(0.0);
        fallbackDto.setClientId(0L);
        fallbackDto.setTrainId(0L);
        fallbackDto.setDepartureDate(Date.valueOf(LocalDate.now()));
        return fallbackDto;
    }
}