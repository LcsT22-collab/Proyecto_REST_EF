package com.clinica.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="evaluaciones")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EvaluacionesEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_evaluaciones")
    private Long idEvaluaciones;

    @ManyToOne
    @JoinColumn(name="id_paciente")
    private PacientesEntity paciente;

    @Column(name="fecha_evaluacion")
    private LocalDate fechaEvaluacion;

    @Column(name="tipo_evaluacion")
    private String tipoEvaluacion;

    @Lob
    @Column(name="resultado", columnDefinition = "TEXT")
    private String resultado;
    
}
