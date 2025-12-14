package com.clinica.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la gestión de evaluaciones")
public class EvaluacionDTO {
    
    @Schema(description = "ID de la evaluación", example = "1")
    private Long idEvaluaciones;
    
    @Schema(description = "ID del paciente", example = "1", required = true)
    private Long pacienteId;
    
    @Schema(description = "Nombre del paciente", example = "Juan Pérez")
    private String nombrePaciente;
    
    @Schema(description = "Fecha de evaluación", example = "2024-12-01", required = true)
    private LocalDate fechaEvaluacion;
    
    @Schema(description = "Tipo de evaluación", example = "Evaluación Inicial", required = true)
    private String tipoEvaluacion;
    
    @Schema(description = "Resultados", example = "Paciente presenta...")
    private String resultado;
    
    @Schema(description = "Recomendaciones", example = "Sesiones de terapia 2 veces por semana")
    private String recomendaciones;
}