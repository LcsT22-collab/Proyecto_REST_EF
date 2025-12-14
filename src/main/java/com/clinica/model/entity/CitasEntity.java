package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CitasEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long idCita;
    
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private PacientesEntity paciente;
    
    @ManyToOne
    @JoinColumn(name = "id_terapeuta", nullable = false)
    private TerapeutaEntity terapeuta;
    
    @Column(name = "fecha_cita", nullable = false)
    private LocalDateTime fechaCita;
    
    @Column(name = "duracion_minutos")
    private Integer duracionMinutos = 60;
    
    @Column(name = "estado_cita", nullable = false)
    private String estadoCita = "PROGRAMADA";
    
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}