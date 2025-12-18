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
@Schema(description = "Información de una tarea BPMN")
public class TaskInfo {
    
    @Schema(description = "ID de la tarea", example = "1")
    private Long taskId;
    
    @Schema(description = "Nombre de la tarea", example = "Validar Email")
    private String taskName;
    
    @Schema(description = "Estado de la tarea", example = "Ready")
    private String state;
    
    @Schema(description = "Variables de la tarea")
    private Map<String, Object> variables;
}
