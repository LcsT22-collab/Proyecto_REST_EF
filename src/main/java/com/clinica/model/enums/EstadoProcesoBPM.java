package com.clinica.model.enums;

public enum EstadoProcesoBPM {
    SOLICITADO("Solicitud recibida"),
    VALIDANDO("Validando datos"),
    VALIDADO("Datos validados correctamente"),
    ASIGNANDO("Asignando recursos"),
    ASIGNADO("Recursos asignados"),
    CONFIRMANDO("Confirmando cita"),
    COMPLETADO("Proceso completado exitosamente"),
    ERROR("Error en el proceso"),
    COMPENSADO("Proceso compensado"),
    NOTIFICADO("Notificación enviada");

    private final String descripcion;

    EstadoProcesoBPM(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}	