package com.clinica.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de Transferencia de Datos para Terapeutas")
public class TerapeutaDTO {
    
    @Schema(description = "ID único del terapeuta", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idTerapeuta;
    
    @Schema(description = "Nombre completo del terapeuta", example = "Dr. Carlos López", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
    
    @Schema(description = "Especialidad del terapeuta", example = "Psicología Clínica")
    private String especialidad;
    
    @Schema(description = "Disponibilidad actual del terapeuta", 
             example = "DISPONIBLE", 
             allowableValues = {"DISPONIBLE", "OCUPADO", "VACACIONES", "LICENCIA", "NO_DISPONIBLE"})
    private String disponibilidadTerapeuta;
    
    @Schema(description = "ID de la disciplina asociada", example = "1")
    private Long idDisciplina;
    
    @Schema(description = "Nombre de la disciplina (solo lectura)", example = "Psicología", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreDisciplina;
}