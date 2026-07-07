package com.transit.engines.service;

import com.transit.engines.assembler.EngineAssembler;
import com.transit.engines.dto.EngineDTO;
import com.transit.engines.model.EngineModel;
import com.transit.engines.repository.EnginesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EngineServiceTest {

    @Mock 
    private EnginesRepository repository;
    
    @Mock 
    private EngineAssembler assembler;
    
    @InjectMocks 
    private EngineService service;

    @Test
    void createEngine_ShouldReturnSavedEngine() {
        EngineDTO dto = new EngineDTO();
        EngineModel model = new EngineModel();
        
        when(assembler.toEntity(any(EngineDTO.class))).thenReturn(model);
        when(repository.save(any(EngineModel.class))).thenReturn(model);
        when(assembler.toDTO(any(EngineModel.class))).thenReturn(dto);

        EngineDTO result = service.createEngine(dto);
        
        assertNotNull(result);
        verify(repository, times(1)).save(any(EngineModel.class));
    }
}