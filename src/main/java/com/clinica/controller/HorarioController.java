package com.clinica.controller;

import com.clinica.model.dto.HorarioDTO;
import com.clinica.model.entity.HorarioEntity;
import com.clinica.model.entity.TerapeutaEntity;
import com.clinica.service.HorarioService;
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
@RequestMapping("/api/v1/horarios")
@Tag(name = "Horarios", description = "Gestión de horarios de terapeutas")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los horarios", description = "Obtiene la lista completa de horarios")
    @ApiResponse(responseCode = "200", description = "Lista de horarios obtenida exitosamente")
    public ResponseEntity<List<HorarioDTO>> getAllHorarios() {
        List<HorarioDTO> horarios = horarioService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener horario por ID", description = "Busca un horario específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horario encontrado"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrada")
    })
    public ResponseEntity<HorarioDTO> getHorarioById(
            @Parameter(description = "ID del horario", required = true) @PathVariable Long id) {
        return horarioService.findById(id)
                .map(horario -> ResponseEntity.ok(convertToDTO(horario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo horario", description = "Crea un nuevo horario para un terapeuta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Horario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<HorarioDTO> createHorario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del horario", required = true)
            @RequestBody HorarioDTO horarioDTO) {
        HorarioEntity horario = convertToEntity(horarioDTO);
        HorarioEntity savedHorario = horarioService.save(horario);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedHorario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar horario", description = "Actualiza los datos de un horario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horario actualizado"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<HorarioDTO> updateHorario(
            @Parameter(description = "ID del horario", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del horario", required = true)
            @RequestBody HorarioDTO horarioDTO) {
        HorarioEntity horario = convertToEntity(horarioDTO);
        HorarioEntity updatedHorario = horarioService.update(id, horario);
        return ResponseEntity.ok(convertToDTO(updatedHorario));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar horario", description = "Elimina un horario del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Horario eliminado"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    public ResponseEntity<Void> deleteHorario(
            @Parameter(description = "ID del horario", required = true) @PathVariable Long id) {
        horarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private HorarioDTO convertToDTO(HorarioEntity horario) {
        HorarioDTO dto = new HorarioDTO();
        dto.setIdHorario(horario.getIdHorario());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setActivo(horario.getActivo());
        
        if (horario.getTerapeuta() != null) {
            dto.setTerapeutaId(horario.getTerapeuta().getIdTerapeuta());
            dto.setNombreTerapeuta(horario.getTerapeuta().getNombre());
        }
        
        return dto;
    }

    private HorarioEntity convertToEntity(HorarioDTO dto) {
        HorarioEntity horario = new HorarioEntity();
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setActivo(dto.getActivo());
        
        if (dto.getTerapeutaId() != null) {
            TerapeutaEntity terapeuta = new TerapeutaEntity();
            terapeuta.setIdTerapeuta(dto.getTerapeutaId());
            horario.setTerapeuta(terapeuta);
        }
        
        return horario;
    }
}