package com.clinica.controller;

import com.clinica.model.dto.PagoDTO;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.entity.PagosEntity;
import com.clinica.service.PagoService;
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
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "Gestión de pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los pagos", description = "Obtiene la lista completa de pagos")
    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida exitosamente")
    public ResponseEntity<List<PagoDTO>> getAllPagos() {
        List<PagoDTO> pagos = pagoService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID", description = "Busca un pago específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<PagoDTO> getPagoById(
            @Parameter(description = "ID del pago", required = true) @PathVariable Long id) {
        return pagoService.findById(id)
                .map(pago -> ResponseEntity.ok(convertToDTO(pago)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo pago", description = "Crea un nuevo registro de pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<PagoDTO> createPago(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del pago", required = true)
            @RequestBody PagoDTO pagoDTO) {
        PagosEntity pago = convertToEntity(pagoDTO);
        PagosEntity savedPago = pagoService.save(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedPago));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pago", description = "Actualiza los datos de un pago existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<PagoDTO> updatePago(
            @Parameter(description = "ID del pago", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del pago", required = true)
            @RequestBody PagoDTO pagoDTO) {
        PagosEntity pago = convertToEntity(pagoDTO);
        PagosEntity updatedPago = pagoService.update(id, pago);
        return ResponseEntity.ok(convertToDTO(updatedPago));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pago", description = "Elimina un pago del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> deletePago(
            @Parameter(description = "ID del pago", required = true) @PathVariable Long id) {
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PagoDTO convertToDTO(PagosEntity pago) {
        PagoDTO dto = new PagoDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setMonto(pago.getMonto());
        dto.setFechaPago(pago.getFechaPago());
        dto.setEstadoPago(pago.getEstadoPago());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setReferencia(pago.getReferencia());
        dto.setObservaciones(pago.getObservaciones());
        
        if (pago.getPaciente() != null) {
            dto.setPacienteId(pago.getPaciente().getIdPaciente());
            dto.setNombrePaciente(pago.getPaciente().getNombre());
        }
        
        return dto;
    }

    private PagosEntity convertToEntity(PagoDTO dto) {
        PagosEntity pago = new PagosEntity();
        pago.setMonto(dto.getMonto());
        pago.setFechaPago(dto.getFechaPago());
        pago.setEstadoPago(dto.getEstadoPago());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setReferencia(dto.getReferencia());
        pago.setObservaciones(dto.getObservaciones());
        
        if (dto.getPacienteId() != null) {
            PacientesEntity paciente = new PacientesEntity();
            paciente.setIdPaciente(dto.getPacienteId());
            pago.setPaciente(paciente);
        }
        
        return pago;
    }
}