package com.banquito.gateway.gestion.banquito.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.banquito.gateway.gestion.banquito.controller.dto.ComercioDTO;
import com.banquito.gateway.gestion.banquito.controller.dto.ComercioInfoDTO;
import com.banquito.gateway.gestion.banquito.controller.mapper.ComercioMapper;
import com.banquito.gateway.gestion.banquito.service.ComercioService;
import com.banquito.gateway.gestion.banquito.service.PosComercioService;
import com.banquito.gateway.gestion.banquito.exception.ComercioNotFoundException;
import com.banquito.gateway.gestion.banquito.model.Comercio;
import com.banquito.gateway.gestion.banquito.model.PosComercio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/comercios")
@Tag(name = "Comercio", description = "API para gestionar comercios en el payment gateway")
@Slf4j
public class ComercioController {

    private final ComercioService comercioService;
    private final ComercioMapper comercioMapper;
    private final PosComercioService posComercioService;

    public ComercioController(ComercioService comercioService, ComercioMapper comercioMapper, PosComercioService posComercioService) {
        this.comercioService = comercioService;
        this.comercioMapper = comercioMapper;
        this.posComercioService = posComercioService;
    }

    @GetMapping
    @Operation(summary = "Listar comercios", description = "Obtiene una lista paginada de todos los comercios")
    @ApiResponse(responseCode = "200", description = "Lista de comercios obtenida exitosamente")
    public ResponseEntity<Page<ComercioDTO>> getAllComercios(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(
            this.comercioService.findAll(pageable)
                .map(comercioMapper::toDTO)
        );
    }

    @GetMapping("/{codigoComercio}")
    @Operation(summary = "Obtener comercio por código", description = "Retorna un comercio específico por su código")
    @ApiResponse(responseCode = "200", description = "Comercio encontrado")
    @ApiResponse(responseCode = "404", description = "Comercio no encontrado")
    public ResponseEntity<ComercioDTO> getComercioById(
            @Parameter(description = "Código del comercio", required = true)
            @PathVariable String codigoComercio) {
        return ResponseEntity.ok(
            this.comercioMapper.toDTO(
                this.comercioService.findById(codigoComercio)
            )
        );
    }

    @PostMapping
    @Operation(summary = "Crear comercio", description = "Crea un nuevo comercio en el payment gateway")
    @ApiResponse(responseCode = "200", description = "Comercio creado exitosamente")
    public ResponseEntity<ComercioDTO> createComercio(
            @Parameter(description = "Datos del comercio", required = true)
            @Valid @RequestBody ComercioDTO comercioDTO) {
        return ResponseEntity.ok(
            this.comercioMapper.toDTO(
                this.comercioService.create(
                    this.comercioMapper.toModel(comercioDTO)
                )
            )
        );
    }

    @GetMapping("/nombre/{nombreComercial}")
    @Operation(summary = "Buscar comercios por nombre", description = "Retorna una lista de comercios que coinciden con el nombre comercial")
    @ApiResponse(responseCode = "200", description = "Lista de comercios obtenida exitosamente")
    public ResponseEntity<List<ComercioDTO>> getComerciosByNombre(
            @Parameter(description = "Nombre comercial a buscar", required = true)
            @PathVariable String nombreComercial) {
        return ResponseEntity.ok(
            this.comercioService.findByNombreComercial(nombreComercial).stream()
                .map(comercioMapper::toDTO)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/iban/{cuentaIban}")
    @Operation(summary = "Buscar comercio por IBAN", description = "Retorna un comercio que coincide con la cuenta IBAN")
    @ApiResponse(responseCode = "200", description = "Comercio encontrado")
    @ApiResponse(responseCode = "404", description = "Comercio no encontrado")
    public ResponseEntity<ComercioDTO> getComercioByIban(
            @Parameter(description = "Cuenta IBAN a buscar", required = true)
            @PathVariable String cuentaIban) {
        return ResponseEntity.ok(
            this.comercioMapper.toDTO(
                this.comercioService.findByCuentaIban(cuentaIban)
            )
        );
    }

    @PatchMapping("/{codigoComercio}/suspension")
    @Operation(summary = "Suspender comercio", description = "Suspende un comercio existente")
    @ApiResponse(responseCode = "200", description = "Comercio suspendido exitosamente")
    @ApiResponse(responseCode = "404", description = "Comercio no encontrado")
    public ResponseEntity<ComercioDTO> suspenderComercio(
            @Parameter(description = "Código del comercio", required = true)
            @PathVariable String codigoComercio) {
        return ResponseEntity.ok(
            this.comercioMapper.toDTO(
                this.comercioService.suspender(codigoComercio)
            )
        );
    }

    @GetMapping("/ruc/{ruc}")
    @Operation(summary = "Buscar comercio por RUC", description = "Retorna una lista de comercios que coinciden con el RUC")
    @ApiResponse(responseCode = "200", description = "Lista de comercios obtenida exitosamente")
    public ResponseEntity<List<ComercioDTO>> getComerciosByRuc(
            @Parameter(description = "RUC del comercio", required = true)
            @PathVariable String ruc) {
        return ResponseEntity.ok(
            this.comercioService.findByRuc(ruc).stream()
                .map(comercioMapper::toDTO)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/pos/{codigoPos}")
    @Operation(summary = "Obtener información del comercio por POS", description = "Retorna información específica del comercio asociado al POS")
    @ApiResponse(responseCode = "200", description = "Información del comercio obtenida exitosamente")
    @ApiResponse(responseCode = "404", description = "POS o comercio no encontrado")
    public ResponseEntity<ComercioInfoDTO> getComercioInfoByPos(
            @Parameter(description = "Código del POS", required = true)
            @PathVariable String codigoPos) {
        log.info("Obteniendo información del comercio para el POS: {}", codigoPos);
        
        PosComercio pos = this.posComercioService.findById(codigoPos);
        Comercio comercio = pos.getComercio();
        
        ComercioInfoDTO dto = new ComercioInfoDTO();
        dto.setCodigo_comercio(comercio.getCodigoComercio());
        dto.setNombre_comercio(comercio.getNombreComercial());
        dto.setSwift_banco(comercio.getSwiftBanco());
        dto.setCuenta_iban(comercio.getCuentaIban());
        dto.setEstado(comercio.getEstado().equals("ACT") ? "ACTIVO" : "INACTIVO");
        
        return ResponseEntity.ok(dto);
    }

    @ExceptionHandler(ComercioNotFoundException.class)
    public ResponseEntity<Void> handleComercioNotFound() {
        return ResponseEntity.notFound().build();
    }
} 