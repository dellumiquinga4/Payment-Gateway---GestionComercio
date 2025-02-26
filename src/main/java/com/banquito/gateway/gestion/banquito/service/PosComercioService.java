package com.banquito.gateway.gestion.banquito.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.banquito.gateway.gestion.banquito.model.PosComercio;
import com.banquito.gateway.gestion.banquito.model.Comercio;
import com.banquito.gateway.gestion.banquito.repository.PosComercioRepository;
import com.banquito.gateway.gestion.banquito.exception.PosComercioNotFoundException;
import com.banquito.gateway.gestion.banquito.exception.BusinessException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class PosComercioService {

    private static final int MAX_POS_POR_COMERCIO = 10;
    private static final int DIAS_INACTIVIDAD_MAXIMO = 90;

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
        if (!"ACT".equals(comercio.getEstado())) {
            throw new BusinessException("No se puede crear un POS para un comercio inactivo");
        }

        
        List<PosComercio> posExistentes = findByComercio(comercio.getCodigoComercio());
        if (posExistentes.size() >= MAX_POS_POR_COMERCIO) {
            throw new BusinessException("El comercio ha alcanzado el límite máximo de " + MAX_POS_POR_COMERCIO + " POS");
        }

        
        List<PosComercio> posConMismaMac = this.posComercioRepository.findByDireccionMac(posComercio.getDireccionMac());
        if (!posConMismaMac.isEmpty()) {
            throw new BusinessException("Ya existe un POS registrado con la dirección MAC proporcionada");
        }

        posComercio.setComercio(comercio);
        posComercio.setFechaActivacion(LocalDateTime.now());
        posComercio.setEstado("ACT");
        posComercio.setUltimoUso(LocalDateTime.now());

        return this.posComercioRepository.save(posComercio);
    }

    @Transactional
    public PosComercio update(String codigoPos, PosComercio posComercio) {
        log.info("Actualizando POS comercio con código: {}", codigoPos);
        
        PosComercio posExistente = findById(codigoPos);
        Comercio comercio = this.comercioService.findById(posComercio.getComercio().getCodigoComercio());

        
        if (!"ACT".equals(comercio.getEstado())) {
            throw new BusinessException("No se puede actualizar un POS para un comercio inactivo");
        }

        
        if (!posExistente.getComercio().getCodigoComercio().equals(comercio.getCodigoComercio())) {
            throw new BusinessException("No se permite cambiar el comercio asociado al POS");
        }

        
        if (!posExistente.getDireccionMac().equals(posComercio.getDireccionMac())) {
            List<PosComercio> posConMismaMac = this.posComercioRepository.findByDireccionMac(posComercio.getDireccionMac());
            if (!posConMismaMac.isEmpty()) {
                throw new BusinessException("Ya existe un POS registrado con la dirección MAC proporcionada");
            }
        }

        
        if ("ACT".equals(posComercio.getEstado()) && "INA".equals(posExistente.getEstado())) {
            posComercio.setFechaActivacion(LocalDateTime.now());
            posComercio.setUltimoUso(LocalDateTime.now());
        } else {
            posComercio.setFechaActivacion(posExistente.getFechaActivacion());
            posComercio.setUltimoUso(posExistente.getUltimoUso());
        }

        posComercio.setCodigoPos(codigoPos);
        posComercio.setComercio(comercio);
        
        return this.posComercioRepository.save(posComercio);
    }

    @Transactional
    public void delete(String codigoPos) {
        log.info("Eliminando POS comercio con código: {}", codigoPos);
        PosComercio posComercio = findById(codigoPos);
        this.posComercioRepository.delete(posComercio);
    }

    @Transactional
    public void actualizarUltimoUso(String codigoPos) {
        log.info("Actualizando último uso del POS con código: {}", codigoPos);
        PosComercio posComercio = findById(codigoPos);
        
        if (!"ACT".equals(posComercio.getEstado())) {
            throw new BusinessException("No se puede actualizar el último uso de un POS inactivo");
        }

        posComercio.setUltimoUso(LocalDateTime.now());
        this.posComercioRepository.save(posComercio);
    }

    @Transactional
    public void verificarInactividad() {
        log.info("Verificando inactividad de POS");
        List<PosComercio> posActivos = this.posComercioRepository.findByEstado("ACT");
        LocalDateTime ahora = LocalDateTime.now();

        for (PosComercio pos : posActivos) {
            if (pos.getUltimoUso() != null) {
                long diasInactivo = ChronoUnit.DAYS.between(pos.getUltimoUso(), ahora);
                if (diasInactivo > DIAS_INACTIVIDAD_MAXIMO) {
                    log.info("Desactivando POS {} por inactividad", pos.getCodigoPos());
                    pos.setEstado("INA");
                    this.posComercioRepository.save(pos);
                }
            }
        }
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
} 