package com.clinica.service.impl;

import com.clinica.model.entity.EvaluacionesEntity;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.repository.EvaluacionesRepository;
import com.clinica.model.repository.PacientesRepository;
import com.clinica.service.EvaluacionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionesRepository evaluacionesRepository;
    private final PacientesRepository pacientesRepository;

    public EvaluacionServiceImpl(EvaluacionesRepository evaluacionesRepository,
                                PacientesRepository pacientesRepository) {
        this.evaluacionesRepository = evaluacionesRepository;
        this.pacientesRepository = pacientesRepository;
    }

    @Override
    public List<EvaluacionesEntity> findAll() {
        return evaluacionesRepository.findAll();
    }

    @Override
    public Optional<EvaluacionesEntity> findById(Long id) {        if (id == null) {
            return Optional.empty();
        }        return evaluacionesRepository.findById(id);
    }

    @Override
    public EvaluacionesEntity save(EvaluacionesEntity evaluacion) {
        if (evaluacion.getPaciente() == null || evaluacion.getPaciente().getIdPaciente() == null) {
            throw new IllegalArgumentException("El paciente es requerido");
        }
        
        PacientesEntity paciente = pacientesRepository.findById(evaluacion.getPaciente().getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + evaluacion.getPaciente().getIdPaciente()));
        evaluacion.setPaciente(paciente);
        
        if (evaluacion.getTipoEvaluacion() == null || evaluacion.getTipoEvaluacion().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de evaluación es requerido");
        }
        
        if (evaluacion.getFechaEvaluacion() == null) {
            throw new IllegalArgumentException("La fecha de evaluación es requerida");
        }
        
        return evaluacionesRepository.save(evaluacion);
    }

    @Override
    public EvaluacionesEntity update(Long id, EvaluacionesEntity evaluacion) {
        EvaluacionesEntity existente = evaluacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada con ID: " + id));
        
        if (evaluacion.getTipoEvaluacion() != null) {
            existente.setTipoEvaluacion(evaluacion.getTipoEvaluacion());
        }
        
        if (evaluacion.getFechaEvaluacion() != null) {
            existente.setFechaEvaluacion(evaluacion.getFechaEvaluacion());
        }
        
        if (evaluacion.getResultado() != null) {
            existente.setResultado(evaluacion.getResultado());
        }
        
        if (evaluacion.getRecomendaciones() != null) {
            existente.setRecomendaciones(evaluacion.getRecomendaciones());
        }
        
        return evaluacionesRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        EvaluacionesEntity evaluacion = evaluacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada con ID: " + id));
        
        evaluacionesRepository.delete(evaluacion);
    }
}