package com.clinica.jms.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.clinica.model.dto.bpm.EventoProcesoBPM;

import lombok.extern.slf4j.Slf4j;

@Component
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