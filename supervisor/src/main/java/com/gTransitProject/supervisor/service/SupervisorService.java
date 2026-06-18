package com.gTransitProject.supervisor.service;

import com.gTransitProject.supervisor.exception.ResourceNotFoundException;
import com.gTransitProject.supervisor.model.Supervisor;
import com.gTransitProject.supervisor.repo.SupervisorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorService {

    @Autowired
    private SupervisorRepository supervisorRepository;

    public List<Supervisor> getAllSupervisors() {
        return supervisorRepository.findAll();
    }

    public Supervisor saveSupervisor(Supervisor supervisor) {
        return supervisorRepository.save(supervisor);
    }

    public Supervisor getSupervisorById(Integer id) {
        return supervisorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supervisor no encontrado"));
    }

    public Supervisor findByCode(String code) {
        return supervisorRepository
                .findBySupervisorCode(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supervisor no encontrado"));
    }

    public void deleteSupervisor(Integer id) {
        supervisorRepository.deleteById(id);
    }
    public Supervisor updateSupervisor(
        Integer id,
        Supervisor newSupervisor){

    Supervisor supervisor =
            getSupervisorById(id);

    supervisor.setSupervisorName(
            newSupervisor.getSupervisorName());

    supervisor.setSupervisorCode(
            newSupervisor.getSupervisorCode());

    supervisor.setCityCode(
            newSupervisor.getCityCode());

    supervisor.setAuthorized(
            newSupervisor.getAuthorized());

    return supervisorRepository.save(supervisor);
}
}