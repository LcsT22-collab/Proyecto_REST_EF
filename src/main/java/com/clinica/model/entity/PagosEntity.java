package com.clinica.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagosEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;
    
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private PacientesEntity paciente;
    
    @Column(name = "monto", nullable = false)
    private BigDecimal monto;
    
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;
    
    @Column(name = "estado_pago", nullable = false, length = 20)
    private String estadoPago = "PENDIENTE";
    
    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;
    
    @Column(name = "referencia", length = 100)
    private String referencia;
    
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDate.now();
        }
        if (fechaPago == null) {
            fechaPago = LocalDate.now();
        }
    }
}