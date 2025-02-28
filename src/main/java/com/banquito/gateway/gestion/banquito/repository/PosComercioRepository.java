package com.banquito.gateway.gestion.banquito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.banquito.gateway.gestion.banquito.model.PosComercio;
import java.util.List;

@Repository
public interface PosComercioRepository extends JpaRepository<PosComercio, String> {
    Page<PosComercio> findAll(Pageable pageable);
    Page<PosComercio> findByComercioCodigoComercio(String codigoComercio, Pageable pageable);
    List<PosComercio> findByComercioCodigoComercio(String codigoComercio);
    List<PosComercio> findByEstado(String estado);
    List<PosComercio> findByDireccionMac(String direccionMac);
    List<PosComercio> findByModelo(String modelo);
} 