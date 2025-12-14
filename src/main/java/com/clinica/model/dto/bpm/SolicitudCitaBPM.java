package com.clinica.model.dto.bpm;

import com.clinica.model.enums.EstadoProcesoBPM;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudCitaBPM {
    private String idProceso;
    private Long pacienteId;
    private Long terapeutaId;
    private String tipoCita;
    private LocalDateTime fechaSolicitud;
    private Map<String, Object> datosAdicionales = new HashMap<>();
    private EstadoProcesoBPM estado = EstadoProcesoBPM.SOLICITADO;
    private String errorMensaje;
    private String idTransaccion;
}