package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "salas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SalaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala")
    private Long idSala;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "numero", unique = true, length = 10)
    private String numero;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "piso", length = 20)
    private String piso;

    @Column(name = "disponible", nullable = false)
    private Boolean disponible = true;

    @Column(name = "equipamiento", columnDefinition = "TEXT")
    private String equipamiento;

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
