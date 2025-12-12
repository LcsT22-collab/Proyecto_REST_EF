package com.clinica.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados posibles de un pago")
public enum EstadoPago {
    @Schema(description = "Pago pendiente de realizar")
    PENDIENTE,
    
    @Schema(description = "Pago completado exitosamente")
    PAGADO,
    
    @Schema(description = "Pago realizado parcialmente")
    PARCIAL,
    
    @Schema(description = "Pago cancelado")
    CANCELADO,
    
    @Schema(description = "Pago reembolsado")
    REEMBOLSADO,
    
    @Schema(description = "Pago vencido")
    VENCIDO
}