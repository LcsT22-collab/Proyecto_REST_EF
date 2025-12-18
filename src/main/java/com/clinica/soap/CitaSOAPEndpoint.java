package com.clinica.soap;

import com.clinica.model.dto.CitaDTO;
import com.clinica.service.impl.CitaServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

/**
 * Endpoint SOAP para Citas
 */
@Endpoint
@Slf4j
@RequiredArgsConstructor
public class CitaSOAPEndpoint {
    
    private static final String NAMESPACE_URI = "http://clinica.com/soap/cita";
    private final CitaServiceImpl citaService;
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CrearCitaRequest")
    @ResponsePayload
    public CrearCitaResponse crearCita(@RequestPayload CrearCitaRequest request) {
        log.info("SOAP: Creando cita para paciente {}", request.getPacienteId());
        
        CrearCitaResponse response = new CrearCitaResponse();
        try {
            CitaDTO citaDTO = new CitaDTO();
            citaDTO.setPacienteId(request.getPacienteId());
            citaDTO.setTerapeutaId(request.getTerapeutaId());
            citaDTO.setFechaHora(request.getFechaHora());
            citaDTO.setMotivo(request.getMotivo());
            
            CitaDTO creada = citaService.crearCita(citaDTO);
            response.setIdCita(creada.getIdCita());
            response.setMensaje("Cita creada exitosamente");
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error creando cita SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConfirmarCitaRequest")
    @ResponsePayload
    public ConfirmarCitaResponse confirmarCita(@RequestPayload ConfirmarCitaRequest request) {
        log.info("SOAP: Confirmando cita {}", request.getIdCita());
        
        ConfirmarCitaResponse response = new ConfirmarCitaResponse();
        try {
            CitaDTO confirmada = citaService.confirmarCita(request.getIdCita());
            response.setCita(confirmada);
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error confirmando cita SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CancelarCitaRequest")
    @ResponsePayload
    public CancelarCitaResponse cancelarCita(@RequestPayload CancelarCitaRequest request) {
        log.info("SOAP: Cancelando cita {}", request.getIdCita());
        
        CancelarCitaResponse response = new CancelarCitaResponse();
        try {
            CitaDTO cancelada = citaService.cancelarCita(request.getIdCita());
            response.setCita(cancelada);
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error cancelando cita SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
}

class CrearCitaRequest {
    private Long pacienteId;
    private Long terapeutaId;
    private String fechaHora;
    private String motivo;
    
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public Long getTerapeutaId() { return terapeutaId; }
    public void setTerapeutaId(Long terapeutaId) { this.terapeutaId = terapeutaId; }
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}

class CrearCitaResponse {
    private Long idCita;
    private String mensaje;
    private Boolean exitoso;
    
    public Long getIdCita() { return idCita; }
    public void setIdCita(Long idCita) { this.idCita = idCita; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
}

class ConfirmarCitaRequest {
    private Long idCita;
    
    public Long getIdCita() { return idCita; }
    public void setIdCita(Long idCita) { this.idCita = idCita; }
}

class ConfirmarCitaResponse {
    private CitaDTO cita;
    private Boolean exitoso;
    private String mensaje;
    
    public CitaDTO getCita() { return cita; }
    public void setCita(CitaDTO cita) { this.cita = cita; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}

class CancelarCitaRequest {
    private Long idCita;
    
    public Long getIdCita() { return idCita; }
    public void setIdCita(Long idCita) { this.idCita = idCita; }
}

class CancelarCitaResponse {
    private CitaDTO cita;
    private Boolean exitoso;
    private String mensaje;
    
    public CitaDTO getCita() { return cita; }
    public void setCita(CitaDTO cita) { this.cita = cita; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
