package com.clinica.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la gestión de pagos")
public class PagoDTO {
    
    @Schema(description = "ID del pago", example = "1")
    private Long idPago;
    
    @Schema(description = "ID del paciente", example = "1")
    private Long pacienteId;
    
    @Schema(description = "Nombre del paciente", example = "Juan Pérez")
    private String nombrePaciente;
    
    @Schema(description = "ID de la cita", example = "1")
    private Long citaId;
    
    @Schema(description = "Monto del pago", example = "150.00")
    private BigDecimal monto;
    
    @Schema(description = "Fecha de pago", example = "2024-12-01")
    private LocalDate fechaPago;
    
    @Schema(description = "Fecha y hora de pago", example = "2024-12-01T10:00:00")
    private LocalDateTime fechaPagoTime;
    
    @Schema(description = "Estado del pago", example = "PAGADO")
    private String estadoPago;
    
    @Schema(description = "Estado del pago (enum)", example = "PENDIENTE")
    private String estado;
    
    @Schema(description = "Método de pago", example = "TARJETA")
    private String metodoPago;
    
    @Schema(description = "Referencia", example = "REF-123456")
    private String referencia;
    
    @Schema(description = "Observaciones", example = "Pago por consulta")
    private String observaciones;
}