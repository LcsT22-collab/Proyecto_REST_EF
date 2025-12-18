package com.clinica.service.impl;

import com.clinica.model.entity.HorarioEntity;
import com.clinica.model.entity.TerapeutaEntity;
import com.clinica.model.repository.HorarioRepository;
import com.clinica.model.repository.TerapeutasRepository;
import com.clinica.service.HorarioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HorarioServiceImpl implements HorarioService {

    private final HorarioRepository horarioRepository;
    private final TerapeutasRepository terapeutasRepository;

    public HorarioServiceImpl(HorarioRepository horarioRepository,
                             TerapeutasRepository terapeutasRepository) {
        this.horarioRepository = horarioRepository;
        this.terapeutasRepository = terapeutasRepository;
    }

    @Override
    public List<HorarioEntity> findAll() {
        return horarioRepository.findAll();
    }

    @Override
    public Optional<HorarioEntity> findById(Long id) {        if (id == null) {
            return Optional.empty();
        }        return horarioRepository.findById(id);
    }

    @Override
    public HorarioEntity save(HorarioEntity horario) {
        // Validar terapeuta
        if (horario.getTerapeuta() != null && horario.getTerapeuta().getIdTerapeuta() != null) {
            TerapeutaEntity terapeuta = terapeutasRepository.findById(horario.getTerapeuta().getIdTerapeuta())
                    .orElseThrow(() -> new RuntimeException("Terapeuta no encontrado con ID: " + horario.getTerapeuta().getIdTerapeuta()));
            horario.setTerapeuta(terapeuta);
        }
        
        // Validar datos requeridos
        if (horario.getDiaSemana() == null || horario.getDiaSemana().trim().isEmpty()) {
            throw new IllegalArgumentException("El día de la semana es requerido");
        }
        
        if (horario.getHoraInicio() == null) {
            throw new IllegalArgumentException("La hora de inicio es requerida");
        }
        
        if (horario.getHoraFin() == null) {
            throw new IllegalArgumentException("La hora de fin es requerida");
        }
        
        // Validar que horaFin sea mayor que horaInicio
        if (horario.getHoraFin().isBefore(horario.getHoraInicio()) || 
            horario.getHoraFin().equals(horario.getHoraInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser mayor que la hora de inicio");
        }
        
        return horarioRepository.save(horario);
    }

    @Override
    public HorarioEntity update(Long id, HorarioEntity horario) {
        HorarioEntity existente = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));
        
        if (horario.getDiaSemana() != null) {
            existente.setDiaSemana(horario.getDiaSemana());
        }
        
        if (horario.getHoraInicio() != null) {
            existente.setHoraInicio(horario.getHoraInicio());
        }
        
        if (horario.getHoraFin() != null) {
            existente.setHoraFin(horario.getHoraFin());
        }
        
        if (horario.getActivo() != null) {
            existente.setActivo(horario.getActivo());
        }
        
        return horarioRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        HorarioEntity horario = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));
        
        horarioRepository.delete(horario);
    }
}