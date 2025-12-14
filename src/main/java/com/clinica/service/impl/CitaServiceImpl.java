package com.clinica.service.impl;

import com.clinica.model.entity.CitasEntity;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.entity.TerapeutaEntity;
import com.clinica.model.repository.CitasRepository;
import com.clinica.model.repository.PacientesRepository;
import com.clinica.model.repository.TerapeutasRepository;
import com.clinica.service.CitaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CitaServiceImpl implements CitaService {

    private final CitasRepository citasRepository;
    private final PacientesRepository pacientesRepository;
    private final TerapeutasRepository terapeutasRepository;

    public CitaServiceImpl(CitasRepository citasRepository,
                          PacientesRepository pacientesRepository,
                          TerapeutasRepository terapeutasRepository) {
        this.citasRepository = citasRepository;
        this.pacientesRepository = pacientesRepository;
        this.terapeutasRepository = terapeutasRepository;
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
        // Validar que el paciente existe
        if (cita.getPaciente() == null || cita.getPaciente().getIdPaciente() == null) {
            throw new IllegalArgumentException("El paciente es requerido");
        }
        
        PacientesEntity paciente = pacientesRepository.findById(cita.getPaciente().getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + cita.getPaciente().getIdPaciente()));
        cita.setPaciente(paciente);
        
        // Validar que el terapeuta existe
        if (cita.getTerapeuta() == null || cita.getTerapeuta().getIdTerapeuta() == null) {
            throw new IllegalArgumentException("El terapeuta es requerido");
        }
        
        TerapeutaEntity terapeuta = terapeutasRepository.findById(cita.getTerapeuta().getIdTerapeuta())
                .orElseThrow(() -> new RuntimeException("Terapeuta no encontrado con ID: " + cita.getTerapeuta().getIdTerapeuta()));
        cita.setTerapeuta(terapeuta);
        
        // Validar fecha
        if (cita.getFechaCita() == null || cita.getFechaCita().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la cita no puede ser en el pasado");
        }
        
        // Validar conflicto de horarios para el terapeuta
        boolean existeConflicto = citasRepository.findAll().stream()
                .filter(c -> c.getTerapeuta().getIdTerapeuta().equals(terapeuta.getIdTerapeuta()))
                .filter(c -> !c.getEstadoCita().equals("CANCELADA"))
                .anyMatch(c -> {
                    LocalDateTime inicioExistente = c.getFechaCita();
                    LocalDateTime finExistente = inicioExistente.plusMinutes(c.getDuracionMinutos());
                    LocalDateTime inicioNueva = cita.getFechaCita();
                    LocalDateTime finNueva = inicioNueva.plusMinutes(cita.getDuracionMinutos());
                    
                    return inicioNueva.isBefore(finExistente) && finNueva.isAfter(inicioExistente);
                });
        
        if (existeConflicto) {
            throw new RuntimeException("El terapeuta ya tiene una cita programada en ese horario");
        }
        
        return citasRepository.save(cita);
    }

    @Override
    public CitasEntity update(Long id, CitasEntity cita) {
        CitasEntity existente = citasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
        
        if (cita.getFechaCita() != null) {
            existente.setFechaCita(cita.getFechaCita());
        }
        
        if (cita.getEstadoCita() != null) {
            existente.setEstadoCita(cita.getEstadoCita());
        }
        
        if (cita.getDuracionMinutos() != null) {
            existente.setDuracionMinutos(cita.getDuracionMinutos());
        }
        
        if (cita.getObservaciones() != null) {
            existente.setObservaciones(cita.getObservaciones());
        }
        
        existente.setFechaActualizacion(LocalDateTime.now());
        
        return citasRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        CitasEntity cita = citasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
        
        // Cambiar estado a CANCELADA en lugar de eliminar
        cita.setEstadoCita("CANCELADA");
        cita.setFechaActualizacion(LocalDateTime.now());
        citasRepository.save(cita);
    }
}