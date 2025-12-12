package com.clinica.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados posibles de una disciplina")
public enum EstadoDisciplina {
    @Schema(description = "Disciplina activa y disponible")
    ACTIVA,
    
    @Schema(description = "Disciplina inactiva temporalmente")
    INACTIVA,
    
    @Schema(description = "Disciplina en revisión")
    EN_REVISION,
    
    @Schema(description = "Disciplina descontinuada")
    DESCONTINUADA
}