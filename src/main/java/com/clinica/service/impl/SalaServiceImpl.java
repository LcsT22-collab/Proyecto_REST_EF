package com.clinica.service.impl;

import com.clinica.model.entity.SalaEntity;
import com.clinica.model.repository.SalaRepository;
import com.clinica.service.SalaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SalaServiceImpl implements SalaService {

    private final SalaRepository salaRepository;

    public SalaServiceImpl(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @Override
    public List<SalaEntity> findAll() {
        return salaRepository.findAll();
    }

    @Override
    public Optional<SalaEntity> findById(Long id) {
        return salaRepository.findById(id);
    }

    @Override
    public SalaEntity save(SalaEntity sala) {
        if (sala.getNombre() == null || sala.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la sala es requerido");
        }

        if (sala.getDisponible() == null) {
            sala.setDisponible(true);
        }

        if (sala.getFechaCreacion() == null) {
            sala.setFechaCreacion(LocalDateTime.now());
        }

        return salaRepository.save(sala);
    }

    @Override
    public SalaEntity update(Long id, SalaEntity sala) {
        SalaEntity existente = salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada con ID: " + id));

        if (sala.getNombre() != null && !sala.getNombre().trim().isEmpty()) {
            existente.setNombre(sala.getNombre());
        }

        if (sala.getNumero() != null) {
            existente.setNumero(sala.getNumero());
        }

        if (sala.getCapacidad() != null) {
            existente.setCapacidad(sala.getCapacidad());
        }

        if (sala.getPiso() != null) {
            existente.setPiso(sala.getPiso());
        }

        if (sala.getDisponible() != null) {
            existente.setDisponible(sala.getDisponible());
        }

        if (sala.getEquipamiento() != null) {
            existente.setEquipamiento(sala.getEquipamiento());
        }

        existente.setFechaActualizacion(LocalDateTime.now());

        return salaRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!salaRepository.existsById(id)) {
            throw new RuntimeException("Sala no encontrada con ID: " + id);
        }
        salaRepository.deleteById(id);
    }
}
