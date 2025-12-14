package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "evaluaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionesEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluaciones")
    private Long idEvaluaciones;
    
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private PacientesEntity paciente;
    
    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDate fechaEvaluacion;
    
    @Column(name = "tipo_evaluacion", nullable = false, length = 100)
    private String tipoEvaluacion;
    
    @Lob
    @Column(name = "resultado", columnDefinition = "TEXT")
    private String resultado;
    
    @Column(name = "recomendaciones", columnDefinition = "TEXT")
    private String recomendaciones;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDate.now();
        }
        if (fechaEvaluacion == null) {
            fechaEvaluacion = LocalDate.now();
        }
    }
}