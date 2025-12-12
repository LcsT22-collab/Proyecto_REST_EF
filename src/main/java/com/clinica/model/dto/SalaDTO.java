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
@Schema(description = "Objeto de Transferencia de Datos para Salas")
public class SalaDTO {

    @Schema(description = "ID único de la sala", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idSala;

    @Schema(description = "Nombre de la sala", example = "Sala de Terapia A", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(description = "Número de sala", example = "101")
    private String numero;

    @Schema(description = "Capacidad de personas", example = "10")
    private Integer capacidad;

    @Schema(description = "Piso donde se encuentra", example = "1")
    private String piso;

    @Schema(description = "Indica si la sala está disponible", example = "true")
    private Boolean disponible;

    @Schema(description = "Equipamiento de la sala", example = "Camilla, sillas ergonómicas, computadora")
    private String equipamiento;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de creación", example = "2024-01-15 10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de última actualización", example = "2024-01-20 14:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;
}
