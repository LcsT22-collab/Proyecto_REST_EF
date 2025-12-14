package com.clinica.controller;

import com.clinica.model.dto.TerapeutaDTO;
import com.clinica.model.entity.DisciplinaEntity;
import com.clinica.model.entity.TerapeutaEntity;
import com.clinica.service.TerapeutaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/terapeutas")
@Tag(name = "Terapeutas", description = "Gestión de terapeutas")
public class TerapeutaController {

    private final TerapeutaService terapeutaService;

    public TerapeutaController(TerapeutaService terapeutaService) {
        this.terapeutaService = terapeutaService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los terapeutas", description = "Obtiene la lista completa de terapeutas")
    @ApiResponse(responseCode = "200", description = "Lista de terapeutas obtenida exitosamente")
    public ResponseEntity<List<TerapeutaDTO>> getAllTerapeutas() {
        List<TerapeutaDTO> terapeutas = terapeutaService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(terapeutas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener terapeuta por ID", description = "Busca un terapeuta específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Terapeuta encontrado"),
            @ApiResponse(responseCode = "404", description = "Terapeuta no encontrado")
    })
    public ResponseEntity<TerapeutaDTO> getTerapeutaById(
            @Parameter(description = "ID del terapeuta", required = true) @PathVariable Long id) {
        return terapeutaService.findById(id)
                .map(terapeuta -> ResponseEntity.ok(convertToDTO(terapeuta)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo terapeuta", description = "Crea un nuevo registro de terapeuta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Terapeuta creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<TerapeutaDTO> createTerapeuta(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del terapeuta", required = true)
            @RequestBody TerapeutaDTO terapeutaDTO) {
        TerapeutaEntity terapeuta = convertToEntity(terapeutaDTO);
        TerapeutaEntity savedTerapeuta = terapeutaService.save(terapeuta);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedTerapeuta));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar terapeuta", description = "Actualiza los datos de un terapeuta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Terapeuta actualizado"),
            @ApiResponse(responseCode = "404", description = "Terapeuta no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<TerapeutaDTO> updateTerapeuta(
            @Parameter(description = "ID del terapeuta", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del terapeuta", required = true)
            @RequestBody TerapeutaDTO terapeutaDTO) {
        TerapeutaEntity terapeuta = convertToEntity(terapeutaDTO);
        TerapeutaEntity updatedTerapeuta = terapeutaService.update(id, terapeuta);
        return ResponseEntity.ok(convertToDTO(updatedTerapeuta));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar terapeuta", description = "Desactiva un terapeuta (marca como inactivo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Terapeuta desactivado"),
            @ApiResponse(responseCode = "404", description = "Terapeuta no encontrado"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar (tiene citas programadas)")
    })
    public ResponseEntity<Void> deleteTerapeuta(
            @Parameter(description = "ID del terapeuta", required = true) @PathVariable Long id) {
        terapeutaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TerapeutaDTO convertToDTO(TerapeutaEntity terapeuta) {
        TerapeutaDTO dto = new TerapeutaDTO();
        dto.setIdTerapeuta(terapeuta.getIdTerapeuta());
        dto.setNombre(terapeuta.getNombre());
        dto.setEspecialidad(terapeuta.getEspecialidad());
        dto.setDisponibilidad(terapeuta.getDisponibilidad());
        dto.setCodigoLicencia(terapeuta.getCodigoLicencia());
        dto.setTelefono(terapeuta.getTelefono());
        dto.setCorreo(terapeuta.getCorreo());
        dto.setActivo(terapeuta.getActivo());
        
        if (terapeuta.getDisciplina() != null) {
            dto.setDisciplinaId(terapeuta.getDisciplina().getIdDisciplina());
            dto.setNombreDisciplina(terapeuta.getDisciplina().getNombre());
        }
        
        return dto;
    }

    private TerapeutaEntity convertToEntity(TerapeutaDTO dto) {
        TerapeutaEntity terapeuta = new TerapeutaEntity();
        terapeuta.setNombre(dto.getNombre());
        terapeuta.setEspecialidad(dto.getEspecialidad());
        terapeuta.setDisponibilidad(dto.getDisponibilidad());
        terapeuta.setCodigoLicencia(dto.getCodigoLicencia());
        terapeuta.setTelefono(dto.getTelefono());
        terapeuta.setCorreo(dto.getCorreo());
        terapeuta.setActivo(dto.getActivo());
        
        if (dto.getDisciplinaId() != null) {
            DisciplinaEntity disciplina = new DisciplinaEntity();
            disciplina.setIdDisciplina(dto.getDisciplinaId());
            terapeuta.setDisciplina(disciplina);
        }
        
        return terapeuta;
    }
}