package com.clinica.controller;

import com.clinica.model.dto.CitaDTO;
import com.clinica.model.dto.PacienteDTO;
import com.clinica.model.dto.PagoDTO;
import com.clinica.model.dto.TerapeutaDTO;
import com.clinica.model.dto.bpm.ProcessStartRequest;
import com.clinica.model.dto.bpm.ProcessStartResponse;
import com.clinica.service.bpm.BpmProcessService;
import com.clinica.service.impl.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST integrado para orquestación de procesos BPM y servicios
 * Dispara procesos BPMN con integración de JMS y manejo de excepciones
 */
@RestController
@RequestMapping("/api/v1/bpm")
@Tag(name = "BPM Orquestación", description = "Orquestación de procesos BPM integrados")
@Slf4j
@RequiredArgsConstructor
public class BpmOrquestadorController {
    
    private final BpmProcessService bpmProcessService;
    private final PacienteServiceImpl pacienteService;
    private final CitaServiceImpl citaService;
    private final PagoServiceImpl pagoService;
    private final TerapeutaServiceImpl terapeutaService;
    private final EvaluacionServiceImpl evaluacionService;
    
    @PostMapping("/iniciar-gestion-paciente")
    @Operation(summary = "Inicia proceso de gestión de paciente", 
               description = "Dispara proceso BPMN GestionPacientes con integración JMS")
    public ResponseEntity<ProcessStartResponse> iniciarGestionPaciente(@RequestBody PacienteDTO pacienteDTO) {
        log.info("Iniciando proceso BPM: GestionPacientes para {}", pacienteDTO.getNombre());
        
        try {
            // Preparar variables para el proceso BPMN
            Map<String, Object> variables = new HashMap<>();
            variables.put("nombre", pacienteDTO.getNombre());
            variables.put("apellido", pacienteDTO.getApellido());
            variables.put("email", pacienteDTO.getEmail());
            variables.put("telefono", pacienteDTO.getTelefono());
            variables.put("tipoOperacion", "CREAR_PACIENTE");
            
            ProcessStartRequest request = new ProcessStartRequest();
            request.setProcessId("GestionPacientes");
            request.setVariables(variables);
            
            ProcessStartResponse response = bpmProcessService.startProcess(request);
            
            if (response.isSuccess()) {
                // Guardar paciente de forma sincrónica para que exista en BD
                pacienteService.crearPaciente(pacienteDTO);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error iniciando proceso GestionPacientes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProcessStartResponse());
        }
    }
    
    @PostMapping("/iniciar-gestion-cita")
    @Operation(summary = "Inicia proceso de gestión de cita",
               description = "Dispara proceso BPMN GestionCitas con validación y integración JMS")
    public ResponseEntity<ProcessStartResponse> iniciarGestionCita(@RequestBody CitaDTO citaDTO) {
        log.info("Iniciando proceso BPM: GestionCitas");
        
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("pacienteId", citaDTO.getPacienteId());
            variables.put("terapeutaId", citaDTO.getTerapeutaId());
            variables.put("fechaHora", citaDTO.getFechaHora().toString());
            variables.put("motivo", citaDTO.getMotivo());
            variables.put("tipoOperacion", "CREAR_CITA");
            
            ProcessStartRequest request = new ProcessStartRequest();
            request.setProcessId("GestionCitas");
            request.setVariables(variables);
            
            ProcessStartResponse response = bpmProcessService.startProcess(request);
            
            if (response.isSuccess()) {
                citaService.crearCita(citaDTO);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error iniciando proceso GestionCitas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProcessStartResponse());
        }
    }
    
    @PostMapping("/iniciar-gestion-pago")
    @Operation(summary = "Inicia proceso de gestión de pago",
               description = "Dispara proceso BPMN GestionPagos con validación y integración JMS")
    public ResponseEntity<ProcessStartResponse> iniciarGestionPago(@RequestBody PagoDTO pagoDTO) {
        log.info("Iniciando proceso BPM: GestionPagos");
        
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("citaId", pagoDTO.getCitaId());
            variables.put("monto", pagoDTO.getMonto());
            variables.put("metodoPago", pagoDTO.getMetodoPago());
            variables.put("tipoOperacion", "CREAR_PAGO");
            
            ProcessStartRequest request = new ProcessStartRequest();
            request.setProcessId("GestionPagos");
            request.setVariables(variables);
            
            ProcessStartResponse response = bpmProcessService.startProcess(request);
            
            if (response.isSuccess()) {
                pagoService.crearPago(pagoDTO);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error iniciando proceso GestionPagos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProcessStartResponse());
        }
    }
    
    @PostMapping("/iniciar-gestion-terapeuta")
    @Operation(summary = "Inicia proceso de gestión de terapeuta",
               description = "Dispara proceso BPMN GestionTerapeutas con integración JMS")
    public ResponseEntity<ProcessStartResponse> iniciarGestionTerapeuta(@RequestBody TerapeutaDTO terapeutaDTO) {
        log.info("Iniciando proceso BPM: GestionTerapeutas");
        
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("nombre", terapeutaDTO.getNombre());
            variables.put("apellido", terapeutaDTO.getApellido());
            variables.put("especialidad", terapeutaDTO.getEspecialidad());
            variables.put("telefono", terapeutaDTO.getTelefono());
            variables.put("tipoOperacion", "CREAR_TERAPEUTA");
            
            ProcessStartRequest request = new ProcessStartRequest();
            request.setProcessId("GestionTerapeutas");
            request.setVariables(variables);
            
            ProcessStartResponse response = bpmProcessService.startProcess(request);
            
            if (response.isSuccess()) {
                terapeutaService.crearTerapeuta(terapeutaDTO);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error iniciando proceso GestionTerapeutas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProcessStartResponse());
        }
    }
    
    @PostMapping("/confirmar-cita/{citaId}")
    @Operation(summary = "Confirma una cita y dispara proceso BPM")
    public ResponseEntity<ProcessStartResponse> confirmarCita(@PathVariable Long citaId) {
        log.info("Confirmando cita ID: {}", citaId);
        
        try {
            CitaDTO cita = citaService.obtenerCitaPorId(citaId);
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("citaId", citaId);
            variables.put("pacienteId", cita.getPacienteId());
            variables.put("terapeutaId", cita.getTerapeutaId());
            variables.put("tipoOperacion", "CONFIRMAR_CITA");
            
            ProcessStartRequest request = new ProcessStartRequest();
            request.setProcessId("GestionCitas");
            request.setVariables(variables);
            
            ProcessStartResponse response = bpmProcessService.startProcess(request);
            
            if (response.isSuccess()) {
                citaService.confirmarCita(citaId);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error confirmando cita", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProcessStartResponse());
        }
    }
    
    @PostMapping("/procesar-pago/{pagoId}")
    @Operation(summary = "Procesa un pago y dispara proceso BPM")
    public ResponseEntity<ProcessStartResponse> procesarPago(@PathVariable Long pagoId) {
        log.info("Procesando pago ID: {}", pagoId);
        
        try {
            PagoDTO pago = pagoService.obtenerPagoPorId(pagoId);
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("pagoId", pagoId);
            variables.put("monto", pago.getMonto());
            variables.put("metodoPago", pago.getMetodoPago());
            variables.put("tipoOperacion", "PROCESAR_PAGO");
            
            ProcessStartRequest request = new ProcessStartRequest();
            request.setProcessId("GestionPagos");
            request.setVariables(variables);
            
            ProcessStartResponse response = bpmProcessService.startProcess(request);
            
            if (response.isSuccess()) {
                pagoService.procesarPago(pagoId);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error procesando pago", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProcessStartResponse());
        }
    }
    
    @GetMapping("/estado/{processInstanceId}")
    @Operation(summary = "Obtiene el estado de un proceso")
    public ResponseEntity<Map<String, Object>> obtenerEstadoProceso(@PathVariable String processInstanceId) {
        log.info("Obteniendo estado del proceso: {}", processInstanceId);
        
        Map<String, Object> estado = new HashMap<>();
        estado.put("processInstanceId", processInstanceId);
        estado.put("estado", "EN_PROGRESO");
        estado.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(estado);
    }
}
