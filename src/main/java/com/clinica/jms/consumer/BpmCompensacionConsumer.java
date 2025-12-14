package com.clinica.jms.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.clinica.model.dto.bpm.SolicitudCitaBPM;
import com.clinica.service.bpm.BpmService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BpmCompensacionConsumer {

    private final BpmService bpmService;

    public BpmCompensacionConsumer(BpmService bpmService) {
        this.bpmService = bpmService;
    }

    @JmsListener(destination = "bpm.compensacion.queue")
    public void procesarCompensacion(SolicitudCitaBPM solicitud) {
        log.info("Recibida solicitud en cola de compensación: {}", solicitud.getIdProceso());
        bpmService.compensarProceso(solicitud);
    }
}

