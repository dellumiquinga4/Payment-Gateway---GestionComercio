package com.banquito.gateway.gestion.banquito.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.banquito.gateway.gestion.banquito.controller.dto.PosComercioDTO;
import com.banquito.gateway.gestion.banquito.model.PosComercio;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PosComercioMapper {
    
    @Mapping(source = "comercio.codigoComercio", target = "codigoComercio")
    PosComercioDTO toDTO(PosComercio model);
    
    @Mapping(source = "codigoComercio", target = "comercio.codigoComercio")
    PosComercio toModel(PosComercioDTO dto);
} 