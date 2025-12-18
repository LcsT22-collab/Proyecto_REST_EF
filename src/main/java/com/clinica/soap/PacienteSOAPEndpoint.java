package com.clinica.soap;

import com.clinica.model.dto.PacienteDTO;
import com.clinica.model.dto.bpm.ProcessStartRequest;
import com.clinica.model.dto.bpm.ProcessStartResponse;
import com.clinica.service.PacienteService;
import com.clinica.service.bpm.BpmProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.HashMap;
import java.util.Map;

@Endpoint
@RequiredArgsConstructor
public class PacienteSOAPEndpoint {
    
    private static final String NAMESPACE_URI = "http://clinica.com/soap/pacientes";
    
    private final PacienteService pacienteService;
    private final BpmProcessService bpmProcessService;
    
    /**
     * SOAP Endpoint para crear pacientes
     * Dispara el proceso BPMN GestionPacientes
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CrearPacienteRequest")
    @ResponsePayload
    public CrearPacienteResponse crearPaciente(@RequestPayload CrearPacienteRequest request) {
        
        try {
            // Preparar DTO
            PacienteDTO pacienteDTO = new PacienteDTO();
            pacienteDTO.setNombre(request.getNombre());
            pacienteDTO.setApellido(request.getApellido());
            pacienteDTO.setEmail(request.getEmail());
            pacienteDTO.setTelefono(request.getTelefono());
            
            // Preparar variables para BPMN
            Map<String, Object> variables = new HashMap<>();
            variables.put("nombre", request.getNombre());
            variables.put("apellido", request.getApellido());
            variables.put("email", request.getEmail());
            variables.put("telefono", request.getTelefono());
            
            ProcessStartRequest processRequest = new ProcessStartRequest();
            processRequest.setProcessId("GestionPacientes");
            processRequest.setVariables(variables);
            
            // Iniciar proceso
            ProcessStartResponse processResponse = bpmProcessService.startProcess(processRequest);
            
            // Preparar respuesta SOAP
            CrearPacienteResponse response = new CrearPacienteResponse();
            response.setExitoso(processResponse.getStatus().equals("COMPLETADO"));
            response.setMensaje(processResponse.getStatus());
            response.setProcessInstanceId(processResponse.getProcessInstanceId());
            
            return response;
        } catch (Exception e) {
            CrearPacienteResponse response = new CrearPacienteResponse();
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
            return response;
        }
    }
    
    /**
     * Clases internas para SOAP
     */
    public static class CrearPacienteRequest {
        private String nombre;
        private String apellido;
        private String email;
        private String telefono;
        
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
    }
    
    public static class CrearPacienteResponse {
        private Boolean exitoso;
        private String mensaje;
        private Long processInstanceId;
        
        public Boolean getExitoso() { return exitoso; }
        public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
        
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
        
        public Long getProcessInstanceId() { return processInstanceId; }
        public void setProcessInstanceId(Long processInstanceId) { this.processInstanceId = processInstanceId; }
    }
}
