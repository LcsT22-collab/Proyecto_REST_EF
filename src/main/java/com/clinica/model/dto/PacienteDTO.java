package com.clinica.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de Transferencia de Datos para Pacientes")
public class PacienteDTO {

    @Schema(description = "ID único del paciente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idPaciente;
    
    @Schema(description = "Nombre completo del paciente", example = "Juan Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de nacimiento del paciente", example = "1990-05-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaNacimiento;
    
    @Schema(description = "Nombre del tutor (para pacientes menores)", example = "María López")
    private String nombreTutor;
    
    @Schema(description = "Número de teléfono de contacto", example = "+1234567890")
    private String telefono;
    
    @Schema(description = "Correo electrónico del paciente", example = "juan.perez@email.com")
    private String correo;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de registro en el sistema", example = "2024-01-15", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaRegistro;
}