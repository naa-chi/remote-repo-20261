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

    public void deleteTypeEngine(Integer id) {
        repo.deleteById(id);
    }

    public typeEngine updateTypeEngine(Integer id, typeEngine updatedTypeEngine) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setNameCodeEngine(updatedTypeEngine.getNameCodeEngine());
                    existing.setType(updatedTypeEngine.getType());
                    return repo.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("No such id exists: " + id));
    }
}
