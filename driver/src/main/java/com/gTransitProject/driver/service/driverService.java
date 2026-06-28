package com.gTransitProject.driver.service;
import com.gTransitProject.driver.exception.resourceNotFoundException;
import com.gTransitProject.driver.model.driverModel;
import com.gTransitProject.driver.repository.driverRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class driverService {
    
    @Autowired
    private driverRepository driverRepository;

    public driverModel createDriver(driverModel driver) {
        return driverRepository.save(driver);
    }

    public List<driverModel> getAllDrivers() {
        return driverRepository.findAll();
    }

    public driverModel getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new resourceNotFoundException("Driver not found with id: " + id));
    }

    public driverModel updateDriver(Long id, driverModel driverDetails) {
        driverModel driver = getDriverById(id);
        driver.setDriverName(driverDetails.getDriverName());
        driver.setDriverLicenseNumber(driverDetails.getDriverLicenseNumber());
        return driverRepository.save(driver);
    }

    public void deleteDriver(Long id) {
        driverModel driver = getDriverById(id);
        driverRepository.delete(driver);
    }


}
