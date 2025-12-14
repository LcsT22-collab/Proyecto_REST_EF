package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "terapeutas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TerapeutaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_terapeuta")
    private Long idTerapeuta;
    
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;
    
    @Column(name = "especialidad", length = 100)
    private String especialidad;
    
    @Column(name = "disponibilidad", nullable = false, length = 20)
    private String disponibilidad = "DISPONIBLE";
    
    @ManyToOne
    @JoinColumn(name = "id_disciplina")
    private DisciplinaEntity disciplina;
    
    @Column(name = "codigo_licencia", unique = true, length = 50)
    private String codigoLicencia;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "correo", length = 100)
    private String correo;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}