package com.banquito.gateway.gestion.banquito.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "DTO para la gestión de POS de comercios")
public class PosComercioDTO {

    @Schema(description = "Código único del POS", example = "POS001")
    @NotBlank(message = "El código POS es requerido")
    @Size(max = 15, message = "El código POS no puede exceder los 15 caracteres")
    private String codigoPos;

    @NotBlank(message = "El modelo es requerido")
    @Size(max = 10, message = "El modelo no puede tener más de 10 caracteres")
    @Schema(description = "Modelo del POS", example = "VX520")
    private String modelo;

    @NotBlank(message = "El código de comercio es requerido")
    @Schema(description = "Código del comercio al que pertenece el POS", example = "COM001")
    @Size(max = 15, message = "El código de comercio no puede exceder los 15 caracteres")
    private String codigoComercio;

    @NotBlank(message = "La dirección MAC es requerida")
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", message = "La dirección MAC no tiene un formato válido")
    @Schema(description = "Dirección MAC del POS", example = "00:1B:44:11:3A:B7")
    private String direccionMac;

    @Pattern(regexp = "^(ACT|INA)$", message = "El estado debe ser ACT o INA")
    @Schema(description = "Estado del POS (ACT: Activo, INA: Inactivo)", example = "ACT")
    private String estado;

    @Schema(description = "Fecha de activación del POS")
    private LocalDateTime fechaActivacion;

    @Schema(description = "Fecha del último uso del POS")
    private LocalDateTime ultimoUso;
} 