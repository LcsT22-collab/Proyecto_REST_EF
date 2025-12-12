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

import com.clinica.model.dto.HorarioDTO;
import com.clinica.model.entity.HorarioEntity;
import com.clinica.service.HorarioService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Horarios", description = "Operaciones CRUD para la gestión de horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/listar/horarios")
    @Operation(summary = "Listar todos los horarios", description = "Obtiene una lista completa de todos los horarios registrados")
    @ApiResponse(responseCode = "200", description = "Lista de horarios obtenida exitosamente")
    public List<HorarioDTO> listarHorarios() {
        List<HorarioEntity> horarios = horarioService.findAll();
        return horarios.stream()
                .map(this::convertirADto)
                .toList();
    }

    @GetMapping("/horario/{id}")
    @Operation(summary = "Obtener horario por ID", description = "Busca un horario específico por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    public ResponseEntity<HorarioDTO> obtenerHorario(
            @Parameter(description = "ID del horario", example = "1", required = true) @PathVariable Long id) {
        return horarioService.findById(id)
                .map(horario -> ResponseEntity.ok(convertirADto(horario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/horario")
    @Operation(summary = "Crear nuevo horario", description = "Registra un nuevo horario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Horario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del horario inválidos")
    })
    public ResponseEntity<HorarioDTO> crearHorario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del horario a crear", required = true) @RequestBody HorarioDTO horarioDTO) {
        HorarioEntity horario = convertirAEntidad(horarioDTO);
        HorarioEntity nuevoHorario = horarioService.save(horario);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADto(nuevoHorario));
    }

    @PutMapping("/editar/horario/{id}")
    @Operation(summary = "Actualizar horario", description = "Actualiza la información de un horario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horario actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<HorarioDTO> actualizarHorario(
            @Parameter(description = "ID del horario a actualizar", example = "1", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del horario", required = true) @RequestBody HorarioDTO horarioDTO) {
        HorarioEntity horario = convertirAEntidad(horarioDTO);
        HorarioEntity horarioActualizado = horarioService.update(id, horario);
        return ResponseEntity.ok(convertirADto(horarioActualizado));
    }

    @DeleteMapping("/eliminar/horario/{id}")
    @Operation(summary = "Eliminar horario", description = "Elimina un horario del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Horario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    public ResponseEntity<Void> eliminarHorario(
            @Parameter(description = "ID del horario a eliminar", example = "1", required = true) @PathVariable Long id) {
        horarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private HorarioDTO convertirADto(HorarioEntity horario) {
        HorarioDTO dto = new HorarioDTO();
        dto.setIdHorario(horario.getIdHorario());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setTipo(horario.getTipo());
        dto.setActivo(horario.getActivo());
        dto.setDescripcion(horario.getDescripcion());
        dto.setFechaCreacion(horario.getFechaCreacion());
        dto.setFechaActualizacion(horario.getFechaActualizacion());
        return dto;
    }

    private HorarioEntity convertirAEntidad(HorarioDTO dto) {
        HorarioEntity horario = new HorarioEntity();
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setTipo(dto.getTipo());
        horario.setActivo(dto.getActivo());
        horario.setDescripcion(dto.getDescripcion());
        return horario;
    }
}
