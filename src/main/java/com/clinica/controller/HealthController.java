package com.clinica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health", description = "Estado del sistema y validación")
public class HealthController {

    @GetMapping
    @Operation(summary = "Verificar estado del sistema", description = "Retorna el estado general del sistema")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("services", Map.of(
            "rest", "ACTIVE",
            "jms", "ACTIVE",
            "bpm", "ACTIVE",
            "soap", "ACTIVE",
            "esb", "ACTIVE"
        ));
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/info")
    @Operation(summary = "Información del sistema", description = "Retorna información detallada del sistema")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "Sistema de Gestión de Clínica");
        info.put("version", "1.0.0");
        info.put("java", System.getProperty("java.version"));
        info.put("springBoot", "3.3.6");
        
        info.put("endpoints", Map.of(
            "rest", "/api/v1",
            "swagger", "/swagger-ui.html",
            "h2Console", "/h2-console",
            "soapWsdl", "/ws/pacientes.wsdl"
        ));
        
        info.put("services", new String[]{
            "Pacientes", "Terapeutas", "Citas", "Disciplinas",
            "Evaluaciones", "Pagos", "Horarios", "BPM"
        });
        
        info.put("features", Map.of(
            "restApi", true,
            "soapWebServices", true,
            "jmsMessaging", true,
            "bpmWorkflow", true,
            "esbIntegration", true,
            "alternateFlow", true
        ));
        
        return ResponseEntity.ok(info);
    }
}
