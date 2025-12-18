package com.clinica.soap;

import com.clinica.model.dto.PacienteDTO;
import com.clinica.service.impl.PacienteServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

/**
 * Endpoint SOAP para Pacientes
 * Proporciona operaciones SOAP para gestión de pacientes
 */
@Endpoint
@Slf4j
@RequiredArgsConstructor
@Tag(name = "SOAP Pacientes", description = "Servicio SOAP para gestión de pacientes")
public class PacienteSOAPService {
    
    private static final String NAMESPACE_URI = "http://clinica.com/soap/paciente";
    private final PacienteServiceImpl pacienteService;
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CrearPacienteRequest")
    @ResponsePayload
    public CrearPacienteResponse crearPaciente(@RequestPayload CrearPacienteRequest request) {
        log.info("SOAP: Creando paciente {}", request.getNombre());
        
        CrearPacienteResponse response = new CrearPacienteResponse();
        try {
            PacienteDTO pacienteDTO = new PacienteDTO();
            pacienteDTO.setNombre(request.getNombre());
            pacienteDTO.setApellido(request.getApellido());
            pacienteDTO.setEmail(request.getEmail());
            pacienteDTO.setTelefono(request.getTelefono());
            
            PacienteDTO creado = pacienteService.crearPaciente(pacienteDTO);
            response.setIdPaciente(creado.getIdPaciente());
            response.setMensaje("Paciente creado exitosamente");
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error creando paciente SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ObtenerPacienteRequest")
    @ResponsePayload
    public ObtenerPacienteResponse obtenerPaciente(@RequestPayload ObtenerPacienteRequest request) {
        log.info("SOAP: Obteniendo paciente {}", request.getIdPaciente());
        
        ObtenerPacienteResponse response = new ObtenerPacienteResponse();
        try {
            PacienteDTO paciente = pacienteService.obtenerPacientePorId(request.getIdPaciente());
            response.setPaciente(paciente);
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error obteniendo paciente SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListarPacientesRequest")
    @ResponsePayload
    public ListarPacientesResponse listarPacientes(@RequestPayload ListarPacientesRequest request) {
        log.info("SOAP: Listando pacientes");
        
        ListarPacientesResponse response = new ListarPacientesResponse();
        try {
            List<PacienteDTO> pacientes = pacienteService.obtenerTodosLosPacientes();
            response.setPacientes(pacientes);
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error listando pacientes SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ActualizarPacienteRequest")
    @ResponsePayload
    public ActualizarPacienteResponse actualizarPaciente(@RequestPayload ActualizarPacienteRequest request) {
        log.info("SOAP: Actualizando paciente {}", request.getIdPaciente());
        
        ActualizarPacienteResponse response = new ActualizarPacienteResponse();
        try {
            PacienteDTO pacienteDTO = new PacienteDTO();
            pacienteDTO.setNombre(request.getNombre());
            pacienteDTO.setApellido(request.getApellido());
            pacienteDTO.setEmail(request.getEmail());
            pacienteDTO.setTelefono(request.getTelefono());
            
            PacienteDTO actualizado = pacienteService.actualizarPaciente(request.getIdPaciente(), pacienteDTO);
            response.setPaciente(actualizado);
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error actualizando paciente SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "EliminarPacienteRequest")
    @ResponsePayload
    public EliminarPacienteResponse eliminarPaciente(@RequestPayload EliminarPacienteRequest request) {
        log.info("SOAP: Eliminando paciente {}", request.getIdPaciente());
        
        EliminarPacienteResponse response = new EliminarPacienteResponse();
        try {
            pacienteService.eliminarPaciente(request.getIdPaciente());
            response.setMensaje("Paciente eliminado exitosamente");
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error eliminando paciente SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
}

// DTOs para SOAP
class CrearPacienteRequest {
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

class CrearPacienteResponse {
    private Long idPaciente;
    private String mensaje;
    private Boolean exitoso;
    
    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
}

class ObtenerPacienteRequest {
    private Long idPaciente;
    
    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }
}

class ObtenerPacienteResponse {
    private PacienteDTO paciente;
    private Boolean exitoso;
    private String mensaje;
    
    public PacienteDTO getPaciente() { return paciente; }
    public void setPaciente(PacienteDTO paciente) { this.paciente = paciente; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}

class ListarPacientesRequest {}

class ListarPacientesResponse {
    private List<PacienteDTO> pacientes;
    private Boolean exitoso;
    private String mensaje;
    
    public List<PacienteDTO> getPacientes() { return pacientes; }
    public void setPacientes(List<PacienteDTO> pacientes) { this.pacientes = pacientes; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}

class ActualizarPacienteRequest {
    private Long idPaciente;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    
    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}

class ActualizarPacienteResponse {
    private PacienteDTO paciente;
    private Boolean exitoso;
    private String mensaje;
    
    public PacienteDTO getPaciente() { return paciente; }
    public void setPaciente(PacienteDTO paciente) { this.paciente = paciente; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}

class EliminarPacienteRequest {
    private Long idPaciente;
    
    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }
}

class EliminarPacienteResponse {
    private String mensaje;
    private Boolean exitoso;
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
}
