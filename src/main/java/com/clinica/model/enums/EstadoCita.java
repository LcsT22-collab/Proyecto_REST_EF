package com.clinica.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados posibles de una cita")
public enum EstadoCita {
    @Schema(description = "Cita programada pero no confirmada")
    PROGRAMADA,
    
    @Schema(description = "Cita confirmada por el paciente")
    CONFIRMADA,
    
    @Schema(description = "Cita en proceso de realización")
    EN_PROCESO,
    
    @Schema(description = "Cita completada exitosamente")
    COMPLETADA,
    
    @Schema(description = "Cita cancelada")
    CANCELADA,
    
    @Schema(description = "Cita reprogramada para otra fecha")
    REPROGRAMADA,
    
    @Schema(description = "Paciente no se presentó a la cita")
    AUSENTE
}