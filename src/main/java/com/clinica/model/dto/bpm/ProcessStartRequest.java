package com.clinica.model.dto.bpm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para iniciar un proceso BPMN")
public class ProcessStartRequest {
    
    @Schema(description = "ID del proceso a iniciar", example = "GestionCitas")
    private String processId;
    
    @Schema(description = "Variables del proceso")
    private Map<String, Object> variables;
}
