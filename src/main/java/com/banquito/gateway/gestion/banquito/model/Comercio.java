package com.banquito.gateway.gestion.banquito.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "GTW_COMERCIO")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Comercio {

    @Id
    @Column(name = "CODIGO_COMERCIO", nullable = false, length = 15)
    private String codigoComercio;

    @Column(name = "CODIGO_EXTERNO", nullable = false, length = 15)
    private String codigoExterno;

    @Column(name = "RUC", nullable = false, length = 13)
    private String ruc;

    @Column(name = "NOMBRE_COMERCIAL", nullable = false, length = 100)
    private String nombreComercial;

    @Column(name = "FECHA_CREACION", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "ESTADO", nullable = false, length = 3)
    private String estado;

    @Column(name = "SWIFT_BANCO", nullable = false, length = 11)
    private String swiftBanco;

    @Column(name = "CUENTA_SUSPENSION", length = 25)
    private String cuentaSuspension;

    public Comercio(String codigoComercio) {
        this.codigoComercio = codigoComercio;
    }

    @Override
    public int hashCode() {
        return codigoComercio != null ? codigoComercio.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comercio comercio = (Comercio) o;
        return codigoComercio != null && codigoComercio.equals(comercio.codigoComercio);
    }
} 