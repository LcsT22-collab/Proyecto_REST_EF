package com.clinica.model.entity;


import java.time.LocalDateTime;

import com.clinica.model.enums.EstadoCita;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="citas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CitasEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_cita")
    private Long idCita;

    @ManyToOne
    @JoinColumn(name="id_paciente")
    private PacientesEntity paciente;


    @ManyToOne
    @JoinColumn(name="id_terapeuta")
    private TerapeutaEntity terapeuta;


    @Column(name="fecha_cita")
    private LocalDateTime fechaCita;


    @Enumerated(EnumType.STRING)
    @Column(name="estado_cita")
    private EstadoCita estadoCita;

}
