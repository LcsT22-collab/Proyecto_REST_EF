package com.clinica.service.impl;

import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.entity.PagosEntity;
import com.clinica.model.repository.PacientesRepository;
import com.clinica.model.repository.PagosRepository;
import com.clinica.service.PagoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PagoServiceImpl implements PagoService {

    private final PagosRepository pagosRepository;
    private final PacientesRepository pacientesRepository;

    public PagoServiceImpl(PagosRepository pagosRepository,
                          PacientesRepository pacientesRepository) {
        this.pagosRepository = pagosRepository;
        this.pacientesRepository = pacientesRepository;
    }

    @Override
    public List<PagosEntity> findAll() {
        return pagosRepository.findAll();
    }

    @Override
    public Optional<PagosEntity> findById(Long id) {        if (id == null) {
            return Optional.empty();
        }        return pagosRepository.findById(id);
    }

    @Override
    public PagosEntity save(PagosEntity pago) {
        // Validar paciente
        if (pago.getPaciente() == null || pago.getPaciente().getIdPaciente() == null) {
            throw new IllegalArgumentException("El paciente es requerido");
        }
        
        PacientesEntity paciente = pacientesRepository.findById(pago.getPaciente().getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + pago.getPaciente().getIdPaciente()));
        pago.setPaciente(paciente);
        
        // Validar monto
        if (pago.getMonto() == null || pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        
        // Validar fecha de pago
        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDate.now());
        }
        
        // Validar que la fecha de pago no sea en el futuro
        if (pago.getFechaPago().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de pago no puede ser en el futuro");
        }
        
        // Validar método de pago si se proporciona
        if (pago.getMetodoPago() != null && !pago.getMetodoPago().trim().isEmpty()) {
            String[] metodosValidos = {"EFECTIVO", "TARJETA", "TRANSFERENCIA", "CHEQUE"};
            boolean metodoValido = false;
            for (String metodo : metodosValidos) {
                if (pago.getMetodoPago().equalsIgnoreCase(metodo)) {
                    metodoValido = true;
                    break;
                }
            }
            if (!metodoValido) {
                throw new IllegalArgumentException("Método de pago no válido. Use: EFECTIVO, TARJETA, TRANSFERENCIA o CHEQUE");
            }
        }
        
        return pagosRepository.save(pago);
    }

    @Override
    public PagosEntity update(Long id, PagosEntity pago) {
        PagosEntity existente = pagosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        
        if (pago.getMonto() != null && pago.getMonto().compareTo(BigDecimal.ZERO) > 0) {
            existente.setMonto(pago.getMonto());
        }
        
        if (pago.getFechaPago() != null) {
            existente.setFechaPago(pago.getFechaPago());
        }
        
        if (pago.getEstadoPago() != null) {
            existente.setEstadoPago(pago.getEstadoPago());
        }
        
        if (pago.getMetodoPago() != null) {
            existente.setMetodoPago(pago.getMetodoPago());
        }
        
        if (pago.getReferencia() != null) {
            existente.setReferencia(pago.getReferencia());
        }
        
        if (pago.getObservaciones() != null) {
            existente.setObservaciones(pago.getObservaciones());
        }
        
        return pagosRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        PagosEntity pago = pagosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        
        pagosRepository.delete(pago);
    }
}