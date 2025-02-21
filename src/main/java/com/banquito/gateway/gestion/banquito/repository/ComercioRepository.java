package com.banquito.gateway.gestion.banquito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.gateway.gestion.banquito.model.Comercio;
import java.util.List;

@Repository
public interface ComercioRepository extends JpaRepository<Comercio, String> {
    List<Comercio> findByEstado(String estado);
    List<Comercio> findByRuc(String ruc);
    List<Comercio> findBySwiftBanco(String swiftBanco);
} 