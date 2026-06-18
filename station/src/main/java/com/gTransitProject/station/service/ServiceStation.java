package com.gTransitProject.station.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gTransitProject.station.model.station;
import com.gTransitProject.station.repo.RepositoryStation;

@Service
public class ServiceStation {

    @Autowired
    private RepositoryStation stationRepo;

    private static final Logger logger =
            LoggerFactory.getLogger(ServiceStation.class);

    public List<station> getStations(){

        logger.info("Obteniendo todas las estaciones");

        return stationRepo.findAll();
    }

    public station saveStation(station station){

        logger.info(
                "Guardando estacion: {}",
                station.getStationName());

        return stationRepo.save(station);
    }

    public station getStationById(Integer id){

        logger.info(
                "Buscando estacion con ID: {}",
                id);

        return stationRepo.findById(id).orElse(null);
    }

    public void deleteStation(Integer id){

        logger.warn(
                "Eliminando estacion con ID: {}",
                id);

        stationRepo.deleteById(id);
    }

    public station updateStation(Integer id, station updatedStation){

        station existingStation =
                stationRepo.findById(id).orElse(null);

        if(existingStation != null){

            logger.info(
                    "Actualizando estacion con ID: {}",
                    id);

            existingStation.setUniqueStationCode(
                    updatedStation.getUniqueStationCode());

            existingStation.setStationName(
                    updatedStation.getStationName());

            existingStation.setCityCode(
                    updatedStation.getCityCode());

            existingStation.setLineNumber(
                    updatedStation.getLineNumber());

            return stationRepo.save(existingStation);
        }

        logger.error(
                "No se encontro la estacion con ID: {}",
                id);

        return null;
    }
}