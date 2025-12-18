package com.clinica.model.dto.bpm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del estado de un proceso BPMN")
public class ProcessInstanceInfo {
    
    @Schema(description = "ID de la instancia del proceso", example = "1")
    private Long processInstanceId;
    
    @Schema(description = "ID del proceso", example = "GestionCitas")
    private String processId;
    
    @Schema(description = "Estado actual", example = "ACTIVE")
    private String state;
    
    @Schema(description = "Variables del proceso")
    private Map<String, Object> variables;
    
    @Schema(description = "Tareas activas")
    private List<TaskInfo> activeTasks;
}
