package com.banquito.gateway.gestion.banquito.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.banquito.gateway.gestion.banquito.controller.dto.ComercioDTO;
import com.banquito.gateway.gestion.banquito.controller.mapper.ComercioMapper;
import com.banquito.gateway.gestion.banquito.service.ComercioService;
import com.banquito.gateway.gestion.banquito.exception.ComercioNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/comercios")
@Tag(name = "Comercio", description = "API para gestionar comercios")
@Slf4j
public class ComercioController {

    private final ComercioService comercioService;
    private final ComercioMapper comercioMapper;

    public ComercioController(ComercioService comercioService, ComercioMapper comercioMapper) {
        this.comercioService = comercioService;
        this.comercioMapper = comercioMapper;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los comercios", description = "Retorna una lista de todos los comercios registrados")
    @ApiResponse(responseCode = "200", description = "Lista de comercios obtenida exitosamente")
    public ResponseEntity<List<ComercioDTO>> getAllComercios() {
        return ResponseEntity.ok(
            this.comercioService.findAll().stream()
                .map(comercioMapper::toDTO)
                .collect(Collectors.toList())
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
    @Operation(summary = "Crear nuevo comercio", description = "Crea un nuevo comercio con los datos proporcionados")
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

    @PutMapping("/{codigoComercio}")
    @Operation(summary = "Actualizar comercio", description = "Actualiza los datos de un comercio existente")
    @ApiResponse(responseCode = "200", description = "Comercio actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Comercio no encontrado")
    public ResponseEntity<ComercioDTO> updateComercio(
            @Parameter(description = "Código del comercio", required = true)
            @PathVariable String codigoComercio,
            @Parameter(description = "Datos actualizados del comercio", required = true)
            @Valid @RequestBody ComercioDTO comercioDTO) {
        return ResponseEntity.ok(
            this.comercioMapper.toDTO(
                this.comercioService.update(
                    codigoComercio,
                    this.comercioMapper.toModel(comercioDTO)
                )
            )
        );
    }

    @DeleteMapping("/{codigoComercio}")
    @Operation(summary = "Eliminar comercio", description = "Elimina un comercio existente")
    @ApiResponse(responseCode = "204", description = "Comercio eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Comercio no encontrado")
    public ResponseEntity<Void> deleteComercio(
            @Parameter(description = "Código del comercio", required = true)
            @PathVariable String codigoComercio) {
        this.comercioService.delete(codigoComercio);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar comercios por estado", description = "Retorna una lista de comercios que coinciden con el estado especificado")
    @ApiResponse(responseCode = "200", description = "Lista de comercios obtenida exitosamente")
    public ResponseEntity<List<ComercioDTO>> getComerciosByEstado(
            @Parameter(description = "Estado del comercio (ACT, INA, SUS)", required = true)
            @PathVariable String estado) {
        return ResponseEntity.ok(
            this.comercioService.findByEstado(estado).stream()
                .map(comercioMapper::toDTO)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/ruc/{ruc}")
    @Operation(summary = "Buscar comercios por RUC", description = "Retorna una lista de comercios que coinciden con el RUC especificado")
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

    @ExceptionHandler(ComercioNotFoundException.class)
    public ResponseEntity<Void> handleComercioNotFound() {
        return ResponseEntity.notFound().build();
    }
} 