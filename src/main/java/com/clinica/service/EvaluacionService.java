package com.clinica.service;

import java.util.List;
import java.util.Optional;

import com.clinica.model.entity.EvaluacionesEntity;

public interface EvaluacionService {

    List<EvaluacionesEntity> findAll();

    Optional<EvaluacionesEntity> findById(Long id);

    EvaluacionesEntity save(EvaluacionesEntity evaluaciones);

    EvaluacionesEntity update(Long id, EvaluacionesEntity evaluaciones);

    void delete(Long id);

}
