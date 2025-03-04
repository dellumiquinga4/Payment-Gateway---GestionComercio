package com.banquito.gateway.gestion.banquito.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;

import com.banquito.gateway.gestion.banquito.model.PosComercio;
import com.banquito.gateway.gestion.banquito.model.Comercio;
import com.banquito.gateway.gestion.banquito.repository.PosComercioRepository;
import com.banquito.gateway.gestion.banquito.exception.PosComercioNotFoundException;
import com.banquito.gateway.gestion.banquito.exception.BusinessException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class PosComercioService {

    private static final int MAX_POS_POR_COMERCIO = 10;
    private static final int DIAS_INACTIVIDAD_MAXIMO = 90;
    private static final Pattern MAC_PATTERN = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");

    private final PosComercioRepository posComercioRepository;
    private final ComercioService comercioService;

    public PosComercioService(PosComercioRepository posComercioRepository, ComercioService comercioService) {
        this.posComercioRepository = posComercioRepository;
        this.comercioService = comercioService;
    }

    @Transactional(readOnly = true)
    public Page<PosComercio> findAll(Pageable pageable) {
        log.info("Obteniendo todos los POS comercio paginados");
        return this.posComercioRepository.findAll(pageable);
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
        
        
        if (this.posComercioRepository.findById(posComercio.getCodigoPos()).isPresent()) {
            throw new BusinessException("Ya existe un POS con el código: " + posComercio.getCodigoPos());
        }
        
        
        Comercio comercio = this.comercioService.findById(posComercio.getComercio().getCodigoComercio());
        log.info("Comercio encontrado: {}", comercio);
        
        
        validarComercioActivo(comercio);
        validarLimitePosComercio(comercio.getCodigoComercio());
        validarDireccionMac(posComercio.getDireccionMac());
        validarMacUnica(posComercio.getDireccionMac());

        
        posComercio.setFechaActivacion(LocalDateTime.now());
        posComercio.setEstado("ACT");
        posComercio.setUltimoUso(LocalDateTime.now());
        posComercio.setComercio(comercio);

        try {
            return this.posComercioRepository.save(posComercio);
        } catch (Exception e) {
            log.error("Error al guardar el POS: {}", e.getMessage());
            throw new BusinessException("Error al guardar el POS: " + e.getMessage());
        }
    }

    @Transactional
    public void delete(String codigoPos) {
        log.info("Eliminando POS comercio con código: {}", codigoPos);
        PosComercio posComercio = findById(codigoPos);
        
        if ("ACT".equals(posComercio.getEstado())) {
            throw new BusinessException("No se puede eliminar un POS activo. Debe inactivarlo primero");
        }
        
        this.posComercioRepository.delete(posComercio);
    }

    @Transactional(readOnly = true)
    public Page<PosComercio> findByComercio(String codigoComercio, Pageable pageable) {
        log.info("Buscando POS por código de comercio: {}", codigoComercio);
        return this.posComercioRepository.findByComercioCodigoComercio(codigoComercio, pageable);
    }

    @Transactional(readOnly = true)
    public List<PosComercio> findByModelo(String modelo) {
        log.info("Buscando POS por modelo: {}", modelo);
        return this.posComercioRepository.findByModelo(modelo);
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

    @Transactional
    public PosComercio update(PosComercio posComercio) {
        log.info("Actualizando POS comercio: {}", posComercio);
        
        
        Comercio comercio = this.comercioService.findById(posComercio.getComercio().getCodigoComercio());
        validarComercioActivo(comercio);
        
       
        
        try {
            return this.posComercioRepository.save(posComercio);
        } catch (Exception e) {
            log.error("Error al actualizar el POS: {}", e.getMessage());
            throw new BusinessException("Error al actualizar el POS: " + e.getMessage());
        }
    }

    @Transactional
    public PosComercio actualizarEstado(String codigoPos, String nuevoEstado) {
        log.info("Actualizando estado del POS {} a {}", codigoPos, nuevoEstado);
        
        if (!nuevoEstado.equals("ACT") && !nuevoEstado.equals("INA")) {
            throw new BusinessException("El estado debe ser ACT o INA");
        }

        PosComercio posComercio = findById(codigoPos);
        
        if (nuevoEstado.equals(posComercio.getEstado())) {
            throw new BusinessException("El POS ya se encuentra en estado: " + nuevoEstado);
        }

        posComercio.setEstado(nuevoEstado);
        
        if (nuevoEstado.equals("ACT")) {
            posComercio.setFechaActivacion(LocalDateTime.now());
        }

        try {
            return this.posComercioRepository.save(posComercio);
        } catch (Exception e) {
            log.error("Error al actualizar el estado del POS: {}", e.getMessage());
            throw new BusinessException("Error al actualizar el estado del POS: " + e.getMessage());
        }
    }

    private void validarComercioActivo(Comercio comercio) {
        if (!"ACT".equals(comercio.getEstado())) {
            throw new BusinessException("No se puede crear un POS para un comercio inactivo o suspendido");
        }
    }

    private void validarLimitePosComercio(String codigoComercio) {
        List<PosComercio> posExistentes = this.posComercioRepository.findByComercioCodigoComercio(codigoComercio);
        if (posExistentes.size() >= MAX_POS_POR_COMERCIO) {
            throw new BusinessException("El comercio ha alcanzado el límite máximo de " + MAX_POS_POR_COMERCIO + " POS");
        }
    }

    private void validarDireccionMac(String direccionMac) {
        if (!MAC_PATTERN.matcher(direccionMac).matches()) {
            throw new BusinessException("La dirección MAC no tiene un formato válido");
        }
    }

    private void validarMacUnica(String direccionMac) {
        List<PosComercio> posConMismaMac = this.posComercioRepository.findByDireccionMac(direccionMac);
        if (!posConMismaMac.isEmpty()) {
            throw new BusinessException("Ya existe un POS registrado con la dirección MAC proporcionada");
        }
    }
} 