package com.clinica.model.entity;


import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PacientesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_paciente")
    private Long idPaciente;

    @Column(name="nombre")
    private String nombre;

    @Column(name="fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name="nombre_tutor")
    private String nombreTutor;

    @Column(name="telefono")
    private String telefono;

    @Column(name="correo")
    private String correo;

    @Column(name="fecha_registro")
    private LocalDate fechaRegistro;

    //Relacion con la tabla CitasEntity
    @OneToMany(mappedBy = "paciente")
    private List<CitasEntity> citas;

    //Relacion con la tabla EvaluacionesEntity
    @OneToMany(mappedBy = "paciente")
    private List<EvaluacionesEntity> evaluaciones;

    //Relacion con la tabla PagosEntity
    @OneToMany(mappedBy = "paciente")
    private List<PagosEntity> pagos;

}