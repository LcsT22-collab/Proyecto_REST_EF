package com.clinica.service;

import com.clinica.model.entity.PacientesEntity;
import java.util.List;
import java.util.Optional;

public interface PacienteService {
    List<PacientesEntity> findAll();
    Optional<PacientesEntity> findById(Long id);
    PacientesEntity save(PacientesEntity paciente);
    PacientesEntity update(Long id, PacientesEntity paciente);
    void delete(Long id);
}