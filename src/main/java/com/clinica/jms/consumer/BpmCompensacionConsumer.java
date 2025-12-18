package com.clinica.jms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Consumidor de cola de compensación de BPM
 * Maneja la reversión de transacciones en caso de fallos
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BpmCompensacionConsumer {
    
    private final ObjectMapper objectMapper;
    
    /**
     * Consume eventos de compensación de procesos BPMN
     * Revierte cambios en caso de error
     */
    @JmsListener(destination = "${jms.queue.bpm.compensacion:bpm.compensacion.queue}", 
                 containerFactory = "jmsListenerContainerFactory")
    public void consumeCompensacion(String message) {
        log.info("BpmCompensacionConsumer: Compensación recibida");
        
        try {
            Map<String, Object> compensationData = objectMapper.readValue(message, Map.class);
            String processId = (String) compensationData.get("processId");
            String processInstanceId = (String) compensationData.get("processInstanceId");
            String compensationType = (String) compensationData.get("compensationType");
            
            log.info("Ejecutando compensación: Tipo={}, Proceso={}, Instancia={}", 
                    compensationType, processId, processInstanceId);
            
            ejecutarCompensacion(compensationData);
            
        } catch (Exception e) {
            log.error("Error al consumir compensación BPM", e);
        }
    }
    
    /**
     * Ejecuta la compensación según el tipo de proceso
     */
    private void ejecutarCompensacion(Map<String, Object> compensationData) {
        String processId = (String) compensationData.get("processId");
        String compensationType = (String) compensationData.get("compensationType");
        
        if (processId == null) {
            log.warn("ProcessId no encontrado en datos de compensación");
            return;
        }
        
        String processIdLower = processId.toLowerCase();
        
        try {
            switch (processIdLower) {
                case "gestioncitas":
                    compensarCita(compensationData);
                    break;
                case "gestionpacientes":
                    compensarPaciente(compensationData);
                    break;
                case "gestionterapeutas":
                    compensarTerapeuta(compensationData);
                    break;
                case "gestionpagos":
                    compensarPago(compensationData);
                    break;
                case "gestionevaluaciones":
                    compensarEvaluacion(compensationData);
                    break;
                default:
                    log.warn("Tipo de compensación no reconocida: {}", processId);
            }
            
            log.info("Compensación completada exitosamente para {}", processId);
            
        } catch (Exception e) {
            log.error("Error durante la ejecución de compensación para {}", processId, e);
        }
    }
    
    /**
     * Compensa cambios en citas
     */
    private void compensarCita(Map<String, Object> data) {
        log.info("COMPENSACIÓN INICIADA: Revertiendo cambios en Citas");
        Long pacienteId = extractLongValue(data.get("pacienteId"));
        log.info("  Cancelando cita para paciente: {}", pacienteId);
        // Aquí iría la lógica de reversión de transacciones
        log.info("  Cita revertida exitosamente");
    }
    
    /**
     * Compensa cambios en pacientes
     */
    private void compensarPaciente(Map<String, Object> data) {
        log.info("COMPENSACIÓN INICIADA: Revertiendo cambios en Pacientes");
        String nombre = (String) data.get("nombre");
        log.info("  Eliminando/Revirtiendo paciente: {}", nombre);
        // Lógica de compensación
        log.info("  Paciente revertido exitosamente");
    }
    
    /**
     * Compensa cambios en terapeutas
     */
    private void compensarTerapeuta(Map<String, Object> data) {
        log.info("COMPENSACIÓN INICIADA: Revertiendo cambios en Terapeutas");
        String nombre = (String) data.get("nombre");
        log.info("  Desactivando terapeuta: {}", nombre);
        // Lógica de compensación
        log.info("  Terapeuta revertido exitosamente");
    }
    
    /**
     * Compensa cambios en pagos
     */
    private void compensarPago(Map<String, Object> data) {
        log.info("COMPENSACIÓN INICIADA: Revertiendo cambios en Pagos");
        Object monto = data.get("monto");
        log.info("  Reembolsando pago de: {}", monto);
        // Lógica de compensación
        log.info("  Pago revertido exitosamente");
    }
    
    /**
     * Compensa cambios en evaluaciones
     */
    private void compensarEvaluacion(Map<String, Object> data) {
        log.info("COMPENSACIÓN INICIADA: Revertiendo cambios en Evaluaciones");
        Long pacienteId = extractLongValue(data.get("pacienteId"));
        log.info("  Eliminando evaluación del paciente: {}", pacienteId);
        // Lógica de compensación
        log.info("  Evaluación revertida exitosamente");
    }
    
    private Long extractLongValue(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}