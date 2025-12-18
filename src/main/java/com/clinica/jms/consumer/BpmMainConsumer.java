package com.clinica.jms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Consumidor de cola principal de BPM
 * Orquesta los flujos de negocio según el tipo de proceso
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BpmMainConsumer {
    
    private final ObjectMapper objectMapper;
    
    /**
     * Consume eventos principales del proceso BPMN
     * Orquesta los flujos de negocio según el tipo de proceso
     */
    @JmsListener(destination = "${jms.queue.bpm.main:bpm.main.queue}", 
                 containerFactory = "jmsListenerContainerFactory")
    public void consumeMainProcess(String message) {
        log.info("BpmMainConsumer: Recibido mensaje de cola principal");
        
        try {
            Map<String, Object> messageData = objectMapper.readValue(message, Map.class);
            String processId = (String) messageData.get("processId");
            String processInstanceId = (String) messageData.get("processInstanceId");
            
            log.info("Procesando BPMN: {} - Instancia: {}", processId, processInstanceId);
            
            if (processId == null) {
                log.warn("ProcessId no encontrado en mensaje");
                return;
            }
            
            String processIdLower = processId.toLowerCase();
            
            // Orquestar según tipo de proceso
            switch (processIdLower) {
                case "gestioncitas":
                    procesarGestionCitas(messageData, processInstanceId);
                    break;
                case "gestionpacientes":
                    procesarGestionPacientes(messageData, processInstanceId);
                    break;
                case "gestionterapeutas":
                    procesarGestionTerapeutas(messageData, processInstanceId);
                    break;
                case "gestiondisciplinas":
                    procesarGestionDisciplinas(messageData, processInstanceId);
                    break;
                case "gestionhorarios":
                    procesarGestionHorarios(messageData, processInstanceId);
                    break;
                case "gestionpagos":
                    procesarGestionPagos(messageData, processInstanceId);
                    break;
                case "gestionevaluaciones":
                    procesarGestionEvaluaciones(messageData, processInstanceId);
                    break;
                default:
                    log.warn("Proceso no reconocido: {}", processId);
            }
            
            log.info("Proceso {} completado exitosamente", processId);
            
        } catch (Exception e) {
            log.error("Error al consumir mensaje de BPM principal", e);
        }
    }
    
    private void procesarGestionCitas(Map<String, Object> data, String processInstanceId) {
        log.info("[{}] Procesando Gestión de Citas", processInstanceId);
        // Lógica de procesamiento de citas
        Long pacienteId = extractLongValue(data.get("pacienteId"));
        Long terapeutaId = extractLongValue(data.get("terapeutaId"));
        log.info("[{}] Cita: Paciente={}, Terapeuta={}", processInstanceId, pacienteId, terapeutaId);
    }
    
    private void procesarGestionPacientes(Map<String, Object> data, String processInstanceId) {
        log.info("[{}] Procesando Gestión de Pacientes", processInstanceId);
        // Lógica de procesamiento de pacientes
        String nombre = (String) data.get("nombre");
        String email = (String) data.get("email");
        log.info("[{}] Paciente: Nombre={}, Email={}", processInstanceId, nombre, email);
    }
    
    private void procesarGestionTerapeutas(Map<String, Object> data, String processInstanceId) {
        log.info("[{}] Procesando Gestión de Terapeutas", processInstanceId);
        String nombre = (String) data.get("nombre");
        String especialidad = (String) data.get("especialidad");
        log.info("[{}] Terapeuta: Nombre={}, Especialidad={}", processInstanceId, nombre, especialidad);
    }
    
    private void procesarGestionDisciplinas(Map<String, Object> data, String processInstanceId) {
        log.info("[{}] Procesando Gestión de Disciplinas", processInstanceId);
        String disciplina = (String) data.get("disciplina");
        log.info("[{}] Disciplina: {}", processInstanceId, disciplina);
    }
    
    private void procesarGestionHorarios(Map<String, Object> data, String processInstanceId) {
        log.info("[{}] Procesando Gestión de Horarios", processInstanceId);
        String horario = (String) data.get("horario");
        log.info("[{}] Horario: {}", processInstanceId, horario);
    }
    
    private void procesarGestionPagos(Map<String, Object> data, String processInstanceId) {
        log.info("[{}] Procesando Gestión de Pagos", processInstanceId);
        Object monto = data.get("monto");
        String metodoPago = (String) data.get("metodoPago");
        log.info("[{}] Pago: Monto={}, Método={}", processInstanceId, monto, metodoPago);
    }
    
    private void procesarGestionEvaluaciones(Map<String, Object> data, String processInstanceId) {
        log.info("[{}] Procesando Gestión de Evaluaciones", processInstanceId);
        Long pacienteId = extractLongValue(data.get("pacienteId"));
        String resultado = (String) data.get("resultado");
        log.info("[{}] Evaluación: Paciente={}, Resultado={}", processInstanceId, pacienteId, resultado);
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