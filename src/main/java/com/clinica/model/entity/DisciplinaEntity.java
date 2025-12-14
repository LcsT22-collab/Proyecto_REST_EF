package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disciplinas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disciplina")
    private Long idDisciplina;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "estado", nullable = false)
    private String estado = "ACTIVA";
    
    @Column(name = "codigo", unique = true, length = 20)
    private String codigo;
    
    @Column(name = "color", length = 7)
    private String color;
    
    @OneToMany(mappedBy = "disciplina", fetch = FetchType.LAZY)
    private List<TerapeutaEntity> terapeutas = new ArrayList<>();
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (codigo == null || codigo.isEmpty()) {
            codigo = nombre.length() >= 3 
                ? nombre.substring(0, 3).toUpperCase() 
                : nombre.toUpperCase();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}