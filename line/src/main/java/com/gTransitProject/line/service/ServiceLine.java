package com.gTransitProject.line.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gTransitProject.line.model.Line;
import com.gTransitProject.line.repo.RepositoryLine;

@Service
public class ServiceLine {

    @Autowired
    private RepositoryLine lineRepo;

    private static final Logger logger =
    LoggerFactory.getLogger(ServiceLine.class);

    public List<Line> getLines(){

        logger.info("Obteniendo todas las lineas");

        return lineRepo.findAll();
    }

    public Line saveLine(Line line){

        logger.info("Guardando linea: " + line.getLineNumber());

        return lineRepo.save(line);
    }

    public Line getLineById(Integer id){

        logger.info("Buscando linea con ID: " + id);

        return lineRepo.findById(id).orElse(null);
    }

    public Line getLineByNumber(Integer lineNumber){

    logger.info("Buscando linea por numero: " + lineNumber);

    return lineRepo.findByLineNumber(lineNumber)
            .orElse(null);
} 

    public void deleteLine(Integer id){

        logger.warn("Eliminando linea con ID: " + id);

        lineRepo.deleteById(id);
    }

    public Line updateLine(Integer id, Line updatedLine){

        Line existingLine = lineRepo.findById(id).orElse(null);

        if(existingLine != null){

            logger.info("Actualizando linea con ID: " + id);

            existingLine.setLineNumber(updatedLine.getLineNumber());
            existingLine.setLengthInKm(updatedLine.getLengthInKm());

            return lineRepo.save(existingLine);
        }

        logger.error("No se encontro la linea con ID: " + id);

        return null;
    }
}