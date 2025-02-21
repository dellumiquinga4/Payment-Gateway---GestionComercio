package com.banquito.gateway.gestion.banquito.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ComercioDTO {

    @NotBlank(message = "El código de comercio es requerido")
    @Size(max = 15, message = "El código de comercio no puede exceder los 15 caracteres")
    private String codigoComercio;

    @NotBlank(message = "El código externo es requerido")
    @Size(max = 15, message = "El código externo no puede exceder los 15 caracteres")
    private String codigoExterno;

    @NotBlank(message = "El RUC es requerido")
    @Pattern(regexp = "^[0-9]{13}$", message = "El RUC debe tener 13 dígitos numéricos")
    private String ruc;

    @NotBlank(message = "El nombre comercial es requerido")
    @Size(max = 100, message = "El nombre comercial no puede exceder los 100 caracteres")
    private String nombreComercial;

    private LocalDateTime fechaCreacion;

    @NotBlank(message = "El estado es requerido")
    @Pattern(regexp = "^(ACT|INA|SUS)$", message = "El estado debe ser ACT, INA o SUS")
    private String estado;

    @NotBlank(message = "El SWIFT del banco es requerido")
    @Size(max = 11, message = "El SWIFT del banco no puede exceder los 11 caracteres")
    private String swiftBanco;

    @Size(max = 25, message = "La cuenta de suspensión no puede exceder los 25 caracteres")
    private String cuentaSuspension;
} 