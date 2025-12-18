package com.clinica.jms.consumer;

import com.clinica.model.dto.bpm.ProcessStartResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BpmNotificacionConsumer {
    
    private final ObjectMapper objectMapper;
    
    /**
     * Consume eventos de notificación de procesos BPMN
     */
    @JmsListener(destination = "bpm.notificaciones.queue", containerFactory = "jmsListenerContainerFactory")
    public void consumeNotificacion(String message) {
        log.info("BpmNotificacionConsumer: Notificación recibida");
        
        try {
            ProcessStartResponse response = objectMapper.readValue(message, ProcessStartResponse.class);
            log.info("Enviando notificación para proceso: {}", response.getProcessId());
            
            enviarNotificacion(response);
            
        } catch (Exception e) {
            log.error("Error al consumir notificación BPM", e);
        }
    }
    
    private void enviarNotificacion(ProcessStartResponse response) {
        Object notificacion = response.getVariables().get("notificacion");
        if (notificacion != null) {
            log.info("Notificación: {}", notificacion.toString());
        }
    }
}

@Slf4j
public class BpmNotificacionConsumer {

    @JmsListener(destination = "bpm.notificaciones.queue")
    public void procesarNotificacion(EventoProcesoBPM evento) {
        log.info("Recibida notificación: {} - {}", evento.getIdProceso(), evento.getMensaje());
        enviarEmailNotificacion(evento);
    }

    private void enviarEmailNotificacion(EventoProcesoBPM evento) {
        log.info("Enviando email de notificación para proceso: {}", evento.getIdProceso());
    }
}