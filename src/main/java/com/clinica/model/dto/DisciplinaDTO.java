package com.clinica.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de Transferencia de Datos para Disciplinas")
public class DisciplinaDTO {

    @Schema(description = "ID único de la disciplina", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idDisciplina;
    
    @Schema(description = "Nombre de la disciplina", example = "Psicología Clínica", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
    
    @Schema(description = "Descripción detallada", example = "Evaluación y tratamiento de trastornos psicológicos")
    private String descripcion;
    
    @Schema(description = "Estado de la disciplina", 
             example = "ACTIVA", 
             allowableValues = {"ACTIVA", "INACTIVA", "EN_REVISION", "DESCONTINUADA"})
    private String estado;
    
    @Schema(description = "Código único de la disciplina", example = "PSC")
    private String codigo;
    
    @Schema(description = "Color representativo (hexadecimal)", example = "#4A90E2")
    private String color;
    
    @Schema(description = "Cantidad de terapeutas asociados", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private int terapeutasAsociados;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de creación", example = "2024-01-15 10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de última actualización", example = "2024-01-20 14:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;
}