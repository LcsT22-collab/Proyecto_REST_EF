package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "horarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HorarioEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Long idHorario;
    
    @ManyToOne
    @JoinColumn(name = "id_terapeuta")
    private TerapeutaEntity terapeuta;
    
    @Column(name = "dia_semana", nullable = false, length = 20)
    private String diaSemana;
    
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;
    
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}