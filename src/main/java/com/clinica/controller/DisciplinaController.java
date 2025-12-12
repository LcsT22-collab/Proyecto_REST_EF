package com.clinica.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinica.model.dto.DisciplinaDTO;
import com.clinica.model.entity.DisciplinaEntity;
import com.clinica.model.enums.EstadoDisciplina;
import com.clinica.service.DisciplinaService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Disciplinas", description = "Operaciones CRUD para la gestión de disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @GetMapping("/listar/disciplinas")
    @Operation(summary = "Listar todas las disciplinas", description = "Obtiene una lista completa de todas las disciplinas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de disciplinas obtenida exitosamente")
    public List<DisciplinaDTO> listarDisciplinas() {
        List<DisciplinaEntity> disciplinas = disciplinaService.findAll();
        return disciplinas.stream()
                .map(this::convertirADto)
                .toList();
    }

    @GetMapping("/disciplina/{id}")
    @Operation(summary = "Obtener disciplina por ID", description = "Busca una disciplina específica por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Disciplina no encontrada")
    })
    public ResponseEntity<DisciplinaDTO> obtenerDisciplina(
            @Parameter(description = "ID de la disciplina", example = "1", required = true) @PathVariable Long id) {
        return disciplinaService.findById(id)
                .map(disciplina -> ResponseEntity.ok(convertirADto(disciplina)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/disciplina")
    @Operation(summary = "Crear nueva disciplina", description = "Registra una nueva disciplina en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Disciplina creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la disciplina inválidos")
    })
    public ResponseEntity<DisciplinaDTO> crearDisciplina(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la disciplina a crear", required = true) @RequestBody DisciplinaDTO disciplinaDTO) {
        DisciplinaEntity disciplina = convertirAEntidad(disciplinaDTO);
        DisciplinaEntity nuevaDisciplina = disciplinaService.save(disciplina);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADto(nuevaDisciplina));
    }

    @PutMapping("/editar/disciplina/{id}")
    @Operation(summary = "Actualizar disciplina", description = "Actualiza la información de una disciplina existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Disciplina no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<DisciplinaDTO> actualizarDisciplina(
            @Parameter(description = "ID de la disciplina a actualizar", example = "1", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos de la disciplina", required = true) @RequestBody DisciplinaDTO disciplinaDTO) {
        DisciplinaEntity disciplina = convertirAEntidad(disciplinaDTO);
        DisciplinaEntity disciplinaActualizada = disciplinaService.update(id, disciplina);
        return ResponseEntity.ok(convertirADto(disciplinaActualizada));
    }

    @DeleteMapping("/eliminar/disciplina/{id}")
    @Operation(summary = "Eliminar disciplina", description = "Elimina una disciplina del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Disciplina eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Disciplina no encontrada"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar porque tiene terapeutas asociados")
    })
    public ResponseEntity<Void> eliminarDisciplina(
            @Parameter(description = "ID de la disciplina a eliminar", example = "1", required = true) @PathVariable Long id) {
        disciplinaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private DisciplinaDTO convertirADto(DisciplinaEntity disciplina) {
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setIdDisciplina(disciplina.getIdDisciplina());
        dto.setNombre(disciplina.getNombre());
        dto.setDescripcion(disciplina.getDescripcion());
        dto.setEstado(disciplina.getEstado().toString());
        dto.setCodigo(disciplina.getCodigo());
        dto.setColor(disciplina.getColor());
        dto.setFechaCreacion(disciplina.getFechaCreacion());
        dto.setFechaActualizacion(disciplina.getFechaActualizacion());
        dto.setTerapeutasAsociados(disciplina.getTerapeutas() != null ? disciplina.getTerapeutas().size() : 0);
        return dto;
    }

    private DisciplinaEntity convertirAEntidad(DisciplinaDTO dto) {
        DisciplinaEntity disciplina = new DisciplinaEntity();
        disciplina.setNombre(dto.getNombre());
        disciplina.setDescripcion(dto.getDescripcion());
        disciplina.setColor(dto.getColor());

        if (dto.getEstado() != null) {
            try {
                disciplina.setEstado(EstadoDisciplina.valueOf(dto.getEstado()));
            } catch (IllegalArgumentException e) {
                disciplina.setEstado(EstadoDisciplina.ACTIVA);
            }
        }

        return disciplina;
    }
}