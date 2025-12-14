package com.clinica.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la gestión de terapeutas")
public class TerapeutaDTO {
    
    @Schema(description = "ID del terapeuta", example = "1")
    private Long idTerapeuta;
    
    @Schema(description = "Nombre completo", example = "Dra. María López", required = true)
    private String nombre;
    
    @Schema(description = "Especialidad", example = "Fisioterapia")
    private String especialidad;
    
    @Schema(description = "Disponibilidad", example = "DISPONIBLE")
    private String disponibilidad;
    
    @Schema(description = "ID de la disciplina", example = "1")
    private Long disciplinaId;
    
    @Schema(description = "Nombre de la disciplina", example = "Fisioterapia")
    private String nombreDisciplina;
    
    @Schema(description = "Código de licencia", example = "LIC-12345")
    private String codigoLicencia;
    
    @Schema(description = "Teléfono", example = "+1234567890")
    private String telefono;
    
    @Schema(description = "Correo electrónico", example = "maria@clinica.com")
    private String correo;
    
    @Schema(description = "Activo", example = "true")
    private Boolean activo;
}