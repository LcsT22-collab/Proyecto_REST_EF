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

import com.clinica.model.dto.TerapeutaDTO;
import com.clinica.model.entity.DisciplinaEntity;
import com.clinica.model.entity.TerapeutaEntity;
import com.clinica.model.enums.DisponibilidadTerapeuta;
import com.clinica.service.TerapeutaService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Terapeutas", description = "Operaciones CRUD para la gestión de terapeutas")
public class TerapeutaController {

    @Autowired
    private TerapeutaService terapeutaService;

    @GetMapping("/listar/terapeutas")
    @Operation(summary = "Listar todos los terapeutas", description = "Obtiene una lista completa de todos los terapeutas registrados")
    @ApiResponse(responseCode = "200", description = "Lista de terapeutas obtenida exitosamente")
    public List<TerapeutaDTO> listarTerapeutas() {
        List<TerapeutaEntity> terapeutas = terapeutaService.findAll();
        return terapeutas.stream()
                .map(this::convertirADto)
                .toList();
    }

    @GetMapping("/terapeuta/{id}")
    @Operation(summary = "Obtener terapeuta por ID", description = "Busca un terapeuta específico por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Terapeuta encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Terapeuta no encontrado")
    })
    public ResponseEntity<TerapeutaDTO> obtenerTerapeuta(
            @Parameter(description = "ID del terapeuta", example = "1", required = true) @PathVariable Long id) {
        return terapeutaService.findById(id)
                .map(terapeuta -> ResponseEntity.ok(convertirADto(terapeuta)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/terapeuta")
    @Operation(summary = "Crear nuevo terapeuta", description = "Registra un nuevo terapeuta en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Terapeuta creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del terapeuta inválidos")
    })
    public ResponseEntity<TerapeutaDTO> crearTerapeuta(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del terapeuta a crear", required = true) @RequestBody TerapeutaDTO terapeutaDTO) {
        TerapeutaEntity terapeuta = convertirAEntidad(terapeutaDTO);
        TerapeutaEntity nuevoTerapeuta = terapeutaService.save(terapeuta);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADto(nuevoTerapeuta));
    }

    @PutMapping("/editar/terapeuta/{id}")
    @Operation(summary = "Actualizar terapeuta", description = "Actualiza la información de un terapeuta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Terapeuta actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Terapeuta no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<TerapeutaDTO> actualizarTerapeuta(
            @Parameter(description = "ID del terapeuta a actualizar", example = "1", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del terapeuta", required = true) @RequestBody TerapeutaDTO terapeutaDTO) {
        TerapeutaEntity terapeuta = convertirAEntidad(terapeutaDTO);
        TerapeutaEntity terapeutaActualizado = terapeutaService.update(id, terapeuta);
        return ResponseEntity.ok(convertirADto(terapeutaActualizado));
    }

    @DeleteMapping("/eliminar/terapeuta/{id}")
    @Operation(summary = "Eliminar terapeuta", description = "Elimina un terapeuta del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Terapeuta eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Terapeuta no encontrado"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar porque tiene citas programadas")
    })
    public ResponseEntity<Void> eliminarTerapeuta(
            @Parameter(description = "ID del terapeuta a eliminar", example = "1", required = true) @PathVariable Long id) {
        terapeutaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TerapeutaDTO convertirADto(TerapeutaEntity terapeuta) {
        TerapeutaDTO dto = new TerapeutaDTO();
        dto.setIdTerapeuta(terapeuta.getIdTerapeuta());
        dto.setNombre(terapeuta.getNombre());
        dto.setEspecialidad(terapeuta.getEspecialidad());
        dto.setDisponibilidadTerapeuta(terapeuta.getDisponibilidadTerapeuta().toString());

        if (terapeuta.getDisciplina() != null) {
            dto.setIdDisciplina(terapeuta.getDisciplina().getIdDisciplina());
            dto.setNombreDisciplina(terapeuta.getDisciplina().getNombre());
        }

        return dto;
    }

    private TerapeutaEntity convertirAEntidad(TerapeutaDTO dto) {
        TerapeutaEntity terapeuta = new TerapeutaEntity();
        terapeuta.setNombre(dto.getNombre());
        terapeuta.setEspecialidad(dto.getEspecialidad());

        if (dto.getDisponibilidadTerapeuta() != null) {
            try {
                terapeuta.setDisponibilidadTerapeuta(
                        DisponibilidadTerapeuta.valueOf(dto.getDisponibilidadTerapeuta()));
            } catch (IllegalArgumentException e) {
                terapeuta.setDisponibilidadTerapeuta(DisponibilidadTerapeuta.NO_DISPONIBLE);
            }
        }

        if (dto.getIdDisciplina() != null) {
            DisciplinaEntity disciplina = new DisciplinaEntity();
            disciplina.setIdDisciplina(dto.getIdDisciplina());
            terapeuta.setDisciplina(disciplina);
        }

        return terapeuta;
    }
}