package com.clinica.service.bpm;

import com.clinica.model.dto.bpm.ProcessStartRequest;
import com.clinica.model.dto.bpm.ProcessStartResponse;
import com.clinica.model.enums.EstadoProcesoBPM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Servicio para gestionar procesos BPM con integración JMS
 * Maneja orquestación de procesos y flujos de excepción
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BpmProcessServiceImpl implements BpmProcessService {
    
    private final JmsTemplate jmsTemplate;
    private static final String BPM_MAIN_QUEUE = "bpm.main.queue";
    private static final String BPM_ERROR_QUEUE = "bpm.error.queue";
    private static final String BPM_COMPENSATION_QUEUE = "bpm.compensacion.queue";
    
    /**
     * Inicia un nuevo proceso BPMN con las variables necesarias
     */
    @Override
    public ProcessStartResponse startProcess(ProcessStartRequest request) {
        log.info("Iniciando proceso BPMN: {}", request.getProcessId());
        
        ProcessStartResponse response = new ProcessStartResponse();
        String processInstanceId = UUID.randomUUID().toString();
        
        try {
            response.setProcessInstanceId(processInstanceId);
            response.setProcessId(request.getProcessId());
            response.setStartTime(LocalDateTime.now());
            response.setStatus(EstadoProcesoBPM.INICIADO.toString());
            
            // Preparar mensaje para JMS
            Map<String, Object> messageData = new HashMap<>(request.getVariables());
            messageData.put("processInstanceId", processInstanceId);
            messageData.put("processId", request.getProcessId());
            messageData.put("timestamp", LocalDateTime.now());
            
            // Enviar a cola principal
            jmsTemplate.convertAndSend(BPM_MAIN_QUEUE, messageData);
            
            log.info("Proceso {} iniciado exitosamente - Instancia: {}", 
                    request.getProcessId(), processInstanceId);
            
            response.setSuccess(true);
            response.setMessage("Proceso iniciado exitosamente");
            
        } catch (Exception e) {
            log.error("Error iniciando proceso BPMN: {}", request.getProcessId(), e);
            
            response.setSuccess(false);
            response.setStatus(EstadoProcesoBPM.ERROR.toString());
            response.setMessage("Error iniciando proceso: " + e.getMessage());
            
            // Enviar a cola de errores
            enviarErrorJMS(request, processInstanceId, e);
        }
        
        return response;
    }
    
    /**
     * Maneja errores del proceso y envía a cola de compensación
     */
    @Override
    public void handleProcessError(String processInstanceId, String processId, Exception error) {
        log.error("Manejando error en proceso {} - Instancia: {}", processId, processInstanceId);
        
        try {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("processInstanceId", processInstanceId);
            errorData.put("processId", processId);
            errorData.put("errorMessage", error.getMessage());
            errorData.put("errorType", error.getClass().getSimpleName());
            errorData.put("timestamp", LocalDateTime.now());
            errorData.put("status", EstadoProcesoBPM.ERROR.toString());
            
            // Enviar a cola de errores
            jmsTemplate.convertAndSend(BPM_ERROR_QUEUE, errorData);
            
            log.info("Error enviado a cola de errores para proceso {}", processId);
            
        } catch (Exception e) {
            log.error("Error enviando mensaje a cola de errores", e);
        }
    }
    
    /**
     * Inicia compensación de un proceso fallido
     */
    @Override
    public void compensateProcess(String processInstanceId, String processId) {
        log.info("Iniciando compensación para proceso {} - Instancia: {}", processId, processInstanceId);
        
        try {
            Map<String, Object> compensationData = new HashMap<>();
            compensationData.put("processInstanceId", processInstanceId);
            compensationData.put("processId", processId);
            compensationData.put("compensationType", "ROLLBACK");
            compensationData.put("timestamp", LocalDateTime.now());
            compensationData.put("status", EstadoProcesoBPM.COMPENSACION.toString());
            
            // Enviar a cola de compensación
            jmsTemplate.convertAndSend(BPM_COMPENSATION_QUEUE, compensationData);
            
            log.info("Proceso de compensación enviado para {}", processId);
            
        } catch (Exception e) {
            log.error("Error enviando mensaje de compensación", e);
        }
    }
    
    /**
     * Método helper para enviar errores a JMS
     */
    private void enviarErrorJMS(ProcessStartRequest request, String processInstanceId, Exception e) {
        try {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("processId", request.getProcessId());
            errorData.put("processInstanceId", processInstanceId);
            errorData.put("errorMessage", e.getMessage());
            errorData.put("timestamp", LocalDateTime.now());
            errorData.put("variables", request.getVariables());
            
            jmsTemplate.convertAndSend(BPM_ERROR_QUEUE, errorData);
            
        } catch (Exception jmsError) {
            log.error("Error enviando a cola de errores", jmsError);
        }
    }
}
