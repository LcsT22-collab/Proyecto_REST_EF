package com.clinica.controller;

import com.clinica.model.dto.CitaDTO;
import com.clinica.model.dto.bpm.ProcessStartRequest;
import com.clinica.model.dto.bpm.ProcessStartResponse;
import com.clinica.model.entity.CitasEntity;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.entity.TerapeutaEntity;
import com.clinica.service.CitaService;
import com.clinica.service.bpm.BpmProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/citas")
@Tag(name = "Citas", description = "Gestión de citas médicas con BPMN")
@Slf4j
public class CitaController {

    private final CitaService citaService;
    private final BpmProcessService bpmProcessService;

    public CitaController(CitaService citaService, BpmProcessService bpmProcessService) {
        this.citaService = citaService;
        this.bpmProcessService = bpmProcessService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las citas", description = "Obtiene la lista completa de citas")
    @ApiResponse(responseCode = "200", description = "Lista de citas obtenida exitosamente")
    public ResponseEntity<List<CitaDTO>> getAllCitas() {
        List<CitaDTO> citas = citaService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cita por ID", description = "Busca una cita específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita encontrada"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    public ResponseEntity<CitaDTO> getCitaById(
            @Parameter(description = "ID de la cita", required = true) @PathVariable Long id) {
        return citaService.findById(id)
                .map(cita -> ResponseEntity.ok(convertToDTO(cita)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva cita", description = "Crea una nueva cita médica disparando el proceso BPMN GestionCitas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ProcessStartResponse> createCita(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la cita", required = true)
            @RequestBody CitaDTO citaDTO) {
        log.info("POST /api/v1/citas - Creando cita para paciente: {}", citaDTO.getPacienteId());
        try {
            // Preparar variables para el proceso BPMN GestionCitas
            Map<String, Object> variables = new HashMap<>();
            variables.put("email", citaDTO.getEmail());
            variables.put("pacienteId", citaDTO.getPacienteId());
            variables.put("terapeutaId", citaDTO.getTerapeutaId());
            variables.put("horarioId", citaDTO.getHorarioId());
            variables.put("motivo", citaDTO.getMotivo());
            
            ProcessStartRequest request = new ProcessStartRequest();
            request.setProcessId("GestionCitas");
            request.setVariables(variables);
            
            // Iniciar proceso BPMN
            ProcessStartResponse response = bpmProcessService.startProcess(request);
            log.info("Cita creada exitosamente con proceso BPMN ID: {}", response.getProcessInstanceId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al crear cita", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cita", description = "Actualiza los datos de una cita existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita actualizada"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<CitaDTO> updateCita(
            @Parameter(description = "ID de la cita", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos de la cita", required = true)
            @RequestBody CitaDTO citaDTO) {
        CitasEntity cita = convertToEntity(citaDTO);
        CitasEntity updatedCita = citaService.update(id, cita);
        return ResponseEntity.ok(convertToDTO(updatedCita));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar cita", description = "Cancela una cita (marca como cancelada)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cita cancelada"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    public ResponseEntity<Void> deleteCita(
            @Parameter(description = "ID de la cita", required = true) @PathVariable Long id) {
        citaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CitaDTO convertToDTO(CitasEntity cita) {
        CitaDTO dto = new CitaDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setFechaCita(cita.getFechaCita());
        dto.setDuracionMinutos(cita.getDuracionMinutos());
        dto.setEstadoCita(cita.getEstadoCita());
        dto.setObservaciones(cita.getObservaciones());
        
        if (cita.getPaciente() != null) {
            dto.setPacienteId(cita.getPaciente().getIdPaciente());
            dto.setNombrePaciente(cita.getPaciente().getNombre());
        }
        
        if (cita.getTerapeuta() != null) {
            dto.setTerapeutaId(cita.getTerapeuta().getIdTerapeuta());
            dto.setNombreTerapeuta(cita.getTerapeuta().getNombre());
        }
        
        return dto;
    }

    private CitasEntity convertToEntity(CitaDTO dto) {
        CitasEntity cita = new CitasEntity();
        cita.setFechaCita(dto.getFechaCita());
        cita.setDuracionMinutos(dto.getDuracionMinutos());
        cita.setEstadoCita(dto.getEstadoCita());
        cita.setObservaciones(dto.getObservaciones());
        
        if (dto.getPacienteId() != null) {
            PacientesEntity paciente = new PacientesEntity();
            paciente.setIdPaciente(dto.getPacienteId());
            cita.setPaciente(paciente);
        }
        
        if (dto.getTerapeutaId() != null) {
            TerapeutaEntity terapeuta = new TerapeutaEntity();
            terapeuta.setIdTerapeuta(dto.getTerapeutaId());
            cita.setTerapeuta(terapeuta);
        }
        
        return cita;
    }
}