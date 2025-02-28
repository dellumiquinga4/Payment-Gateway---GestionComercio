package com.banquito.gateway.gestion.banquito.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private static final Pattern IBAN_PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$");
    private static final int MIN_MONTO_TRANSACCION = 1;
    private static final int MAX_MONTO_TRANSACCION = 10000;

    private final ComercioRepository comercioRepository;

    public ComercioService(ComercioRepository comercioRepository) {
        this.comercioRepository = comercioRepository;
    }

    @Transactional(readOnly = true)
    public Page<Comercio> findAll(Pageable pageable) {
        log.info("Obteniendo todos los comercios paginados");
        return this.comercioRepository.findAll(pageable);
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
        
        validarRuc(comercio.getRuc());
        validarSwiftBanco(comercio.getSwiftBanco());
        validarIban(comercio.getCuentaIban());
        validarComercioExistente(comercio.getRuc());

        comercio.setFechaCreacion(LocalDateTime.now());
        comercio.setFechaActivacion(LocalDateTime.now());
        comercio.setEstado("ACT");
        return this.comercioRepository.save(comercio);
    }

    @Transactional
    public Comercio suspender(String codigoComercio) {
        log.info("Suspendiendo comercio con código: {}", codigoComercio);
        Comercio comercio = findById(codigoComercio);
        
        if ("SUS".equals(comercio.getEstado())) {
            throw new BusinessException("El comercio ya se encuentra suspendido");
        }

        comercio.setEstado("SUS");
        comercio.setFechaSuspension(LocalDateTime.now());
        return this.comercioRepository.save(comercio);
    }

    @Transactional(readOnly = true)
    public List<Comercio> findByNombreComercial(String nombreComercial) {
        log.info("Buscando comercios por nombre comercial: {}", nombreComercial);
        return this.comercioRepository.findByNombreComercialContainingIgnoreCase(nombreComercial);
    }

    @Transactional(readOnly = true)
    public Comercio findByCuentaIban(String cuentaIban) {
        log.info("Buscando comercio por cuenta IBAN: {}", cuentaIban);
        validarIban(cuentaIban);
        return this.comercioRepository.findByCuentaIban(cuentaIban)
                .orElseThrow(() -> new ComercioNotFoundException("cuenta IBAN: " + cuentaIban));
    }

    @Transactional(readOnly = true)
    public List<Comercio> findByRuc(String ruc) {
        log.info("Buscando comercios por RUC: {}", ruc);
        validarRuc(ruc);
        return this.comercioRepository.findByRuc(ruc);
    }

    @Transactional(readOnly = true)
    public List<Comercio> findByEstado(String estado) {
        log.info("Buscando comercios por estado: {}", estado);
        return this.comercioRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Comercio> findBySwiftBanco(String swiftBanco) {
        log.info("Buscando comercios por código SWIFT del banco: {}", swiftBanco);
        return this.comercioRepository.findBySwiftBanco(swiftBanco);
    }

    private void validarRuc(String ruc) {
        if (!RUC_PATTERN.matcher(ruc).matches()) {
            throw new BusinessException("El RUC debe tener exactamente 13 dígitos numéricos");
        }
    }

    private void validarSwiftBanco(String swiftBanco) {
        if (!SWIFT_PATTERN.matcher(swiftBanco).matches()) {
            throw new BusinessException("El código SWIFT del banco no tiene un formato válido");
        }
    }

    private void validarIban(String iban) {
        if (iban != null && !iban.isEmpty() && !IBAN_PATTERN.matcher(iban).matches()) {
            throw new BusinessException("El IBAN no tiene un formato válido");
        }
    }

    private void validarComercioExistente(String ruc) {
        List<Comercio> comerciosExistentes = this.comercioRepository.findByRuc(ruc);
        if (!comerciosExistentes.isEmpty()) {
            throw new BusinessException("Ya existe un comercio registrado con el RUC proporcionado");
        }
    }
} 