package com.clinica.controller;

import com.clinica.model.dto.DisciplinaDTO;
import com.clinica.model.entity.DisciplinaEntity;
import com.clinica.service.DisciplinaService;
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
@RequestMapping("/api/v1/disciplinas")
@Tag(name = "Disciplinas", description = "Gestión de disciplinas médicas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las disciplinas", description = "Obtiene la lista completa de disciplinas")
    @ApiResponse(responseCode = "200", description = "Lista de disciplinas obtenida exitosamente")
    public ResponseEntity<List<DisciplinaDTO>> getAllDisciplinas() {
        List<DisciplinaDTO> disciplinas = disciplinaService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(disciplinas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener disciplina por ID", description = "Busca una disciplina específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina encontrada"),
            @ApiResponse(responseCode = "404", description = "Disciplina no encontrada")
    })
    public ResponseEntity<DisciplinaDTO> getDisciplinaById(
            @Parameter(description = "ID de la disciplina", required = true) @PathVariable Long id) {
        return disciplinaService.findById(id)
                .map(disciplina -> ResponseEntity.ok(convertToDTO(disciplina)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva disciplina", description = "Crea una nueva disciplina médica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Disciplina creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<DisciplinaDTO> createDisciplina(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la disciplina", required = true)
            @RequestBody DisciplinaDTO disciplinaDTO) {
        DisciplinaEntity disciplina = convertToEntity(disciplinaDTO);
        DisciplinaEntity savedDisciplina = disciplinaService.save(disciplina);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedDisciplina));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar disciplina", description = "Actualiza los datos de una disciplina existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina actualizada"),
            @ApiResponse(responseCode = "404", description = "Disciplina no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<DisciplinaDTO> updateDisciplina(
            @Parameter(description = "ID de la disciplina", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos de la disciplina", required = true)
            @RequestBody DisciplinaDTO disciplinaDTO) {
        DisciplinaEntity disciplina = convertToEntity(disciplinaDTO);
        DisciplinaEntity updatedDisciplina = disciplinaService.update(id, disciplina);
        return ResponseEntity.ok(convertToDTO(updatedDisciplina));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar disciplina", description = "Elimina una disciplina del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Disciplina eliminada"),
            @ApiResponse(responseCode = "404", description = "Disciplina no encontrada"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar (tiene terapeutas asociados)")
    })
    public ResponseEntity<Void> deleteDisciplina(
            @Parameter(description = "ID de la disciplina", required = true) @PathVariable Long id) {
        disciplinaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private DisciplinaDTO convertToDTO(DisciplinaEntity disciplina) {
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setIdDisciplina(disciplina.getIdDisciplina());
        dto.setNombre(disciplina.getNombre());
        dto.setDescripcion(disciplina.getDescripcion());
        dto.setEstado(disciplina.getEstado());
        dto.setCodigo(disciplina.getCodigo());
        dto.setColor(disciplina.getColor());
        dto.setFechaCreacion(disciplina.getFechaCreacion());
        dto.setTerapeutasAsociados(disciplina.getTerapeutas() != null ? disciplina.getTerapeutas().size() : 0);
        return dto;
    }

    private DisciplinaEntity convertToEntity(DisciplinaDTO dto) {
        DisciplinaEntity disciplina = new DisciplinaEntity();
        disciplina.setNombre(dto.getNombre());
        disciplina.setDescripcion(dto.getDescripcion());
        disciplina.setEstado(dto.getEstado());
        disciplina.setCodigo(dto.getCodigo());
        disciplina.setColor(dto.getColor());
        return disciplina;
    }
}