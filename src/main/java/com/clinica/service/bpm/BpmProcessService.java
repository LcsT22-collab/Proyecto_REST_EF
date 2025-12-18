package com.clinica.service.bpm;

import com.clinica.model.dto.bpm.ProcessStartRequest;
import com.clinica.model.dto.bpm.ProcessStartResponse;
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
 * Servicio LEGACY para compatibilidad - usar BpmProcessServiceImpl
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BpmProcessService {
    
    private final JmsTemplate jmsTemplate;
    
    /**
     * Inicia un proceso BPMN
     */
    public ProcessStartResponse startProcess(ProcessStartRequest request) {
        log.info("Iniciando proceso BPMN: {}", request.getProcessId());
        
        ProcessStartResponse response = new ProcessStartResponse();
        long processInstanceId = System.nanoTime();
        
        try {
            response.setProcessInstanceId(processInstanceId);
            response.setProcessId(request.getProcessId());
            response.setStartTime(LocalDateTime.now());
            response.setStatus("INICIADO");
            
            // Preparar mensaje para JMS
            Map<String, Object> messageData = new HashMap<>(request.getVariables());
            messageData.put("processInstanceId", processInstanceId);
            messageData.put("processId", request.getProcessId());
            messageData.put("timestamp", LocalDateTime.now());
            
            // Enviar a cola principal
            jmsTemplate.convertAndSend("${jms.queue.bpm.main:bpm.main.queue}", messageData);
            
            log.info("Proceso {} iniciado exitosamente - Instancia: {}", 
                    request.getProcessId(), processInstanceId);
            
            response.setSuccess(true);
            response.setMessage("Proceso iniciado exitosamente");
            
        } catch (Exception e) {
            log.error("Error iniciando proceso BPMN: {}", request.getProcessId(), e);
            response.setSuccess(false);
            response.setStatus("ERROR");
            response.setMessage("Error iniciando proceso: " + e.getMessage());
        }
        
        return response;
    }
}
