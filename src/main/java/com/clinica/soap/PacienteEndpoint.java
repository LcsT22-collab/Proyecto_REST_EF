package com.clinica.soap;

import com.clinica.model.entity.PacientesEntity;
import com.clinica.service.PacienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
@Slf4j
public class PacienteEndpoint {
    private static final String NAMESPACE_URI = "http://clinica.com/pacientes";

    private final PacienteService pacienteService;

    public PacienteEndpoint(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConsultarPacienteRequest")
    @ResponsePayload
    public ConsultarPacienteResponse consultarPaciente(@RequestPayload ConsultarPacienteRequest request) {
        log.info("SOAP - Consultar paciente con ID: {}", request.getIdPaciente());
        
        ConsultarPacienteResponse response = new ConsultarPacienteResponse();
        
        PacientesEntity pacienteEntity = pacienteService.findById(request.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(pacienteEntity.getIdPaciente());
        paciente.setNombre(pacienteEntity.getNombre());
        paciente.setFechaNacimiento(pacienteEntity.getFechaNacimiento().toString());
        paciente.setNombreTutor(pacienteEntity.getNombreTutor());
        paciente.setTelefono(pacienteEntity.getTelefono());
        paciente.setCorreo(pacienteEntity.getCorreo());
        paciente.setActivo(pacienteEntity.getActivo());
        
        response.setPaciente(paciente);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListarPacientesRequest")
    @ResponsePayload
    public ListarPacientesResponse listarPacientes(@RequestPayload ListarPacientesRequest request) {
        log.info("SOAP - Listar todos los pacientes");
        
        ListarPacientesResponse response = new ListarPacientesResponse();
        List<PacientesEntity> pacientesEntities = pacienteService.findAll();
        
        for (PacientesEntity entity : pacientesEntities) {
            Paciente paciente = new Paciente();
            paciente.setIdPaciente(entity.getIdPaciente());
            paciente.setNombre(entity.getNombre());
            paciente.setFechaNacimiento(entity.getFechaNacimiento().toString());
            paciente.setNombreTutor(entity.getNombreTutor());
            paciente.setTelefono(entity.getTelefono());
            paciente.setCorreo(entity.getCorreo());
            paciente.setActivo(entity.getActivo());
            
            response.getPacientes().add(paciente);
        }
        
        return response;
    }

    // Clases internas para Request/Response (simplificadas)
    public static class ConsultarPacienteRequest {
        private Long idPaciente;
        public Long getIdPaciente() { return idPaciente; }
        public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }
    }

    public static class ConsultarPacienteResponse {
        private Paciente paciente;
        public Paciente getPaciente() { return paciente; }
        public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    }

    public static class ListarPacientesRequest {
        private Boolean activo;
        public Boolean getActivo() { return activo; }
        public void setActivo(Boolean activo) { this.activo = activo; }
    }

    public static class ListarPacientesResponse {
        private List<Paciente> pacientes = new java.util.ArrayList<>();
        public List<Paciente> getPacientes() { return pacientes; }
        public void setPacientes(List<Paciente> pacientes) { this.pacientes = pacientes; }
    }

    public static class Paciente {
        private Long idPaciente;
        private String nombre;
        private String fechaNacimiento;
        private String nombreTutor;
        private String telefono;
        private String correo;
        private Boolean activo;

        public Long getIdPaciente() { return idPaciente; }
        public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
        public String getNombreTutor() { return nombreTutor; }
        public void setNombreTutor(String nombreTutor) { this.nombreTutor = nombreTutor; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
        public Boolean getActivo() { return activo; }
        public void setActivo(Boolean activo) { this.activo = activo; }
    }
}
