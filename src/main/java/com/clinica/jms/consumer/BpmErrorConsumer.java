package com.clinica.jms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Consumidor de cola de errores de BPM
 * Maneja errores, realiza compensaciones y registra intentos de recuperación
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BpmErrorConsumer {
    
    private final ObjectMapper objectMapper;
    private final JmsTemplate jmsTemplate;
    private static final String BPM_COMPENSATION_QUEUE = "${jms.queue.bpm.compensacion:bpm.compensacion.queue}";
    private static final int MAX_RETRY_ATTEMPTS = 3;
    
    /**
     * Consume eventos de error de procesos BPMN
     * Registra errores y realiza compensaciones
     */
    @JmsListener(destination = "${jms.queue.bpm.error:bpm.error.queue}", 
                 containerFactory = "jmsListenerContainerFactory")
    public void consumeError(String message) {
        log.error("BpmErrorConsumer: Error recibido en cola de errores");
        
        try {
            Map<String, Object> errorData = objectMapper.readValue(message, Map.class);
            String processId = (String) errorData.get("processId");
            String processInstanceId = (String) errorData.get("processInstanceId");
            String errorMessage = (String) errorData.get("errorMessage");
            
            log.error("Proceso en ERROR - ProcessId: {}, Instancia: {}, Error: {}", 
                    processId, processInstanceId, errorMessage);
            
            // Registrar el error
            registrarError(processId, processInstanceId, errorMessage);
            
            // Obtener número de intentos
            int retryCount = extractIntValue(errorData.get("retryCount"));
            
            // Si no se ha excedido el límite de reintentos, reintentar
            if (retryCount < MAX_RETRY_ATTEMPTS) {
                log.info("Reintentando proceso {} - Intento {} de {}", 
                        processId, retryCount + 1, MAX_RETRY_ATTEMPTS);
                reintentar(errorData, retryCount + 1);
            } else {
                log.error("Se alcanzó el límite de reintentos para el proceso {}", processId);
                // Iniciar compensación
                compensar(processId, processInstanceId, errorData);
            }
            
        } catch (Exception e) {
            log.error("Error al consumir mensaje de error BPM", e);
        }
    }
    
    /**
     * Registra el error en logs y bases de datos (si es necesario)
     */
    private void registrarError(String processId, String processInstanceId, String errorMessage) {
        log.error("REGISTRANDO ERROR:");
        log.error("  ProcessId: {}", processId);
        log.error("  ProcessInstanceId: {}", processInstanceId);
        log.error("  Mensaje: {}", errorMessage);
        log.error("  Timestamp: {}", LocalDateTime.now());
        
        // Aquí se podría persisti el error en la base de datos
    }
    
    /**
     * Reintenta el proceso
     */
    private void reintentar(Map<String, Object> errorData, int retryCount) {
        try {
            Map<String, Object> retryData = new HashMap<>(errorData);
            retryData.put("retryCount", retryCount);
            retryData.put("lastRetryTime", LocalDateTime.now());
            
            // Enviar de vuelta a la cola principal para reintentar
            String messageStr = objectMapper.writeValueAsString(retryData);
            jmsTemplate.convertAndSend("${jms.queue.bpm.main:bpm.main.queue}", messageStr);
            
            log.info("Proceso reenviado para reintento");
            
        } catch (Exception e) {
            log.error("Error reenviando proceso para reintento", e);
        }
    }
    
    /**
     * Inicia el proceso de compensación
     */
    private void compensar(String processId, String processInstanceId, Map<String, Object> errorData) {
        try {
            log.info("Iniciando compensación para proceso {}", processId);
            
            Map<String, Object> compensationData = new HashMap<>();
            compensationData.put("processId", processId);
            compensationData.put("processInstanceId", processInstanceId);
            compensationData.put("compensationType", "ROLLBACK_COMPLETO");
            compensationData.put("timestamp", LocalDateTime.now());
            compensationData.put("originalData", errorData);
            
            String messageStr = objectMapper.writeValueAsString(compensationData);
            jmsTemplate.convertAndSend(BPM_COMPENSATION_QUEUE, messageStr);
            
            log.info("Proceso de compensación enviado exitosamente");
            
        } catch (Exception e) {
            log.error("Error iniciando compensación", e);
        }
    }
    
    private int extractIntValue(Object value) {
        if (value == null) return 0;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Long) return ((Long) value).intValue();
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}