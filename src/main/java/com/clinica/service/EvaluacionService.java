package com.clinica.service;

import com.clinica.model.entity.EvaluacionesEntity;
import java.util.List;
import java.util.Optional;

public interface EvaluacionService {
    List<EvaluacionesEntity> findAll();
    Optional<EvaluacionesEntity> findById(Long id);
    EvaluacionesEntity save(EvaluacionesEntity evaluacion);
    EvaluacionesEntity update(Long id, EvaluacionesEntity evaluacion);
    void delete(Long id);
}