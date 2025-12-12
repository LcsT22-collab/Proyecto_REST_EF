package com.clinica.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de Transferencia de Datos para Pagos")
public class PagoDTO {

    @Schema(description = "ID único del pago", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idPago;
    
    @Schema(description = "ID del paciente asociado al pago", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idPaciente;
    
    @Schema(description = "Monto del pago", example = "150.75", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal monto;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha del pago", example = "2024-01-20")
    private LocalDate fechaPago;
    
    @Schema(description = "Estado del pago", 
             example = "PAGADO", 
             allowableValues = {"PENDIENTE", "PAGADO", "PARCIAL", "CANCELADO", "REEMBOLSADO", "VENCIDO"})
    private String estadoPago;
    
    @Schema(description = "Nombre del paciente (solo lectura)", example = "Juan Pérez", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombrePaciente;
}