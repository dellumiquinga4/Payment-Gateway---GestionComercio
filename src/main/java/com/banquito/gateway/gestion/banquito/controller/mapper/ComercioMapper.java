package com.banquito.gateway.gestion.banquito.controller.mapper;

import org.springframework.stereotype.Component;
import com.banquito.gateway.gestion.banquito.controller.dto.ComercioDTO;
import com.banquito.gateway.gestion.banquito.model.Comercio;

@Component
public class ComercioMapper {

    public ComercioDTO toDTO(Comercio model) {
        if (model == null) {
            return null;
        }

        ComercioDTO dto = new ComercioDTO();
        dto.setCodigoComercio(model.getCodigoComercio());
        dto.setCodigoInterno(model.getCodigoInterno());
        dto.setRuc(model.getRuc());
        dto.setRazonSocial(model.getRazonSocial());
        dto.setNombreComercial(model.getNombreComercial());
        dto.setFechaCreacion(model.getFechaCreacion());
        dto.setCodigoComision(model.getCodigoComision());
        dto.setEstado(model.getEstado());
        dto.setSwiftBanco(model.getSwiftBanco());
        dto.setCuentaIban(model.getCuentaIban());
        dto.setFechaActivacion(model.getFechaActivacion());
        dto.setFechaSuspension(model.getFechaSuspension());

        return dto;
    }

    public Comercio toModel(ComercioDTO dto) {
        if (dto == null) {
            return null;
        }

        Comercio model = new Comercio();
        model.setCodigoComercio(dto.getCodigoComercio());
        model.setCodigoInterno(dto.getCodigoInterno());
        model.setRuc(dto.getRuc());
        model.setRazonSocial(dto.getRazonSocial());
        model.setNombreComercial(dto.getNombreComercial());
        model.setFechaCreacion(dto.getFechaCreacion());
        model.setCodigoComision(dto.getCodigoComision());
        model.setEstado(dto.getEstado());
        model.setSwiftBanco(dto.getSwiftBanco());
        model.setCuentaIban(dto.getCuentaIban());
        model.setFechaActivacion(dto.getFechaActivacion());
        model.setFechaSuspension(dto.getFechaSuspension());

        return model;
    }
} 