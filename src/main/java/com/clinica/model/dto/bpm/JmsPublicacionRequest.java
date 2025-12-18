package com.clinica.model.dto.bpm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JmsPublicacionRequest {

    @JsonProperty("cola")
    private String cola;

    @JsonProperty("mensaje")
    private Object mensaje;

    public JmsPublicacionRequest() {
    }

    public JmsPublicacionRequest(String cola, Object mensaje) {
        this.cola = cola;
        this.mensaje = mensaje;
    }

    public String getCola() {
        return cola;
    }

    public void setCola(String cola) {
        this.cola = cola;
    }

    public Object getMensaje() {
        return mensaje;
    }

    public void setMensaje(Object mensaje) {
        this.mensaje = mensaje;
    }
}
