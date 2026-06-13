package com.gTransitProject.typeengine.service;

import com.gTransitProject.typeengine.model.typeEngine;
import com.gTransitProject.typeengine.repo.typeEngineRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class typeEngineService {
    private final typeEngineRepo repo;

    public List<typeEngine> getAll() {
        return repo.findAll();
    }

    public typeEngine getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("No such id exists: "  + id));
    }

    public typeEngine createTypeEngine(typeEngine typeEngineData) {
        return repo.save(typeEngineData);
    }
}
