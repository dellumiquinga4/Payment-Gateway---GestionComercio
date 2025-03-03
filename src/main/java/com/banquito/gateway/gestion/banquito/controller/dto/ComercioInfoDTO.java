package com.banquito.gateway.gestion.banquito.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComercioInfoDTO {
    private String codigo_comercio;
    private String nombre_comercio;
    private String swift_banco;
    private String cuenta_iban;
    private String estado;
} 