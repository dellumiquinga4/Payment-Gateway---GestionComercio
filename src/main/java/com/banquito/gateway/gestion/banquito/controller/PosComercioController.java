package com.banquito.gateway.gestion.banquito.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@Tag(name = "POS Comercio", description = "API para gestionar POS de comercios en el payment gateway")
@Slf4j
public class PosComercioController {

    private final PosComercioService posComercioService;
    private final PosComercioMapper posComercioMapper;

    public PosComercioController(PosComercioService posComercioService, PosComercioMapper posComercioMapper) {
        this.posComercioService = posComercioService;
        this.posComercioMapper = posComercioMapper;
    }

    @GetMapping
    @Operation(summary = "Listar POS", description = "Obtiene una lista paginada de todos los POS")
    @ApiResponse(responseCode = "200", description = "Lista de POS obtenida exitosamente")
    public ResponseEntity<Page<PosComercioDTO>> getAllPosComercio(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(
            this.posComercioService.findAll(pageable)
                .map(posComercioMapper::toDTO)
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
    @Operation(summary = "Asignar POS", description = "Asigna un nuevo POS a un comercio")
    @ApiResponse(responseCode = "200", description = "POS asignado exitosamente")
    public ResponseEntity<PosComercioDTO> asignarPosComercio(
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

    @DeleteMapping("/{codigoPos}")
    @Operation(summary = "Remover POS", description = "Remueve un POS de un comercio")
    @ApiResponse(responseCode = "204", description = "POS removido exitosamente")
    @ApiResponse(responseCode = "404", description = "POS no encontrado")
    public ResponseEntity<Void> removerPosComercio(
            @Parameter(description = "Código del POS", required = true)
            @PathVariable String codigoPos) {
        this.posComercioService.delete(codigoPos);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comercio/{codigoComercio}")
    @Operation(summary = "Listar POS por comercio", description = "Retorna una lista paginada de POS que pertenecen al comercio especificado")
    @ApiResponse(responseCode = "200", description = "Lista de POS obtenida exitosamente")
    public ResponseEntity<Page<PosComercioDTO>> getPosByComercio(
            @Parameter(description = "Código del comercio", required = true)
            @PathVariable String codigoComercio,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(
            this.posComercioService.findByComercio(codigoComercio, pageable)
                .map(posComercioMapper::toDTO)
        );
    }

    @GetMapping("/modelo/{modelo}")
    @Operation(summary = "Buscar POS por modelo", description = "Retorna una lista de POS que coinciden con el modelo especificado")
    @ApiResponse(responseCode = "200", description = "Lista de POS obtenida exitosamente")
    public ResponseEntity<List<PosComercioDTO>> getPosByModelo(
            @Parameter(description = "Modelo del POS", required = true)
            @PathVariable String modelo) {
        return ResponseEntity.ok(
            this.posComercioService.findByModelo(modelo).stream()
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