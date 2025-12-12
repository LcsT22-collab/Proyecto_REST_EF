package com.clinica.service;

import java.util.List;
import java.util.Optional;

import com.clinica.model.entity.PacientesEntity;

public interface PacienteService {

    List<PacientesEntity> findAll();

    Optional<PacientesEntity> findById(Long id);

    PacientesEntity save(PacientesEntity paciente);

    PacientesEntity update(Long id, PacientesEntity paciente);

    void delete(Long id);

}
