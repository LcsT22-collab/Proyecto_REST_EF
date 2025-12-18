package com.clinica.model.dto.bpm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de inicio de proceso BPMN")
public class ProcessStartResponse {
    
    @Schema(description = "ID de la instancia del proceso", example = "1")
    private Long processInstanceId;
    
    @Schema(description = "ID del proceso", example = "GestionCitas")
    private String processId;
    
    @Schema(description = "Estado del proceso", example = "INICIADO")
    private String status;
    
    @Schema(description = "Variables de respuesta")
    private Map<String, Object> variables;
    
    @Schema(description = "Éxito de la operación", example = "true")
    private boolean success;
    
    @Schema(description = "Mensaje de respuesta")
    private String message;
    
    @Schema(description = "Hora de inicio del proceso")
    private LocalDateTime startTime;
}
