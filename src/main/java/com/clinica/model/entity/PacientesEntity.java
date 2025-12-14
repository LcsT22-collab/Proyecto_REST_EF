package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacientesEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long idPaciente;
    
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;
    
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    
    @Column(name = "nombre_tutor", length = 200)
    private String nombreTutor;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "correo", length = 100)
    private String correo;
    
    @Column(name = "direccion", length = 300)
    private String direccion;
    
    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }
    }
}