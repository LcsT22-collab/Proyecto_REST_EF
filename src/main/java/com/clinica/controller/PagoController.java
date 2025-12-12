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

import com.clinica.model.dto.PagoDTO;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.entity.PagosEntity;
import com.clinica.model.enums.EstadoPago;
import com.clinica.service.PagoService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Pagos", description = "Operaciones CRUD para la gestión de pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping("/listar/pagos")
    @Operation(summary = "Listar todos los pagos", description = "Obtiene una lista completa de todos los pagos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida exitosamente")
    public List<PagoDTO> listarPagos() {
        List<PagosEntity> pagos = pagoService.findAll();
        return pagos.stream()
                .map(this::convertirADto)
                .toList();
    }

    @GetMapping("/pago/{id}")
    @Operation(summary = "Obtener pago por ID", description = "Busca un pago específico por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<PagoDTO> obtenerPago(
            @Parameter(description = "ID del pago", example = "1", required = true) @PathVariable Long id) {
        return pagoService.findById(id)
                .map(pago -> ResponseEntity.ok(convertirADto(pago)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/pago")
    @Operation(summary = "Crear nuevo pago", description = "Registra un nuevo pago en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del pago inválidos")
    })
    public ResponseEntity<PagoDTO> crearPago(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del pago a crear", required = true) @RequestBody PagoDTO pagoDTO) {
        PagosEntity pago = convertirAEntidad(pagoDTO);
        PagosEntity nuevoPago = pagoService.save(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADto(nuevoPago));
    }

    @PutMapping("/editar/pago/{id}")
    @Operation(summary = "Actualizar pago", description = "Actualiza la información de un pago existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<PagoDTO> actualizarPago(
            @Parameter(description = "ID del pago a actualizar", example = "1", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del pago", required = true) @RequestBody PagoDTO pagoDTO) {
        PagosEntity pago = convertirAEntidad(pagoDTO);
        PagosEntity pagoActualizado = pagoService.update(id, pago);
        return ResponseEntity.ok(convertirADto(pagoActualizado));
    }

    @DeleteMapping("/eliminar/pago/{id}")
    @Operation(summary = "Eliminar pago", description = "Elimina un pago del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> eliminarPago(
            @Parameter(description = "ID del pago a eliminar", example = "1", required = true) @PathVariable Long id) {
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PagoDTO convertirADto(PagosEntity pago) {
        PagoDTO dto = new PagoDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setMonto(pago.getMonto());
        dto.setFechaPago(pago.getFechaPago());
        dto.setEstadoPago(pago.getEstadoPago().toString());

        if (pago.getPaciente() != null) {
            dto.setIdPaciente(pago.getPaciente().getIdPaciente());
            dto.setNombrePaciente(pago.getPaciente().getNombre());
        }

        return dto;
    }

    private PagosEntity convertirAEntidad(PagoDTO dto) {
        PagosEntity pago = new PagosEntity();
        pago.setMonto(dto.getMonto());
        pago.setFechaPago(dto.getFechaPago());

        if (dto.getEstadoPago() != null) {
            try {
                pago.setEstadoPago(EstadoPago.valueOf(dto.getEstadoPago()));
            } catch (IllegalArgumentException e) {
                pago.setEstadoPago(EstadoPago.PENDIENTE);
            }
        } else {
            pago.setEstadoPago(EstadoPago.PENDIENTE);
        }

        if (dto.getIdPaciente() != null) {
            PacientesEntity paciente = new PacientesEntity();
            paciente.setIdPaciente(dto.getIdPaciente());
            pago.setPaciente(paciente);
        }

        return pago;
    }
}