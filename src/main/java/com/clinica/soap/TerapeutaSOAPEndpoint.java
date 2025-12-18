package com.clinica.soap;

import com.clinica.model.dto.TerapeutaDTO;
import com.clinica.service.impl.TerapeutaServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

/**
 * Endpoint SOAP para Terapeutas
 */
@Endpoint
@Slf4j
@RequiredArgsConstructor
public class TerapeutaSOAPEndpoint {
    
    private static final String NAMESPACE_URI = "http://clinica.com/soap/terapeuta";
    private final TerapeutaServiceImpl terapeutaService;
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CrearTerapeutaRequest")
    @ResponsePayload
    public CrearTerapeutaResponse crearTerapeuta(@RequestPayload CrearTerapeutaRequest request) {
        log.info("SOAP: Creando terapeuta {}", request.getNombre());
        
        CrearTerapeutaResponse response = new CrearTerapeutaResponse();
        try {
            TerapeutaDTO terapeutaDTO = new TerapeutaDTO();
            terapeutaDTO.setNombre(request.getNombre());
            terapeutaDTO.setApellido(request.getApellido());
            terapeutaDTO.setEspecialidad(request.getEspecialidad());
            terapeutaDTO.setTelefono(request.getTelefono());
            
            TerapeutaDTO creado = terapeutaService.crearTerapeuta(terapeutaDTO);
            response.setIdTerapeuta(creado.getIdTerapeuta());
            response.setMensaje("Terapeuta creado exitosamente");
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error creando terapeuta SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListarTerapeutasDisponiblesRequest")
    @ResponsePayload
    public ListarTerapeutasDisponiblesResponse listarDisponibles(@RequestPayload ListarTerapeutasDisponiblesRequest request) {
        log.info("SOAP: Listando terapeutas disponibles");
        
        ListarTerapeutasDisponiblesResponse response = new ListarTerapeutasDisponiblesResponse();
        try {
            List<TerapeutaDTO> terapeutas = terapeutaService.obtenerTerapeutasDisponibles();
            response.setTerapeutas(terapeutas);
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error listando terapeutas disponibles SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
}

class CrearTerapeutaRequest {
    private String nombre;
    private String apellido;
    private String especialidad;
    private String telefono;
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}

class CrearTerapeutaResponse {
    private Long idTerapeuta;
    private String mensaje;
    private Boolean exitoso;
    
    public Long getIdTerapeuta() { return idTerapeuta; }
    public void setIdTerapeuta(Long idTerapeuta) { this.idTerapeuta = idTerapeuta; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
}

class ListarTerapeutasDisponiblesRequest {}

class ListarTerapeutasDisponiblesResponse {
    private List<TerapeutaDTO> terapeutas;
    private Boolean exitoso;
    private String mensaje;
    
    public List<TerapeutaDTO> getTerapeutas() { return terapeutas; }
    public void setTerapeutas(List<TerapeutaDTO> terapeutas) { this.terapeutas = terapeutas; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
