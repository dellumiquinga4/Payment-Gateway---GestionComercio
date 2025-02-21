package com.banquito.gateway.gestion.banquito.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.banquito.gateway.gestion.banquito.controller.dto.ComercioDTO;
import com.banquito.gateway.gestion.banquito.model.Comercio;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ComercioMapper {
    
    ComercioDTO toDTO(Comercio model);
    
    Comercio toModel(ComercioDTO dto);
} 