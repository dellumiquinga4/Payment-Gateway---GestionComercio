package com.banquito.gateway.gestion.banquito.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.banquito.gateway.gestion.banquito.model.Comercio;
import com.banquito.gateway.gestion.banquito.repository.ComercioRepository;
import com.banquito.gateway.gestion.banquito.exception.ComercioNotFoundException;
import com.banquito.gateway.gestion.banquito.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ComercioService {

    private static final Pattern RUC_PATTERN = Pattern.compile("^[0-9]{13}$");
    private static final Pattern SWIFT_PATTERN = Pattern.compile("^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$");
    private static final int MIN_MONTO_TRANSACCION = 1;
    private static final int MAX_MONTO_TRANSACCION = 10000;

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

        
        if (!RUC_PATTERN.matcher(comercio.getRuc()).matches()) {
            throw new BusinessException("El RUC debe tener exactamente 13 dígitos numéricos");
        }

        
        List<Comercio> comerciosExistentes = this.comercioRepository.findByRuc(comercio.getRuc());
        if (!comerciosExistentes.isEmpty()) {
            throw new BusinessException("Ya existe un comercio registrado con el RUC proporcionado");
        }

        
        if (!SWIFT_PATTERN.matcher(comercio.getSwiftBanco()).matches()) {
            throw new BusinessException("El código SWIFT del banco no tiene un formato válido");
        }

        
        if (comercio.getMontoMaximoTransaccion() < comercio.getMontoMinimoTransaccion()) {
            throw new BusinessException("El monto máximo de transacción debe ser mayor al monto mínimo");
        }

        if (comercio.getMontoMinimoTransaccion() < MIN_MONTO_TRANSACCION) {
            throw new BusinessException("El monto mínimo de transacción no puede ser menor a " + MIN_MONTO_TRANSACCION);
        }

        if (comercio.getMontoMaximoTransaccion() > MAX_MONTO_TRANSACCION) {
            throw new BusinessException("El monto máximo de transacción no puede ser mayor a " + MAX_MONTO_TRANSACCION);
        }

        
        if (comercio.getNumeroCuenta() == null || comercio.getNumeroCuenta().trim().isEmpty()) {
            throw new BusinessException("El número de cuenta bancaria es requerido");
        }

        comercio.setFechaCreacion(LocalDateTime.now());
        comercio.setEstado("ACT");
        return this.comercioRepository.save(comercio);
    }

    @Transactional
    public Comercio update(String codigoComercio, Comercio comercio) {
        log.info("Actualizando comercio con código: {}", codigoComercio);
        
        Comercio comercioExistente = findById(codigoComercio);

        
        if (!RUC_PATTERN.matcher(comercio.getRuc()).matches()) {
            throw new BusinessException("El RUC debe tener exactamente 13 dígitos numéricos");
        }

        
        if (!comercioExistente.getRuc().equals(comercio.getRuc())) {
            List<Comercio> comerciosExistentes = this.comercioRepository.findByRuc(comercio.getRuc());
            if (!comerciosExistentes.isEmpty()) {
                throw new BusinessException("Ya existe un comercio registrado con el RUC proporcionado");
            }
        }

        
        if (!SWIFT_PATTERN.matcher(comercio.getSwiftBanco()).matches()) {
            throw new BusinessException("El código SWIFT del banco no tiene un formato válido");
        }

        
        if (comercio.getMontoMaximoTransaccion() < comercio.getMontoMinimoTransaccion()) {
            throw new BusinessException("El monto máximo de transacción debe ser mayor al monto mínimo");
        }

        if (comercio.getMontoMinimoTransaccion() < MIN_MONTO_TRANSACCION) {
            throw new BusinessException("El monto mínimo de transacción no puede ser menor a " + MIN_MONTO_TRANSACCION);
        }

        if (comercio.getMontoMaximoTransaccion() > MAX_MONTO_TRANSACCION) {
            throw new BusinessException("El monto máximo de transacción no puede ser mayor a " + MAX_MONTO_TRANSACCION);
        }

        
        if (comercio.getNumeroCuenta() == null || comercio.getNumeroCuenta().trim().isEmpty()) {
            throw new BusinessException("El número de cuenta bancaria es requerido");
        }

        
        if ("INA".equals(comercio.getEstado()) && "ACT".equals(comercioExistente.getEstado())) {
            log.warn("Se está inactivando el comercio {}. Asegúrese de inactivar todos sus POS asociados.", codigoComercio);
        }

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
        log.info("Buscando comercios por código SWIFT del banco: {}", swiftBanco);
        return this.comercioRepository.findBySwiftBanco(swiftBanco);
    }
} 