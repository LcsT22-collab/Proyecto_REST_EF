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
import org.springframework.web.bind.annotation.*;

import com.clinica.model.dto.SalaDTO;
import com.clinica.model.entity.SalaEntity;
import com.clinica.service.SalaService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Salas", description = "Operaciones CRUD para la gestión de salas")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @GetMapping("/listar/salas")
    @Operation(summary = "Listar todas las salas", description = "Obtiene una lista completa de todas las salas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de salas obtenida exitosamente")
    public List<SalaDTO> listarSalas() {
        List<SalaEntity> salas = salaService.findAll();
        return salas.stream()
                .map(this::convertirADto)
                .toList();
    }

    @GetMapping("/sala/{id}")
    @Operation(summary = "Obtener sala por ID", description = "Busca una sala específica por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sala no encontrada")
    })
    public ResponseEntity<SalaDTO> obtenerSala(
            @Parameter(description = "ID de la sala", example = "1", required = true) @PathVariable Long id) {
        return salaService.findById(id)
                .map(sala -> ResponseEntity.ok(convertirADto(sala)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/sala")
    @Operation(summary = "Crear nueva sala", description = "Registra una nueva sala en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sala creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la sala inválidos")
    })
    public ResponseEntity<SalaDTO> crearSala(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la sala a crear", required = true) @RequestBody SalaDTO salaDTO) {
        SalaEntity sala = convertirAEntidad(salaDTO);
        SalaEntity nuevaSala = salaService.save(sala);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADto(nuevaSala));
    }

    @PutMapping("/editar/sala/{id}")
    @Operation(summary = "Actualizar sala", description = "Actualiza la información de una sala existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sala no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<SalaDTO> actualizarSala(
            @Parameter(description = "ID de la sala a actualizar", example = "1", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos de la sala", required = true) @RequestBody SalaDTO salaDTO) {
        SalaEntity sala = convertirAEntidad(salaDTO);
        SalaEntity salaActualizada = salaService.update(id, sala);
        return ResponseEntity.ok(convertirADto(salaActualizada));
    }

    @DeleteMapping("/eliminar/sala/{id}")
    @Operation(summary = "Eliminar sala", description = "Elimina una sala del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sala eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sala no encontrada")
    })
    public ResponseEntity<Void> eliminarSala(
            @Parameter(description = "ID de la sala a eliminar", example = "1", required = true) @PathVariable Long id) {
        salaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private SalaDTO convertirADto(SalaEntity sala) {
        SalaDTO dto = new SalaDTO();
        dto.setIdSala(sala.getIdSala());
        dto.setNombre(sala.getNombre());
        dto.setNumero(sala.getNumero());
        dto.setCapacidad(sala.getCapacidad());
        dto.setPiso(sala.getPiso());
        dto.setDisponible(sala.getDisponible());
        dto.setEquipamiento(sala.getEquipamiento());
        dto.setFechaCreacion(sala.getFechaCreacion());
        dto.setFechaActualizacion(sala.getFechaActualizacion());
        return dto;
    }

    private SalaEntity convertirAEntidad(SalaDTO dto) {
        SalaEntity sala = new SalaEntity();
        sala.setNombre(dto.getNombre());
        sala.setNumero(dto.getNumero());
        sala.setCapacidad(dto.getCapacidad());
        sala.setPiso(dto.getPiso());
        sala.setDisponible(dto.getDisponible());
        sala.setEquipamiento(dto.getEquipamiento());
        return sala;
    }
}
