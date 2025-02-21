package com.banquito.gateway.gestion.banquito.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PosComercioDTO {

    @NotBlank(message = "El código POS es requerido")
    @Size(max = 15, message = "El código POS no puede exceder los 15 caracteres")
    private String codigoPos;

    @NotBlank(message = "El código de comercio es requerido")
    @Size(max = 15, message = "El código de comercio no puede exceder los 15 caracteres")
    private String codigoComercio;

    @NotBlank(message = "La dirección MAC es requerida")
    @Size(max = 100, message = "La dirección MAC no puede exceder los 100 caracteres")
    private String direccionMac;

    @NotBlank(message = "El estado es requerido")
    @Pattern(regexp = "^(ACT|INA)$", message = "El estado debe ser ACT o INA")
    private String estado;

    private LocalDateTime fechaActivacion;
    private LocalDateTime ultimoUso;
} 