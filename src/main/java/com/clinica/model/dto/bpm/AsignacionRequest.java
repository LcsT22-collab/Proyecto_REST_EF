package com.clinica.model.dto.bpm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AsignacionRequest {

    @JsonProperty("terapeutaId")
    private Long terapeutaId;

    @JsonProperty("tipoCita")
    private String tipoCita;

    @JsonProperty("idTransaccion")
    private String idTransaccion;

    public AsignacionRequest() {
    }

    public AsignacionRequest(Long terapeutaId, String tipoCita, String idTransaccion) {
        this.terapeutaId = terapeutaId;
        this.tipoCita = tipoCita;
        this.idTransaccion = idTransaccion;
    }

    public Long getTerapeutaId() {
        return terapeutaId;
    }

    public void setTerapeutaId(Long terapeutaId) {
        this.terapeutaId = terapeutaId;
    }

    public String getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(String tipoCita) {
        this.tipoCita = tipoCita;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }
}
