package com.clinica.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la gestión de pacientes")
public class PacienteDTO {
    
    @Schema(description = "ID del paciente", example = "1")
    private Long idPaciente;
    
@Schema(description = "Nombre completo", example = "Juan Pérez")
    private String nombre;

    @Schema(description = "Fecha de nacimiento", example = "1990-05-15")
    private LocalDate fechaNacimiento;
    
    @Schema(description = "Nombre del tutor", example = "María Pérez")
    private String nombreTutor;
    
    @Schema(description = "Teléfono", example = "+1234567890")
    private String telefono;
    
    @Schema(description = "Correo electrónico", example = "juan@email.com")
    private String correo;
    
    @Schema(description = "Dirección", example = "Calle Principal 123")
    private String direccion;
    
    @Schema(description = "Fecha de registro")
    private LocalDate fechaRegistro;
    
    @Schema(description = "Activo", example = "true")
    private Boolean activo;
}