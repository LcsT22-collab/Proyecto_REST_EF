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

import com.clinica.model.dto.EvaluacionDTO;
import com.clinica.model.entity.EvaluacionesEntity;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.service.EvaluacionService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Evaluaciones", description = "Operaciones CRUD para la gestión de evaluaciones médicas")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    @GetMapping("/listar/evaluaciones")
    @Operation(summary = "Listar todas las evaluaciones", description = "Obtiene una lista completa de todas las evaluaciones registradas")
    @ApiResponse(responseCode = "200", description = "Lista de evaluaciones obtenida exitosamente")
    public List<EvaluacionDTO> listarEvaluaciones() {
        List<EvaluacionesEntity> evaluaciones = evaluacionService.findAll();
        return evaluaciones.stream()
                .map(this::convertirADto)
                .toList();
    }

    @GetMapping("/evaluacion/{id}")
    @Operation(summary = "Obtener evaluación por ID", description = "Busca una evaluación específica por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluación encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    public ResponseEntity<EvaluacionDTO> obtenerEvaluacion(
            @Parameter(description = "ID de la evaluación", example = "1", required = true) @PathVariable Long id) {
        return evaluacionService.findById(id)
                .map(evaluacion -> ResponseEntity.ok(convertirADto(evaluacion)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/evaluacion")
    @Operation(summary = "Crear nueva evaluación", description = "Registra una nueva evaluación médica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evaluación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la evaluación inválidos")
    })
    public ResponseEntity<EvaluacionDTO> crearEvaluacion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la evaluación a crear", required = true) @RequestBody EvaluacionDTO evaluacionDTO) {
        EvaluacionesEntity evaluacion = convertirAEntidad(evaluacionDTO);
        EvaluacionesEntity nuevaEvaluacion = evaluacionService.save(evaluacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADto(nuevaEvaluacion));
    }

    @PutMapping("/editar/evaluacion/{id}")
    @Operation(summary = "Actualizar evaluación", description = "Actualiza la información de una evaluación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluación actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<EvaluacionDTO> actualizarEvaluacion(
            @Parameter(description = "ID de la evaluación a actualizar", example = "1", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos de la evaluación", required = true) @RequestBody EvaluacionDTO evaluacionDTO) {
        EvaluacionesEntity evaluacion = convertirAEntidad(evaluacionDTO);
        EvaluacionesEntity evaluacionActualizada = evaluacionService.update(id, evaluacion);
        return ResponseEntity.ok(convertirADto(evaluacionActualizada));
    }

    @DeleteMapping("/eliminar/evaluacion/{id}")
    @Operation(summary = "Eliminar evaluación", description = "Elimina una evaluación del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evaluación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    public ResponseEntity<Void> eliminarEvaluacion(
            @Parameter(description = "ID de la evaluación a eliminar", example = "1", required = true) @PathVariable Long id) {
        evaluacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EvaluacionDTO convertirADto(EvaluacionesEntity evaluacion) {
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setIdEvaluaciones(evaluacion.getIdEvaluaciones());
        dto.setFechaEvaluacion(evaluacion.getFechaEvaluacion());
        dto.setTipoEvaluacion(evaluacion.getTipoEvaluacion());
        dto.setResultado(evaluacion.getResultado());

        if (evaluacion.getPaciente() != null) {
            dto.setIdPaciente(evaluacion.getPaciente().getIdPaciente());
            dto.setNombrePaciente(evaluacion.getPaciente().getNombre());
        }

        return dto;
    }

    private EvaluacionesEntity convertirAEntidad(EvaluacionDTO dto) {
        EvaluacionesEntity evaluacion = new EvaluacionesEntity();
        evaluacion.setFechaEvaluacion(dto.getFechaEvaluacion());
        evaluacion.setTipoEvaluacion(dto.getTipoEvaluacion());
        evaluacion.setResultado(dto.getResultado());

        if (dto.getIdPaciente() != null) {
            PacientesEntity paciente = new PacientesEntity();
            paciente.setIdPaciente(dto.getIdPaciente());
            evaluacion.setPaciente(paciente);
        }

        return evaluacion;
    }
}