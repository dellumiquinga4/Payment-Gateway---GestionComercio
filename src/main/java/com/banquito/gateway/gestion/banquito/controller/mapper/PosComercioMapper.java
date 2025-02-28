package com.banquito.gateway.gestion.banquito.controller.mapper;

import org.springframework.stereotype.Component;
import com.banquito.gateway.gestion.banquito.controller.dto.PosComercioDTO;
import com.banquito.gateway.gestion.banquito.model.PosComercio;
import com.banquito.gateway.gestion.banquito.model.Comercio;

@Component
public class PosComercioMapper {

    public PosComercioDTO toDTO(PosComercio model) {
        if (model == null) {
            return null;
        }

        PosComercioDTO dto = new PosComercioDTO();
        dto.setCodigoPos(model.getCodigoPos());
        dto.setModelo(model.getModelo());
        dto.setCodigoComercio(model.getComercio() != null ? model.getComercio().getCodigoComercio() : null);
        dto.setDireccionMac(model.getDireccionMac());
        dto.setEstado(model.getEstado());
        dto.setFechaActivacion(model.getFechaActivacion());
        dto.setUltimoUso(model.getUltimoUso());

        return dto;
    }

    public PosComercio toModel(PosComercioDTO dto) {
        if (dto == null) {
            return null;
        }

        PosComercio model = new PosComercio();
        model.setCodigoPos(dto.getCodigoPos());
        model.setModelo(dto.getModelo());
        
        if (dto.getCodigoComercio() != null) {
            Comercio comercio = new Comercio();
            comercio.setCodigoComercio(dto.getCodigoComercio());
            model.setComercio(comercio);
        }

        model.setDireccionMac(dto.getDireccionMac());
        model.setEstado(dto.getEstado());
        model.setFechaActivacion(dto.getFechaActivacion());
        model.setUltimoUso(dto.getUltimoUso());

        return model;
    }
} 