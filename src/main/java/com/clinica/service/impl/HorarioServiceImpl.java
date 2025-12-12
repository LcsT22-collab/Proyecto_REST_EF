package com.clinica.service.impl;

import com.clinica.model.entity.HorarioEntity;
import com.clinica.model.repository.HorarioRepository;
import com.clinica.service.HorarioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HorarioServiceImpl implements HorarioService {

    private final HorarioRepository horarioRepository;

    public HorarioServiceImpl(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    @Override
    public List<HorarioEntity> findAll() {
        return horarioRepository.findAll();
    }

    @Override
    public Optional<HorarioEntity> findById(Long id) {
        return horarioRepository.findById(id);
    }

    @Override
    public HorarioEntity save(HorarioEntity horario) {
        if (horario.getDiaSemana() == null || horario.getDiaSemana().trim().isEmpty()) {
            throw new IllegalArgumentException("El día de la semana es requerido");
        }

        if (horario.getHoraInicio() == null) {
            throw new IllegalArgumentException("La hora de inicio es requerida");
        }

        if (horario.getHoraFin() == null) {
            throw new IllegalArgumentException("La hora de fin es requerida");
        }

        if (horario.getActivo() == null) {
            horario.setActivo(true);
        }

        if (horario.getFechaCreacion() == null) {
            horario.setFechaCreacion(LocalDateTime.now());
        }

        return horarioRepository.save(horario);
    }

    @Override
    public HorarioEntity update(Long id, HorarioEntity horario) {
        HorarioEntity existente = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));

        if (horario.getDiaSemana() != null && !horario.getDiaSemana().trim().isEmpty()) {
            existente.setDiaSemana(horario.getDiaSemana());
        }

        if (horario.getHoraInicio() != null) {
            existente.setHoraInicio(horario.getHoraInicio());
        }

        if (horario.getHoraFin() != null) {
            existente.setHoraFin(horario.getHoraFin());
        }

        if (horario.getTipo() != null) {
            existente.setTipo(horario.getTipo());
        }

        if (horario.getActivo() != null) {
            existente.setActivo(horario.getActivo());
        }

        if (horario.getDescripcion() != null) {
            existente.setDescripcion(horario.getDescripcion());
        }

        existente.setFechaActualizacion(LocalDateTime.now());

        return horarioRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!horarioRepository.existsById(id)) {
            throw new RuntimeException("Horario no encontrado con ID: " + id);
        }
        horarioRepository.deleteById(id);
    }
}
