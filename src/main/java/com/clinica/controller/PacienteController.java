package com.clinica.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.model.dto.PacienteDTO;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.service.PacienteService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Pacientes", description = "Operaciones CRUD para la gestión de pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/listar/pacientes")
    @Operation(summary = "Listar todos los pacientes", description = "Obtiene una lista completa de todos los pacientes registrados")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida exitosamente")
    public List<PacienteDTO> listarPacientes() {
        List<PacientesEntity> pacientes = pacienteService.findAll();
        return pacientes.stream()
                .map(this::convertirADto)
                .toList();
    }

    @GetMapping("/paciente/{id}")
    @Operation(summary = "Obtener paciente por ID", description = "Busca un paciente específico por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<PacienteDTO> obtenerPaciente(
            @Parameter(description = "ID del paciente", example = "1", required = true) @PathVariable Long id) {
        return pacienteService.findById(id)
                .map(paciente -> ResponseEntity.ok(convertirADto(paciente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/paciente")
    @Operation(summary = "Crear nuevo paciente", description = "Registra un nuevo paciente en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del paciente inválidos")
    })
    public ResponseEntity<PacienteDTO> crearPaciente(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del paciente a crear", required = true) @RequestBody PacienteDTO pacienteDTO) {
        PacientesEntity paciente = convertirAEntidad(pacienteDTO);
        PacientesEntity nuevoPaciente = pacienteService.save(paciente);
        PacienteDTO respuesta = convertirADto(nuevoPaciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/editar/paciente/{id}")
    @Operation(summary = "Actualizar paciente", description = "Actualiza la información de un paciente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<PacienteDTO> actualizarPaciente(
            @Parameter(description = "ID del paciente a actualizar", example = "1", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del paciente", required = true) @RequestBody PacienteDTO pacienteDTO) {
        PacientesEntity paciente = convertirAEntidad(pacienteDTO);
        PacientesEntity pacienteActualizado = pacienteService.update(id, paciente);
        return ResponseEntity.ok(convertirADto(pacienteActualizado));
    }

    @DeleteMapping("/eliminar/paciente/{id}")
    @Operation(summary = "Eliminar paciente", description = "Elimina un paciente del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<Void> eliminarPaciente(
            @Parameter(description = "ID del paciente a eliminar", example = "1", required = true) @PathVariable Long id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PacienteDTO convertirADto(PacientesEntity paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setIdPaciente(paciente.getIdPaciente());
        dto.setNombre(paciente.getNombre());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setNombreTutor(paciente.getNombreTutor());
        dto.setTelefono(paciente.getTelefono());
        dto.setCorreo(paciente.getCorreo());
        dto.setFechaRegistro(paciente.getFechaRegistro());
        return dto;
    }

    private PacientesEntity convertirAEntidad(PacienteDTO dto) {
        PacientesEntity paciente = new PacientesEntity();
        paciente.setNombre(dto.getNombre());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setNombreTutor(dto.getNombreTutor());
        paciente.setTelefono(dto.getTelefono());
        paciente.setCorreo(dto.getCorreo());
        paciente.setFechaRegistro(dto.getFechaRegistro());
        return paciente;
    }
}