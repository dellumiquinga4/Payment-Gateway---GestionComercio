package com.banquito.gateway.gestion.banquito.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "GTW_POS_COMERCIO")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PosComercio {

    @Id
    @Column(name = "CODIGO_POS", nullable = false, length = 15)
    private String codigoPos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODIGO_COMERCIO", nullable = false)
    private Comercio comercio;

    @Column(name = "DIRECCION_MAC", nullable = false, length = 100)
    private String direccionMac;

    @Column(name = "ESTADO", nullable = false, length = 3)
    private String estado;

    @Column(name = "FECHA_ACTIVACION")
    private LocalDateTime fechaActivacion;

    @Column(name = "ULTIMO_USO")
    private LocalDateTime ultimoUso;

    public PosComercio(String codigoPos) {
        this.codigoPos = codigoPos;
    }

    @Override
    public int hashCode() {
        return codigoPos != null ? codigoPos.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PosComercio that = (PosComercio) o;
        return codigoPos != null && codigoPos.equals(that.codigoPos);
    }
} 