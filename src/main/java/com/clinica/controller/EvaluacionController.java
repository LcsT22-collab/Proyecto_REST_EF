package com.clinica.controller;

import com.clinica.model.dto.EvaluacionDTO;
import com.clinica.model.entity.EvaluacionesEntity;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.service.EvaluacionService;
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
@RequestMapping("/api/v1/evaluaciones")
@Tag(name = "Evaluaciones", description = "Gestión de evaluaciones médicas")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    public EvaluacionController(EvaluacionService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las evaluaciones", description = "Obtiene la lista completa de evaluaciones")
    @ApiResponse(responseCode = "200", description = "Lista de evaluaciones obtenida exitosamente")
    public ResponseEntity<List<EvaluacionDTO>> getAllEvaluaciones() {
        List<EvaluacionDTO> evaluaciones = evaluacionService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(evaluaciones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener evaluación por ID", description = "Busca una evaluación específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluación encontrada"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    public ResponseEntity<EvaluacionDTO> getEvaluacionById(
            @Parameter(description = "ID de la evaluación", required = true) @PathVariable Long id) {
        return evaluacionService.findById(id)
                .map(evaluacion -> ResponseEntity.ok(convertToDTO(evaluacion)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva evaluación", description = "Crea una nueva evaluación médica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evaluación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EvaluacionDTO> createEvaluacion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la evaluación", required = true)
            @RequestBody EvaluacionDTO evaluacionDTO) {
        EvaluacionesEntity evaluacion = convertToEntity(evaluacionDTO);
        EvaluacionesEntity savedEvaluacion = evaluacionService.save(evaluacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedEvaluacion));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar evaluación", description = "Actualiza los datos de una evaluación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluación actualizada"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EvaluacionDTO> updateEvaluacion(
            @Parameter(description = "ID de la evaluación", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos de la evaluación", required = true)
            @RequestBody EvaluacionDTO evaluacionDTO) {
        EvaluacionesEntity evaluacion = convertToEntity(evaluacionDTO);
        EvaluacionesEntity updatedEvaluacion = evaluacionService.update(id, evaluacion);
        return ResponseEntity.ok(convertToDTO(updatedEvaluacion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar evaluación", description = "Elimina una evaluación del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evaluación eliminada"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    public ResponseEntity<Void> deleteEvaluacion(
            @Parameter(description = "ID de la evaluación", required = true) @PathVariable Long id) {
        evaluacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EvaluacionDTO convertToDTO(EvaluacionesEntity evaluacion) {
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setIdEvaluaciones(evaluacion.getIdEvaluaciones());
        dto.setFechaEvaluacion(evaluacion.getFechaEvaluacion());
        dto.setTipoEvaluacion(evaluacion.getTipoEvaluacion());
        dto.setResultado(evaluacion.getResultado());
        dto.setRecomendaciones(evaluacion.getRecomendaciones());
        
        if (evaluacion.getPaciente() != null) {
            dto.setPacienteId(evaluacion.getPaciente().getIdPaciente());
            dto.setNombrePaciente(evaluacion.getPaciente().getNombre());
        }
        
        return dto;
    }

    private EvaluacionesEntity convertToEntity(EvaluacionDTO dto) {
        EvaluacionesEntity evaluacion = new EvaluacionesEntity();
        evaluacion.setFechaEvaluacion(dto.getFechaEvaluacion());
        evaluacion.setTipoEvaluacion(dto.getTipoEvaluacion());
        evaluacion.setResultado(dto.getResultado());
        evaluacion.setRecomendaciones(dto.getRecomendaciones());
        
        if (dto.getPacienteId() != null) {
            PacientesEntity paciente = new PacientesEntity();
            paciente.setIdPaciente(dto.getPacienteId());
            evaluacion.setPaciente(paciente);
        }
        
        return evaluacion;
    }
}