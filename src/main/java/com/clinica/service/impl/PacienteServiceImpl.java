package com.clinica.service.impl;

import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.repository.PacientesRepository;
import com.clinica.service.PacienteService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PacienteServiceImpl implements PacienteService {

    private final PacientesRepository pacientesRepository;

    public PacienteServiceImpl(PacientesRepository pacientesRepository) {
        this.pacientesRepository = pacientesRepository;
    }

    @Override
    public List<PacientesEntity> findAll() {
        return pacientesRepository.findAll();
    }

    @Override
    public Optional<PacientesEntity> findById(Long id) {        if (id == null) {
            return Optional.empty();
        }        return pacientesRepository.findById(id);
    }

    @Override
    public PacientesEntity save(PacientesEntity paciente) {
        // Validar datos requeridos
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del paciente es requerido");
        }
        
        if (paciente.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es requerida");
        }
        
        // Validar que la fecha de nacimiento no sea en el futuro
        if (paciente.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro");
        }
        
        // Validar email si se proporciona
        if (paciente.getCorreo() != null && !paciente.getCorreo().trim().isEmpty()) {
            if (!paciente.getCorreo().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("El correo electrónico no tiene un formato válido");
            }
        }
        
        return pacientesRepository.save(paciente);
    }

    @Override
    public PacientesEntity update(Long id, PacientesEntity paciente) {
        PacientesEntity existente = pacientesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        
        if (paciente.getNombre() != null && !paciente.getNombre().trim().isEmpty()) {
            existente.setNombre(paciente.getNombre());
        }
        
        if (paciente.getFechaNacimiento() != null) {
            existente.setFechaNacimiento(paciente.getFechaNacimiento());
        }
        
        if (paciente.getNombreTutor() != null) {
            existente.setNombreTutor(paciente.getNombreTutor());
        }
        
        if (paciente.getTelefono() != null) {
            existente.setTelefono(paciente.getTelefono());
        }
        
        if (paciente.getCorreo() != null) {
            existente.setCorreo(paciente.getCorreo());
        }
        
        if (paciente.getDireccion() != null) {
            existente.setDireccion(paciente.getDireccion());
        }
        
        if (paciente.getActivo() != null) {
            existente.setActivo(paciente.getActivo());
        }
        
        return pacientesRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        PacientesEntity paciente = pacientesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        
        // En lugar de eliminar, marcar como inactivo
        paciente.setActivo(false);
        pacientesRepository.save(paciente);
    }
}