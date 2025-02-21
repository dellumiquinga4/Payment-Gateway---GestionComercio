package com.banquito.gateway.gestion.banquito.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.banquito.gateway.gestion.banquito.model.Comercio;
import com.banquito.gateway.gestion.banquito.repository.ComercioRepository;
import com.banquito.gateway.gestion.banquito.exception.ComercioNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ComercioService {

    private final ComercioRepository comercioRepository;

    public ComercioService(ComercioRepository comercioRepository) {
        this.comercioRepository = comercioRepository;
    }

    @Transactional(readOnly = true)
    public List<Comercio> findAll() {
        log.info("Obteniendo todos los comercios");
        return this.comercioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Comercio findById(String codigoComercio) {
        log.info("Buscando comercio con código: {}", codigoComercio);
        return this.comercioRepository.findById(codigoComercio)
                .orElseThrow(() -> new ComercioNotFoundException(codigoComercio));
    }

    @Transactional
    public Comercio create(Comercio comercio) {
        log.info("Creando nuevo comercio: {}", comercio);
        comercio.setFechaCreacion(LocalDateTime.now());
        return this.comercioRepository.save(comercio);
    }

    @Transactional
    public Comercio update(String codigoComercio, Comercio comercio) {
        log.info("Actualizando comercio con código: {}", codigoComercio);
        Comercio comercioExistente = findById(codigoComercio);
        comercio.setCodigoComercio(codigoComercio);
        comercio.setFechaCreacion(comercioExistente.getFechaCreacion());
        return this.comercioRepository.save(comercio);
    }

    @Transactional
    public void delete(String codigoComercio) {
        log.info("Eliminando comercio con código: {}", codigoComercio);
        Comercio comercio = findById(codigoComercio);
        this.comercioRepository.delete(comercio);
    }

    @Transactional(readOnly = true)
    public List<Comercio> findByEstado(String estado) {
        log.info("Buscando comercios por estado: {}", estado);
        return this.comercioRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Comercio> findByRuc(String ruc) {
        log.info("Buscando comercios por RUC: {}", ruc);
        return this.comercioRepository.findByRuc(ruc);
    }

    @Transactional(readOnly = true)
    public List<Comercio> findBySwiftBanco(String swiftBanco) {
        log.info("Buscando comercios por SWIFT del banco: {}", swiftBanco);
        return this.comercioRepository.findBySwiftBanco(swiftBanco);
    }
} 