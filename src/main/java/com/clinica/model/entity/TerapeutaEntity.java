package com.clinica.model.entity;

import com.clinica.model.enums.DisponibilidadTerapeuta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "terapeutas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TerapeutaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_terapeuta")
    private Long idTerapeuta;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "especialidad")
    private String especialidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "disponibilidad_terapeuta")
    private DisponibilidadTerapeuta disponibilidadTerapeuta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_disciplina")
    private DisciplinaEntity disciplina;

    @OneToMany(mappedBy = "terapeuta")
    private List<CitasEntity> citas;

    @Column(name = "codigo_licencia", unique = true, length = 50)
    private String codigoLicencia;

    @Column(name = "años_experiencia")
    private Integer aniosExperiencia;

    @Column(name = "biografia", columnDefinition = "TEXT")
    private String biografia;
}