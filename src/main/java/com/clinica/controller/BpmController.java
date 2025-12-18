package com.clinica.controller.bpm;

import com.clinica.model.dto.bpm.ProcessStartRequest;
import com.clinica.model.dto.bpm.ProcessStartResponse;
import com.clinica.service.bpm.BpmProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bpm")
@Tag(name = "BPM", description = "Gestión de procesos BPMN/jBPM")
@Slf4j
@RequiredArgsConstructor
public class BpmController {
    
    private final BpmProcessService bpmProcessService;
    
    @PostMapping("/process/start")
    @Operation(summary = "Iniciar un proceso BPMN", description = "Inicia un proceso BPMN con las variables especificadas")
    public ResponseEntity<ProcessStartResponse> startProcess(@RequestBody ProcessStartRequest request) {
        log.info("POST /api/bpm/process/start - Iniciando proceso: {}", request.getProcessId());
        try {
            ProcessStartResponse response = bpmProcessService.startProcess(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al iniciar proceso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
