package com.clinica.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.clinica.model.entity.EvaluacionesEntity;
import com.clinica.model.repository.EvaluacionesRepository;
import com.clinica.service.EvaluacionService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionesRepository evaluacionesRepository;

    public EvaluacionServiceImpl(EvaluacionesRepository evaluacionesRepository) {
        this.evaluacionesRepository = evaluacionesRepository;
    }

    @Override
    public List<EvaluacionesEntity> findAll() {
        return evaluacionesRepository.findAll();
    }

    @Override
    public Optional<EvaluacionesEntity> findById(Long id) {
        return evaluacionesRepository.findById(id);
    }

    @Override
    public EvaluacionesEntity save(EvaluacionesEntity evaluaciones) {
        return evaluacionesRepository.save(evaluaciones);
    }

    @Override
    public EvaluacionesEntity update(Long id, EvaluacionesEntity evaluaciones) {
        EvaluacionesEntity existente = evaluacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada: " + id));
        if (evaluaciones.getResultado() != null) existente.setResultado(evaluaciones.getResultado());
        if (evaluaciones.getTipoEvaluacion() != null) existente.setTipoEvaluacion(evaluaciones.getTipoEvaluacion());
        if (evaluaciones.getFechaEvaluacion() != null) existente.setFechaEvaluacion(evaluaciones.getFechaEvaluacion());
        return evaluacionesRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        evaluacionesRepository.deleteById(id);
    }
}