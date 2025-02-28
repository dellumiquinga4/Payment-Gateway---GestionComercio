package com.banquito.gateway.gestion.banquito.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "DTO para la gestión de comercios")
public class ComercioDTO {

    @Schema(description = "Código único del comercio", example = "COM001")
    @NotBlank(message = "El código de comercio es requerido")
    @Size(max = 15, message = "El código de comercio no puede exceder los 15 caracteres")
    private String codigoComercio;

    @NotBlank(message = "El código interno es requerido")
    @Size(max = 10, message = "El código interno no puede tener más de 10 caracteres")
    @Schema(description = "Código interno del comercio", example = "INT001")
    private String codigoInterno;

    @NotBlank(message = "El RUC es requerido")
    @Pattern(regexp = "^[0-9]{13}$", message = "El RUC debe tener 13 dígitos numéricos")
    @Schema(description = "RUC del comercio", example = "1234567890001")
    private String ruc;

    @NotBlank(message = "La razón social es requerida")
    @Size(max = 100, message = "La razón social no puede tener más de 100 caracteres")
    @Schema(description = "Razón social del comercio", example = "Empresa XYZ S.A.")
    private String razonSocial;

    @NotBlank(message = "El nombre comercial es requerido")
    @Size(max = 100, message = "El nombre comercial no puede tener más de 100 caracteres")
    @Schema(description = "Nombre comercial del comercio", example = "XYZ Store")
    private String nombreComercial;

    @Schema(description = "Fecha de creación del comercio")
    private LocalDateTime fechaCreacion;

    @NotNull(message = "El código de comisión es requerido")
    @Schema(description = "Código de comisión asignado al comercio", example = "1")
    private Integer codigoComision;

    @Pattern(regexp = "^(ACT|INA|SUS)$", message = "El estado debe ser ACT, INA o SUS")
    @Schema(description = "Estado del comercio (ACT: Activo, INA: Inactivo, SUS: Suspendido)", example = "ACT")
    @NotBlank(message = "El estado es requerido")
    private String estado;

    @NotBlank(message = "El código SWIFT del banco es requerido")
    @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$", message = "El código SWIFT no tiene un formato válido")
    @Schema(description = "Código SWIFT del banco", example = "BOFAUS3N")
    private String swiftBanco;

    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$", message = "El IBAN no tiene un formato válido")
    @Schema(description = "Número de cuenta IBAN", example = "ES9121000418450200051332")
    private String cuentaIban;

    @Schema(description = "Fecha de activación del comercio")
    private LocalDateTime fechaActivacion;

    @Schema(description = "Fecha de suspensión del comercio")
    private LocalDateTime fechaSuspension;
} 