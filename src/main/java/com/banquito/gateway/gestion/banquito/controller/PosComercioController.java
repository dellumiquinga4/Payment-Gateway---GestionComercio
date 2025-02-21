package com.banquito.gateway.gestion.banquito.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.banquito.gateway.gestion.banquito.controller.dto.PosComercioDTO;
import com.banquito.gateway.gestion.banquito.controller.mapper.PosComercioMapper;
import com.banquito.gateway.gestion.banquito.service.PosComercioService;
import com.banquito.gateway.gestion.banquito.exception.PosComercioNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/pos-comercios")
@Tag(name = "POS Comercio", description = "API para gestionar POS de comercios")
@Slf4j
public class PosComercioController {

    private final PosComercioService posComercioService;
    private final PosComercioMapper posComercioMapper;

    public PosComercioController(PosComercioService posComercioService, PosComercioMapper posComercioMapper) {
        this.posComercioService = posComercioService;
        this.posComercioMapper = posComercioMapper;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los POS", description = "Retorna una lista de todos los POS registrados")
    @ApiResponse(responseCode = "200", description = "Lista de POS obtenida exitosamente")
    public ResponseEntity<List<PosComercioDTO>> getAllPosComercio() {
        return ResponseEntity.ok(
            this.posComercioService.findAll().stream()
                .map(posComercioMapper::toDTO)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{codigoPos}")
    @Operation(summary = "Obtener POS por código", description = "Retorna un POS específico por su código")
    @ApiResponse(responseCode = "200", description = "POS encontrado")
    @ApiResponse(responseCode = "404", description = "POS no encontrado")
    public ResponseEntity<PosComercioDTO> getPosComercioById(
            @Parameter(description = "Código del POS", required = true)
            @PathVariable String codigoPos) {
        return ResponseEntity.ok(
            this.posComercioMapper.toDTO(
                this.posComercioService.findById(codigoPos)
            )
        );
    }

    @PostMapping
    @Operation(summary = "Crear nuevo POS", description = "Crea un nuevo POS con los datos proporcionados")
    @ApiResponse(responseCode = "200", description = "POS creado exitosamente")
    public ResponseEntity<PosComercioDTO> createPosComercio(
            @Parameter(description = "Datos del POS", required = true)
            @Valid @RequestBody PosComercioDTO posComercioDTO) {
        return ResponseEntity.ok(
            this.posComercioMapper.toDTO(
                this.posComercioService.create(
                    this.posComercioMapper.toModel(posComercioDTO)
                )
            )
        );
    }

    @PutMapping("/{codigoPos}")
    @Operation(summary = "Actualizar POS", description = "Actualiza los datos de un POS existente")
    @ApiResponse(responseCode = "200", description = "POS actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "POS no encontrado")
    public ResponseEntity<PosComercioDTO> updatePosComercio(
            @Parameter(description = "Código del POS", required = true)
            @PathVariable String codigoPos,
            @Parameter(description = "Datos actualizados del POS", required = true)
            @Valid @RequestBody PosComercioDTO posComercioDTO) {
        return ResponseEntity.ok(
            this.posComercioMapper.toDTO(
                this.posComercioService.update(
                    codigoPos,
                    this.posComercioMapper.toModel(posComercioDTO)
                )
            )
        );
    }

    @DeleteMapping("/{codigoPos}")
    @Operation(summary = "Eliminar POS", description = "Elimina un POS existente")
    @ApiResponse(responseCode = "204", description = "POS eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "POS no encontrado")
    public ResponseEntity<Void> deletePosComercio(
            @Parameter(description = "Código del POS", required = true)
            @PathVariable String codigoPos) {
        this.posComercioService.delete(codigoPos);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comercio/{codigoComercio}")
    @Operation(summary = "Buscar POS por comercio", description = "Retorna una lista de POS que pertenecen al comercio especificado")
    @ApiResponse(responseCode = "200", description = "Lista de POS obtenida exitosamente")
    public ResponseEntity<List<PosComercioDTO>> getPosByComercio(
            @Parameter(description = "Código del comercio", required = true)
            @PathVariable String codigoComercio) {
        return ResponseEntity.ok(
            this.posComercioService.findByComercio(codigoComercio).stream()
                .map(posComercioMapper::toDTO)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar POS por estado", description = "Retorna una lista de POS que coinciden con el estado especificado")
    @ApiResponse(responseCode = "200", description = "Lista de POS obtenida exitosamente")
    public ResponseEntity<List<PosComercioDTO>> getPosByEstado(
            @Parameter(description = "Estado del POS (ACT, INA)", required = true)
            @PathVariable String estado) {
        return ResponseEntity.ok(
            this.posComercioService.findByEstado(estado).stream()
                .map(posComercioMapper::toDTO)
                .collect(Collectors.toList())
        );
    }

    @PatchMapping("/{codigoPos}/ultimo-uso")
    @Operation(summary = "Actualizar último uso", description = "Actualiza la fecha de último uso de un POS")
    @ApiResponse(responseCode = "204", description = "Último uso actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "POS no encontrado")
    public ResponseEntity<Void> actualizarUltimoUso(
            @Parameter(description = "Código del POS", required = true)
            @PathVariable String codigoPos) {
        this.posComercioService.actualizarUltimoUso(codigoPos);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(PosComercioNotFoundException.class)
    public ResponseEntity<Void> handlePosComercioNotFound() {
        return ResponseEntity.notFound().build();
    }
} 