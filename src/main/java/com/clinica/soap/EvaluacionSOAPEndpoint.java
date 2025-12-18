package com.clinica.soap;

import com.clinica.model.dto.EvaluacionDTO;
import com.clinica.service.impl.EvaluacionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

/**
 * Endpoint SOAP para Evaluaciones
 */
@Endpoint
@Slf4j
@RequiredArgsConstructor
public class EvaluacionSOAPEndpoint {
    
    private static final String NAMESPACE_URI = "http://clinica.com/soap/evaluacion";
    private final EvaluacionServiceImpl evaluacionService;
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CrearEvaluacionRequest")
    @ResponsePayload
    public CrearEvaluacionResponse crearEvaluacion(@RequestPayload CrearEvaluacionRequest request) {
        log.info("SOAP: Creando evaluación para paciente {}", request.getPacienteId());
        
        CrearEvaluacionResponse response = new CrearEvaluacionResponse();
        try {
            EvaluacionDTO evaluacionDTO = new EvaluacionDTO();
            evaluacionDTO.setPacienteId(request.getPacienteId());
            evaluacionDTO.setResultado(request.getResultado());
            evaluacionDTO.setObservaciones(request.getObservaciones());
            
            EvaluacionDTO creada = evaluacionService.crearEvaluacion(evaluacionDTO);
            response.setIdEvaluacion(creada.getIdEvaluacion());
            response.setMensaje("Evaluación creada exitosamente");
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error creando evaluación SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ObtenerEvaluacionesPacienteRequest")
    @ResponsePayload
    public ObtenerEvaluacionesPacienteResponse obtenerEvaluacionesPaciente(
            @RequestPayload ObtenerEvaluacionesPacienteRequest request) {
        log.info("SOAP: Obteniendo evaluaciones para paciente {}", request.getPacienteId());
        
        ObtenerEvaluacionesPacienteResponse response = new ObtenerEvaluacionesPacienteResponse();
        try {
            List<EvaluacionDTO> evaluaciones = evaluacionService.obtenerEvaluacionesPorPaciente(request.getPacienteId());
            response.setEvaluaciones(evaluaciones);
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error obteniendo evaluaciones SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
}

class CrearEvaluacionRequest {
    private Long pacienteId;
    private String resultado;
    private String observaciones;
    
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}

class CrearEvaluacionResponse {
    private Long idEvaluacion;
    private String mensaje;
    private Boolean exitoso;
    
    public Long getIdEvaluacion() { return idEvaluacion; }
    public void setIdEvaluacion(Long idEvaluacion) { this.idEvaluacion = idEvaluacion; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
}

class ObtenerEvaluacionesPacienteRequest {
    private Long pacienteId;
    
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
}

class ObtenerEvaluacionesPacienteResponse {
    private List<EvaluacionDTO> evaluaciones;
    private Boolean exitoso;
    private String mensaje;
    
    public List<EvaluacionDTO> getEvaluaciones() { return evaluaciones; }
    public void setEvaluaciones(List<EvaluacionDTO> evaluaciones) { this.evaluaciones = evaluaciones; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
