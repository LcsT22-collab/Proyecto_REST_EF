package com.clinica.controller.bpm;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.model.dto.bpm.AsignacionRequest;
import com.clinica.model.dto.bpm.JmsPublicacionRequest;
import com.clinica.model.dto.bpm.SolicitudCitaBPM;
import com.clinica.service.bpm.BpmService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bpm")
@Tag(name = "BPM", description = "Gestión de Procesos de Negocio (BPM)")
public class BpmController {

    private final BpmService bpmService;
    private final JmsTemplate jmsTemplate;

    public BpmController(BpmService bpmService, JmsTemplate jmsTemplate) {
        this.bpmService = bpmService;
        this.jmsTemplate = jmsTemplate;
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

    @PostMapping("/asignar")
    @Operation(summary = "Endpoint stub para asignar recursos", 
               description = "Asigna recursos para una cita (stub, siempre retorna 200 OK)")
    public ResponseEntity<Map<String, Object>> asignarRecursos(@RequestBody AsignacionRequest request) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("exito", true);
            response.put("mensaje", "Recursos asignados exitosamente");
            response.put("idTransaccion", request.getIdTransaccion());
            response.put("terapeutaId", request.getTerapeutaId());
            response.put("tipoCita", request.getTipoCita());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("exito", false);
            errorResponse.put("mensaje", "Error al asignar recursos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/notificar")
    @Operation(summary = "Publicar mensaje en cola de notificaciones", 
               description = "Publica un mensaje en bpm.notificaciones.queue")
    public ResponseEntity<Map<String, String>> notificar(@RequestBody JmsPublicacionRequest request) {
        try {
            publicarEnCola(request.getCola(), request.getMensaje());
            Map<String, String> response = new HashMap<>();
            response.put("estado", "OK");
            response.put("mensaje", "Mensaje publicado en " + request.getCola());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("estado", "ERROR");
            errorResponse.put("mensaje", "Error al publicar mensaje: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/error")
    @Operation(summary = "Publicar mensaje en cola de errores", 
               description = "Publica un mensaje en bpm.error.queue")
    public ResponseEntity<Map<String, String>> reportarError(@RequestBody JmsPublicacionRequest request) {
        try {
            publicarEnCola(request.getCola(), request.getMensaje());
            Map<String, String> response = new HashMap<>();
            response.put("estado", "OK");
            response.put("mensaje", "Error reportado en " + request.getCola());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("estado", "ERROR");
            errorResponse.put("mensaje", "Error al reportar error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/compensar")
    @Operation(summary = "Publicar mensaje en cola de compensación", 
               description = "Publica un mensaje en bpm.compensacion.queue para revertir operaciones")
    public ResponseEntity<Map<String, String>> compensar(@RequestBody JmsPublicacionRequest request) {
        try {
            publicarEnCola(request.getCola(), request.getMensaje());
            Map<String, String> response = new HashMap<>();
            response.put("estado", "OK");
            response.put("mensaje", "Compensación iniciada en " + request.getCola());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("estado", "ERROR");
            errorResponse.put("mensaje", "Error al compensar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Método genérico para publicar mensajes en cualquier cola JMS
     */
    private void publicarEnCola(String nombreCola, Object mensaje) {
        jmsTemplate.convertAndSend(nombreCola, mensaje);
    }
}