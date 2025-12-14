package com.clinica.jms.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.clinica.model.dto.bpm.SolicitudCitaBPM;
import com.clinica.service.bpm.BpmService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BpmMainConsumer {

    private final BpmService bpmService;

    public BpmMainConsumer(BpmService bpmService) {
        this.bpmService = bpmService;
    }

    @JmsListener(destination = "bpm.main.queue")
    public void procesarSolicitud(SolicitudCitaBPM solicitud) {
        log.info("Recibida solicitud en cola principal: {}", solicitud.getIdProceso());
        bpmService.procesarSolicitudCita(solicitud);
    }
}