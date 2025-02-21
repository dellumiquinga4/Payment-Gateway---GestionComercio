package com.banquito.gateway.gestion.banquito.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.banquito.gateway.gestion.banquito.model.PosComercio;
import com.banquito.gateway.gestion.banquito.model.Comercio;
import com.banquito.gateway.gestion.banquito.repository.PosComercioRepository;
import com.banquito.gateway.gestion.banquito.exception.PosComercioNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PosComercioService {

    private final PosComercioRepository posComercioRepository;
    private final ComercioService comercioService;

    public PosComercioService(PosComercioRepository posComercioRepository, ComercioService comercioService) {
        this.posComercioRepository = posComercioRepository;
        this.comercioService = comercioService;
    }

    @Transactional(readOnly = true)
    public List<PosComercio> findAll() {
        log.info("Obteniendo todos los POS comercio");
        return this.posComercioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PosComercio findById(String codigoPos) {
        log.info("Buscando POS comercio con código: {}", codigoPos);
        return this.posComercioRepository.findById(codigoPos)
                .orElseThrow(() -> new PosComercioNotFoundException(codigoPos));
    }

    @Transactional
    public PosComercio create(PosComercio posComercio) {
        log.info("Creando nuevo POS comercio: {}", posComercio);
        Comercio comercio = this.comercioService.findById(posComercio.getComercio().getCodigoComercio());
        posComercio.setComercio(comercio);
        posComercio.setFechaActivacion(LocalDateTime.now());
        return this.posComercioRepository.save(posComercio);
    }

    @Transactional
    public PosComercio update(String codigoPos, PosComercio posComercio) {
        log.info("Actualizando POS comercio con código: {}", codigoPos);
        PosComercio posExistente = findById(codigoPos);
        Comercio comercio = this.comercioService.findById(posComercio.getComercio().getCodigoComercio());
        posComercio.setCodigoPos(codigoPos);
        posComercio.setComercio(comercio);
        posComercio.setFechaActivacion(posExistente.getFechaActivacion());
        return this.posComercioRepository.save(posComercio);
    }

    @Transactional
    public void delete(String codigoPos) {
        log.info("Eliminando POS comercio con código: {}", codigoPos);
        PosComercio posComercio = findById(codigoPos);
        this.posComercioRepository.delete(posComercio);
    }

    @Transactional(readOnly = true)
    public List<PosComercio> findByComercio(String codigoComercio) {
        log.info("Buscando POS por código de comercio: {}", codigoComercio);
        return this.posComercioRepository.findByComercioCodigoComercio(codigoComercio);
    }

    @Transactional(readOnly = true)
    public List<PosComercio> findByEstado(String estado) {
        log.info("Buscando POS por estado: {}", estado);
        return this.posComercioRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<PosComercio> findByDireccionMac(String direccionMac) {
        log.info("Buscando POS por dirección MAC: {}", direccionMac);
        return this.posComercioRepository.findByDireccionMac(direccionMac);
    }

    @Transactional
    public void actualizarUltimoUso(String codigoPos) {
        log.info("Actualizando último uso del POS con código: {}", codigoPos);
        PosComercio posComercio = findById(codigoPos);
        posComercio.setUltimoUso(LocalDateTime.now());
        this.posComercioRepository.save(posComercio);
    }
} 