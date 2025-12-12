package com.clinica.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de Transferencia de Datos para Evaluaciones Médicas")
public class EvaluacionDTO {

    @Schema(description = "ID único de la evaluación", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idEvaluaciones;
    
    @Schema(description = "ID del paciente evaluado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idPaciente;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de la evaluación", example = "2024-01-20", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaEvaluacion;
    
    @Schema(description = "Tipo de evaluación realizada", example = "Evaluación Inicial", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoEvaluacion;
    
    @Schema(description = "Resultados de la evaluación", example = "Paciente presenta mejora en X área", requiredMode = Schema.RequiredMode.REQUIRED)
    private String resultado;
    
    @Schema(description = "Nombre del paciente (solo lectura)", example = "Juan Pérez", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombrePaciente;
}