package com.clinica.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.clinica.model.entity.CitasEntity;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.entity.TerapeutaEntity;
import com.clinica.model.enums.DisponibilidadTerapeuta;
import com.clinica.model.enums.EstadoCita;
import com.clinica.model.repository.CitasRepository;
import com.clinica.model.repository.PacientesRepository;
import com.clinica.model.repository.TerapeutasRepository;
import com.clinica.service.CitaService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CitaServiceImpl implements CitaService {

    private final CitasRepository citasRepository;
    private final TerapeutasRepository terapeutasRepository;
    private final PacientesRepository pacientesRepository;

    public CitaServiceImpl(CitasRepository citasRepository,
            TerapeutasRepository terapeutasRepository,
            PacientesRepository pacientesRepository) {
        this.citasRepository = citasRepository;
        this.terapeutasRepository = terapeutasRepository;
        this.pacientesRepository = pacientesRepository;
    }

    @Override
    public List<CitasEntity> findAll() {
        return citasRepository.findAll();
    }

    @Override
    public Optional<CitasEntity> findById(Long id) {
        return citasRepository.findById(id);
    }

    @Override
    public CitasEntity save(CitasEntity cita) {
        if (cita.getPaciente() == null || cita.getPaciente().getIdPaciente() == null) {
            throw new IllegalArgumentException("Paciente requerido");
        }
        if (cita.getTerapeuta() == null || cita.getTerapeuta().getIdTerapeuta() == null) {
            throw new IllegalArgumentException("Terapeuta requerido");
        }
        if (cita.getFechaCita() == null || cita.getFechaCita().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Fecha de cita inválida o en el pasado");
        }

        PacientesEntity paciente = pacientesRepository.findById(cita.getPaciente().getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no existe: " + cita.getPaciente().getIdPaciente()));

        TerapeutaEntity terapeuta = terapeutasRepository.findById(cita.getTerapeuta().getIdTerapeuta())
                .orElseThrow(
                        () -> new RuntimeException("Terapeuta no existe: " + cita.getTerapeuta().getIdTerapeuta()));

        if (terapeuta.getDisponibilidadTerapeuta() != null
                && terapeuta.getDisponibilidadTerapeuta() != DisponibilidadTerapeuta.DISPONIBLE) {
            throw new RuntimeException("Terapeuta no disponible");
        }

        boolean conflicto = citasRepository.findAll().stream()
                .filter(c -> c.getTerapeuta() != null
                        && c.getTerapeuta().getIdTerapeuta().equals(terapeuta.getIdTerapeuta()))
                .anyMatch(c -> c.getFechaCita().equals(cita.getFechaCita())
                        && (c.getEstadoCita() == EstadoCita.CONFIRMADA || c.getEstadoCita() == EstadoCita.PROGRAMADA));

        if (conflicto) {
            throw new RuntimeException("Conflicto de horario para el terapeuta en esa fecha/hora");
        }

        cita.setEstadoCita(EstadoCita.PROGRAMADA);
        CitasEntity saved = citasRepository.save(cita);

        terapeuta.setDisponibilidadTerapeuta(DisponibilidadTerapeuta.OCUPADO);
        terapeutasRepository.save(terapeuta);

        return saved;
    }

    @Override
    public CitasEntity update(Long id, CitasEntity cita) {
        CitasEntity existente = citasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + id));

        if (cita.getFechaCita() != null) {
            existente.setFechaCita(cita.getFechaCita());
        }
        if (cita.getEstadoCita() != null) {
            existente.setEstadoCita(cita.getEstadoCita());
        }
        return citasRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        CitasEntity existente = citasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + id));
        existente.setEstadoCita(EstadoCita.CANCELADA);
        citasRepository.save(existente);
    }
}