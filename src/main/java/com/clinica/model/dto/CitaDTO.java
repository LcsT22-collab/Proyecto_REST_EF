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
@Schema(description = "DTO para la gestión de citas")
public class CitaDTO {
    
    @Schema(description = "ID de la cita", example = "1")
    private Long idCita;
    
    @Schema(description = "ID del paciente", example = "1")
    private Long pacienteId;
    
    @Schema(description = "Nombre del paciente", example = "Juan Pérez")
    private String nombrePaciente;
    
    @Schema(description = "ID del terapeuta", example = "1", required = true)
    private Long terapeutaId;
    
    @Schema(description = "Nombre del terapeuta", example = "Dra. María López")
    private String nombreTerapeuta;
    
    @Schema(description = "Fecha y hora de la cita", example = "2024-12-15T10:00:00", required = true)
    private LocalDateTime fechaCita;
    
    @Schema(description = "Duración en minutos", example = "60")
    private Integer duracionMinutos;
    
    @Schema(description = "Estado de la cita", example = "PROGRAMADA")
    private String estadoCita;
    
    @Schema(description = "Observaciones", example = "Primera consulta")
    private String observaciones;
}