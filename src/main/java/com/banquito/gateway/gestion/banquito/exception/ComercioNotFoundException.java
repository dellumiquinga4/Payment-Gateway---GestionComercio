package com.banquito.gateway.gestion.banquito.exception;

public class ComercioNotFoundException extends RuntimeException {
    private final String codigoComercio;

    public ComercioNotFoundException(String codigoComercio) {
        super();
        this.codigoComercio = codigoComercio;
    }

    @Override
    public String getMessage() {
        return "No se encontró el comercio con código: " + codigoComercio;
    }
} 