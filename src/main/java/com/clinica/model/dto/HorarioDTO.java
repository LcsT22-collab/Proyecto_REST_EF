package com.clinica.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la gestión de horarios")
public class HorarioDTO {
    
    @Schema(description = "ID del horario", example = "1")
    private Long idHorario;
    
    @Schema(description = "ID del terapeuta", example = "1")
    private Long terapeutaId;
    
    @Schema(description = "Nombre del terapeuta", example = "Dra. María López")
    private String nombreTerapeuta;
    
    @Schema(description = "Día de la semana", example = "LUNES", required = true)
    private String diaSemana;
    
    @Schema(description = "Hora de inicio", example = "08:00:00", required = true)
    private LocalTime horaInicio;
    
    @Schema(description = "Hora de fin", example = "12:00:00", required = true)
    private LocalTime horaFin;
    
    @Schema(description = "Activo", example = "true")
    private Boolean activo;
}