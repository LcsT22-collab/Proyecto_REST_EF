package com.clinica.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de Transferencia de Datos para Horarios")
public class HorarioDTO {

    @Schema(description = "ID único del horario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idHorario;

    @Schema(description = "Día de la semana", example = "LUNES", requiredMode = Schema.RequiredMode.REQUIRED)
    private String diaSemana;

    @JsonFormat(pattern = "HH:mm:ss")
    @Schema(description = "Hora de inicio", example = "08:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime horaInicio;

    @JsonFormat(pattern = "HH:mm:ss")
    @Schema(description = "Hora de fin", example = "17:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime horaFin;

    @Schema(description = "Tipo de horario", example = "LABORAL")
    private String tipo;

    @Schema(description = "Indica si el horario está activo", example = "true")
    private Boolean activo;

    @Schema(description = "Descripción del horario", example = "Horario regular de atención")
    private String descripcion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de creación", example = "2024-01-15 10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de última actualización", example = "2024-01-20 14:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;
}
