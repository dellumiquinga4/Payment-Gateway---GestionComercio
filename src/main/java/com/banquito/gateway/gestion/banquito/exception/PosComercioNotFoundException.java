package com.banquito.gateway.gestion.banquito.exception;

public class PosComercioNotFoundException extends RuntimeException {
    private final String codigoPos;

    public PosComercioNotFoundException(String codigoPos) {
        super();
        this.codigoPos = codigoPos;
    }

    @Override
    public String getMessage() {
        return "No se encontró el POS con código: " + codigoPos;
    }
} 