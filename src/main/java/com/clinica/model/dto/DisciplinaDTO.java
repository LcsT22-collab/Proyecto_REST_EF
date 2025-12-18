package com.clinica.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la gestión de disciplinas")
public class DisciplinaDTO {
    
    @Schema(description = "ID de la disciplina", example = "1")
    private Long idDisciplina;
    
    @Schema(description = "Nombre de la disciplina", example = "Fisioterapia")
    private String nombre;
    
    @Schema(description = "Descripción", example = "Terapia física para rehabilitación")
    private String descripcion;
    
    @Schema(description = "Estado", example = "ACTIVA")
    private String estado;
    
    @Schema(description = "Código único", example = "FIS")
    private String codigo;
    
    @Schema(description = "Color en hexadecimal", example = "#FF5733")
    private String color;
    
    @Schema(description = "Fecha de creación")
    private LocalDateTime fechaCreacion;
    
    @Schema(description = "Número de terapeutas asociados")
    private Integer terapeutasAsociados;
}