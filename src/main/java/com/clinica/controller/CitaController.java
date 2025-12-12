package com.clinica.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
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

import com.clinica.model.dto.CitaDTO;
import com.clinica.model.entity.CitasEntity;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.entity.TerapeutaEntity;
import com.clinica.service.CitaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Citas", description = "Operaciones CRUD para la gestión de citas médicas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/listar/citas")
    @Operation(summary = "Listar todas las citas", description = "Obtiene una lista completa de todas las citas programadas")
    @ApiResponse(responseCode = "200", description = "Lista de citas obtenida exitosamente")
    public List<CitaDTO> listarCitas() {
        List<CitasEntity> citas = citaService.findAll();
        return citas.stream()
                .map(this::convertirADto)
                .toList();
    }

    @GetMapping("/cita/{id}")
    @Operation(summary = "Obtener cita por ID", description = "Busca una cita específica por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    public ResponseEntity<CitaDTO> obtenerCita(
            @Parameter(description = "ID de la cita", example = "1", required = true) @PathVariable Long id) {
        return citaService.findById(id)
                .map(cita -> ResponseEntity.ok(convertirADto(cita)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cita")
    @Operation(summary = "Crear nueva cita", description = "Programa una nueva cita médica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cita creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la cita inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto de horario o terapeuta no disponible")
    })
    public ResponseEntity<CitaDTO> crearCita(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la cita a crear", required = true) @RequestBody CitaDTO citaDTO) {
        CitasEntity cita = convertirAEntidad(citaDTO);
        CitasEntity nuevaCita = citaService.save(cita);
        CitaDTO respuesta = convertirADto(nuevaCita);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/editar/cita/{id}")
    @Operation(summary = "Actualizar cita", description = "Actualiza la información de una cita existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<CitaDTO> actualizarCita(
            @Parameter(description = "ID de la cita a actualizar", example = "1", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos de la cita", required = true) @RequestBody CitaDTO citaDTO) {
        CitasEntity cita = convertirAEntidad(citaDTO);
        CitasEntity citaActualizada = citaService.update(id, cita);
        return ResponseEntity.ok(convertirADto(citaActualizada));
    }

    @DeleteMapping("/eliminar/cita/{id}")
    @Operation(summary = "Eliminar cita", description = "Cancela una cita del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cita cancelada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    public ResponseEntity<Void> eliminarCita(
            @Parameter(description = "ID de la cita a cancelar", example = "1", required = true) @PathVariable Long id) {
        citaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CitaDTO convertirADto(CitasEntity cita) {
        CitaDTO dto = new CitaDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setFechaCita(cita.getFechaCita());
        dto.setEstadoCita(cita.getEstadoCita().toString());

        if (cita.getPaciente() != null) {
            dto.setIdPaciente(cita.getPaciente().getIdPaciente());
            dto.setNombrePaciente(cita.getPaciente().getNombre());
        }

        if (cita.getTerapeuta() != null) {
            dto.setIdTerapeuta(cita.getTerapeuta().getIdTerapeuta());
            dto.setNombreTerapeuta(cita.getTerapeuta().getNombre());
            dto.setEspecialidadTerapeuta(cita.getTerapeuta().getEspecialidad());
        }

        return dto;
    }

    private CitasEntity convertirAEntidad(CitaDTO dto) {
        CitasEntity cita = new CitasEntity();
        cita.setFechaCita(dto.getFechaCita());

        if (dto.getIdPaciente() != null) {
            PacientesEntity paciente = new PacientesEntity();
            paciente.setIdPaciente(dto.getIdPaciente());
            cita.setPaciente(paciente);
        }

        if (dto.getIdTerapeuta() != null) {
            TerapeutaEntity terapeuta = new TerapeutaEntity();
            terapeuta.setIdTerapeuta(dto.getIdTerapeuta());
            cita.setTerapeuta(terapeuta);
        }

        return cita;
    }
}