package com.clinica.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la gestión de evaluaciones")
public class EvaluacionDTO {
    
    @Schema(description = "ID de la evaluación", example = "1")
    private Long idEvaluacion;
    
    @Schema(description = "ID de evaluaciones (alias)", example = "1")
    private Long idEvaluaciones;
    
    @Schema(description = "ID del paciente", example = "1")
    private Long pacienteId;
    
    @Schema(description = "Nombre del paciente", example = "Juan Pérez")
    private String nombrePaciente;
    
    @Schema(description = "Fecha de evaluación", example = "2024-12-01")
    private LocalDate fechaEvaluacion;
    
    @Schema(description = "Fecha y hora de evaluación", example = "2024-12-01T10:00:00")
    private LocalDateTime fechaEvaluacionTime;
    
    @Schema(description = "Tipo de evaluación", example = "Evaluación Inicial")
    private String tipoEvaluacion;
    
    @Schema(description = "Resultados", example = "Paciente presenta...")
    private String resultado;
    
    @Schema(description = "Observaciones", example = "Observación adicional")
    private String observaciones;
    
    @Schema(description = "Recomendaciones", example = "Sesiones de terapia 2 veces por semana")
    private String recomendaciones;
}