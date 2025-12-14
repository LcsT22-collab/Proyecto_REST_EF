package com.clinica.jms.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.clinica.model.dto.bpm.SolicitudCitaBPM;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BpmErrorConsumer {

    @JmsListener(destination = "bpm.error.queue")
    public void procesarError(SolicitudCitaBPM solicitud) {
        log.error("ERROR CRÍTICO EN PROCESO BPM: {}", solicitud.getIdProceso());
        log.error("Mensaje de error: {}", solicitud.getErrorMensaje());
        
        notificarAdministradores(solicitud);
    }

    private void notificarAdministradores(SolicitudCitaBPM solicitud) {
        log.warn("Notificando administradores sobre error en proceso: {}", solicitud.getIdProceso());
    }
}