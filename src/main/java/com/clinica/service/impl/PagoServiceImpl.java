package com.clinica.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.clinica.model.entity.PagosEntity;
import com.clinica.model.enums.EstadoPago;
import com.clinica.model.repository.PacientesRepository;
import com.clinica.model.repository.PagosRepository;
import com.clinica.service.PagoService;

import jakarta.transaction.Transactional;

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
    public Optional<PagosEntity> findById(Long id) {
        return pagosRepository.findById(id);
    }

    @Override
    public PagosEntity save(PagosEntity pago) {
        if (pago.getMonto() == null || pago.getMonto().signum() < 0) {
            throw new IllegalArgumentException("Monto inválido");
        }
        if (pago.getPaciente() == null || pago.getPaciente().getIdPaciente() == null) {
            throw new IllegalArgumentException("Paciente asociado requerido");
        }
        
        // Verificar que el paciente existe
        pacientesRepository.findById(pago.getPaciente().getIdPaciente())
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado: " + pago.getPaciente().getIdPaciente()));
        
        // Si no se proporcionó estado, asignar PENDIENTE por defecto
        if (pago.getEstadoPago() == null) {
            pago.setEstadoPago(EstadoPago.PENDIENTE);
        }

        return pagosRepository.save(pago);
    }

    @Override
    public PagosEntity update(Long id, PagosEntity pago) {
        PagosEntity existente = pagosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado: " + id));
        
        if (pago.getMonto() != null) {
            existente.setMonto(pago.getMonto());
        }
        if (pago.getFechaPago() != null) {
            existente.setFechaPago(pago.getFechaPago());
        }
        if (pago.getEstadoPago() != null) {
            existente.setEstadoPago(pago.getEstadoPago());
        }
        
        return pagosRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        pagosRepository.deleteById(id);
    }
}