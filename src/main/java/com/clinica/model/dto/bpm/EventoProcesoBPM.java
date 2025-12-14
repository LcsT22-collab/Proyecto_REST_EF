package com.clinica.model.dto.bpm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoProcesoBPM {
    private String idProceso;
    private String tipoEvento;
    private String mensaje;
    private LocalDateTime timestamp;
    private Object datos;
}