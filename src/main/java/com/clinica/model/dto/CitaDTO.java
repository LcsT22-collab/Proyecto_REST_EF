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
@Schema(description = "Objeto de Transferencia de Datos para Citas Médicas")
public class CitaDTO {

    @Schema(description = "ID único de la cita", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idCita;
    
    @Schema(description = "ID del paciente", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idPaciente;
    
    @Schema(description = "ID del terapeuta", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idTerapeuta;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha y hora de la cita", example = "2024-01-20 14:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime fechaCita;
    
    @Schema(description = "Estado de la cita", 
             example = "PROGRAMADA", 
             allowableValues = {"PROGRAMADA", "CONFIRMADA", "EN_PROCESO", "COMPLETADA", "CANCELADA", "REPROGRAMADA", "AUSENTE"})
    private String estadoCita;
    
    @Schema(description = "Nombre del paciente (solo lectura)", example = "Juan Pérez", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombrePaciente;
    
    @Schema(description = "Nombre del terapeuta (solo lectura)", example = "Dr. Carlos López", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreTerapeuta;
    
    @Schema(description = "Especialidad del terapeuta (solo lectura)", example = "Psicología Clínica", accessMode = Schema.AccessMode.READ_ONLY)
    private String especialidadTerapeuta;
}