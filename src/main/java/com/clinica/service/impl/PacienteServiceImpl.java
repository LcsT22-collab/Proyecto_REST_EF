package com.clinica.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.repository.PacientesRepository;
import com.clinica.service.PacienteService;

import jakarta.transaction.Transactional;

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
    public Optional<PacientesEntity> findById(Long id) {
        return pacientesRepository.findById(id);
    }

    @Override
    public PacientesEntity save(PacientesEntity paciente) {
        if (paciente.getNombre() == null || paciente.getNombre().trim().length() < 2) {
            throw new IllegalArgumentException("Nombre de paciente inválido (mínimo 2 caracteres).");
        }
        if (paciente.getFechaRegistro() == null) {
            paciente.setFechaRegistro(LocalDate.now());
        }
        return pacientesRepository.save(paciente);
    }

    @Override
    public PacientesEntity update(Long id, PacientesEntity paciente) {
        PacientesEntity existente = pacientesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado: " + id));

        if (paciente.getNombre() != null) existente.setNombre(paciente.getNombre());
        if (paciente.getFechaNacimiento() != null) existente.setFechaNacimiento(paciente.getFechaNacimiento());
        if (paciente.getNombreTutor() != null) existente.setNombreTutor(paciente.getNombreTutor());
        if (paciente.getTelefono() != null) existente.setTelefono(paciente.getTelefono());
        if (paciente.getCorreo() != null) existente.setCorreo(paciente.getCorreo());
        return pacientesRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        pacientesRepository.deleteById(id);
    }
}
