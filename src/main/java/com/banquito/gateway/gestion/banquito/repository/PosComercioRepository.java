package com.banquito.gateway.gestion.banquito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.gateway.gestion.banquito.model.PosComercio;
import java.util.List;

@Repository
public interface PosComercioRepository extends JpaRepository<PosComercio, String> {
    List<PosComercio> findByComercioCodigoComercio(String codigoComercio);
    List<PosComercio> findByEstado(String estado);
    List<PosComercio> findByDireccionMac(String direccionMac);
} 