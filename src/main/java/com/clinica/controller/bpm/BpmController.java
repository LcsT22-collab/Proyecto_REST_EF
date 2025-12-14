package com.clinica.controller.bpm;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.model.dto.bpm.SolicitudCitaBPM;
import com.clinica.service.bpm.BpmService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/bpm")
@Tag(name = "BPM", description = "Gestión de Procesos de Negocio (BPM)")
public class BpmController {

    private final BpmService bpmService;

    public BpmController(BpmService bpmService) {
        this.bpmService = bpmService;
    }

    @PostMapping("/cita")
    @Operation(summary = "Iniciar proceso BPM para crear cita")
    public ResponseEntity<SolicitudCitaBPM> iniciarProcesoCita(
            @Parameter(description = "ID del paciente", required = true) @RequestParam Long pacienteId,
            @Parameter(description = "ID del terapeuta", required = true) @RequestParam Long terapeutaId,
            @Parameter(description = "Tipo de cita", example = "CONSULTA") @RequestParam(defaultValue = "CONSULTA") String tipoCita) {
        
        SolicitudCitaBPM solicitud = bpmService.iniciarProcesoCita(pacienteId, terapeutaId, tipoCita);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(solicitud);
    }

    @GetMapping("/status")
    @Operation(summary = "Verificar estado del sistema BPM")
    public ResponseEntity<String> verificarEstado() {
        return ResponseEntity.ok("Sistema BPM funcionando correctamente");
    }
}