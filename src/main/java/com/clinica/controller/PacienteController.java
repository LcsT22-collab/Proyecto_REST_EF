package com.clinica.controller;

import com.clinica.model.dto.PacienteDTO;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pacientes")
@Tag(name = "Pacientes", description = "Gestión de pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los pacientes", description = "Obtiene la lista completa de pacientes")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida exitosamente")
    public ResponseEntity<List<PacienteDTO>> getAllPacientes() {
        List<PacienteDTO> pacientes = pacienteService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener paciente por ID", description = "Busca un paciente específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<PacienteDTO> getPacienteById(
            @Parameter(description = "ID del paciente", required = true) @PathVariable Long id) {
        return pacienteService.findById(id)
                .map(paciente -> ResponseEntity.ok(convertToDTO(paciente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo paciente", description = "Crea un nuevo registro de paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<PacienteDTO> createPaciente(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del paciente", required = true)
            @RequestBody PacienteDTO pacienteDTO) {
        PacientesEntity paciente = convertToEntity(pacienteDTO);
        PacientesEntity savedPaciente = pacienteService.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedPaciente));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<PacienteDTO> updatePaciente(
            @Parameter(description = "ID del paciente", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del paciente", required = true)
            @RequestBody PacienteDTO pacienteDTO) {
        PacientesEntity paciente = convertToEntity(pacienteDTO);
        PacientesEntity updatedPaciente = pacienteService.update(id, paciente);
        return ResponseEntity.ok(convertToDTO(updatedPaciente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar paciente", description = "Desactiva un paciente (marca como inactivo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente desactivado"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<Void> deletePaciente(
            @Parameter(description = "ID del paciente", required = true) @PathVariable Long id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PacienteDTO convertToDTO(PacientesEntity paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setIdPaciente(paciente.getIdPaciente());
        dto.setNombre(paciente.getNombre());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setNombreTutor(paciente.getNombreTutor());
        dto.setTelefono(paciente.getTelefono());
        dto.setCorreo(paciente.getCorreo());
        dto.setDireccion(paciente.getDireccion());
        dto.setFechaRegistro(paciente.getFechaRegistro());
        dto.setActivo(paciente.getActivo());
        return dto;
    }

    private PacientesEntity convertToEntity(PacienteDTO dto) {
        PacientesEntity paciente = new PacientesEntity();
        paciente.setNombre(dto.getNombre());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setNombreTutor(dto.getNombreTutor());
        paciente.setTelefono(dto.getTelefono());
        paciente.setCorreo(dto.getCorreo());
        paciente.setDireccion(dto.getDireccion());
        paciente.setFechaRegistro(dto.getFechaRegistro());
        paciente.setActivo(dto.getActivo());
        return paciente;
    }
}