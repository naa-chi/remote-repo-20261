package com.gTransitProject.city.service;

import java.util.List;

import com.gTransitProject.city.exception.businessException;
import com.gTransitProject.city.exception.resourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gTransitProject.city.model.City;
import com.gTransitProject.city.repo.CityRepository;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(CityService.class);

    public List<City> getAllCities() {

        logger.info("Obteniendo todas las ciudades");

        return cityRepository.findAll();
    }

    public City findByCityCode(String code) {

        logger.info("Buscando ciudad por codigo: {}", code);

        return cityRepository.findByCityCode(code)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Ciudad no encontrada con el codigo: " + code));
    }

    public City saveCity(City city) {

        long totalCities = cityRepository.count();

        if (totalCities >= 2) {

            logger.warn("Intento de agregar una tercera ciudad");

            throw new businessException(
                    "Solo se permiten 2 ciudades en el sistema");
        }

        if (cityRepository.findByCityCode(city.getCityCode()).isPresent()) {

            logger.error(
                    "Intento de duplicar el codigo de ciudad: {}",
                    city.getCityCode());

            throw new businessException(
                    "El codigo de ciudad ya existe");
        }

        boolean lineaExiste = cityRepository.findAll()
                .stream()
                .anyMatch(c ->
                        c.getLineNumber().equals(city.getLineNumber()));

        if (lineaExiste) {

            logger.error(
                    "La linea {} ya esta asignada a otra ciudad",
                    city.getLineNumber());

            throw new businessException(
                    "La linea ya se encuentra asignada a otra ciudad");
        }

        logger.info("Nueva ciudad creada: {}", city.getCityName());

        return cityRepository.save(city);
    }

    public City updateCity(Integer id, City updatedCity) {

        City existingCity =
                cityRepository.findById(id)
                        .orElseThrow(() ->
                                new resourceNotFoundException(
                                        "No existe una ciudad con el ID: " + id));

        boolean lineaExiste = cityRepository.findAll()
                .stream()
                .anyMatch(c ->
                        !c.getCityId().equals(id)
                                &&
                                c.getLineNumber().equals(
                                        updatedCity.getLineNumber()));

        if (lineaExiste) {

            logger.error(
                    "La linea {} ya pertenece a otra ciudad",
                    updatedCity.getLineNumber());

            throw new businessException(
                    "No se puede asignar la linea "
                            + updatedCity.getLineNumber()
                            + ". Ya se encuentra vinculada a otra ciudad.");
        }

        existingCity.setCityName(updatedCity.getCityName());
        existingCity.setCityCode(updatedCity.getCityCode());
        existingCity.setLineNumber(updatedCity.getLineNumber());
        existingCity.setPopulation(updatedCity.getPopulation());

        logger.info("Ciudad actualizada: {}",
                existingCity.getCityName());

        return cityRepository.save(existingCity);
    }

    public void deleteCity(Integer id) {

        logger.warn("Eliminando ciudad con ID: {}", id);

        cityRepository.deleteById(id);
    }
}