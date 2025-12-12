package com.clinica.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados de disponibilidad de los terapeutas")
public enum DisponibilidadTerapeuta {
    @Schema(description = "Disponible para nuevas citas")
    DISPONIBLE,
    
    @Schema(description = "Actualmente en consulta")
    OCUPADO,
    
    @Schema(description = "En período de vacaciones")
    VACACIONES,
    
    @Schema(description = "En licencia médica o administrativa")
    LICENCIA,
    
    @Schema(description = "No disponible temporal o permanentemente")
    NO_DISPONIBLE
}