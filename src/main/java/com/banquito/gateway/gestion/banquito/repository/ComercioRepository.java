package com.banquito.gateway.gestion.banquito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.banquito.gateway.gestion.banquito.model.Comercio;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComercioRepository extends JpaRepository<Comercio, String> {
    Page<Comercio> findAll(Pageable pageable);
    List<Comercio> findByEstado(String estado);
    List<Comercio> findByRuc(String ruc);
    List<Comercio> findBySwiftBanco(String swiftBanco);
    List<Comercio> findByNombreComercialContainingIgnoreCase(String nombreComercial);
    Optional<Comercio> findByCuentaIban(String cuentaIban);
} 