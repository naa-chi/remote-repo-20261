package com.transit.cities.dto.mapper;

import com.transit.cities.dto.request.CityRequestDTO;
import com.transit.cities.dto.response.CityResponseDTO;
import com.transit.cities.model.CityModel;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {
    public CityResponseDTO toResponse(CityModel model) {
        if (model == null) return null;
        CityResponseDTO dto = new CityResponseDTO();
        dto.setId(model.getId());
        dto.setThreeLetterCityCode(model.getThreeLetterCityCode());
        dto.setFullCityName(model.getFullCityName());
        dto.setFoundingCityDate(model.getFoundingCityDate());
        dto.setCityPopulation(model.getCityPopulation());
        dto.setCountryCode(model.getCountryCode());
        return dto;
    }

    public CityModel toEntity(CityRequestDTO dto) {
        if (dto == null) return null;
        CityModel model = new CityModel();
        model.setThreeLetterCityCode(dto.getThreeLetterCityCode());
        model.setFullCityName(dto.getFullCityName());
        model.setFoundingCityDate(dto.getFoundingCityDate());
        model.setCityPopulation(dto.getCityPopulation());
        model.setCountryCode(dto.getCountryCode());
        return model;
    }
}