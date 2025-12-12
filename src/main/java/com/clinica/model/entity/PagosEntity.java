package com.clinica.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.clinica.model.enums.EstadoPago;

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
@Table(name="pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PagosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_pago")
    private Long idPago;

    @ManyToOne
    @JoinColumn(name="id_paciente")
    private PacientesEntity paciente;


    @Column(name="monto")
    private BigDecimal monto;

    @Column(name="fecha_pago")
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(name="estado_pago")
    private EstadoPago estadoPago;

}