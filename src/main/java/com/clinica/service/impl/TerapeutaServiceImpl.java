package com.clinica.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.clinica.model.entity.TerapeutaEntity;
import com.clinica.model.enums.DisponibilidadTerapeuta;
import com.clinica.model.repository.CitasRepository;
import com.clinica.model.repository.TerapeutasRepository;
import com.clinica.service.TerapeutaService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TerapeutaServiceImpl implements TerapeutaService {
	

    private final TerapeutasRepository terapeutasRepository;
    private final CitasRepository citasRepository;

    public TerapeutaServiceImpl(TerapeutasRepository terapeutasRepository, 
                                CitasRepository citasRepository) {
        this.terapeutasRepository = terapeutasRepository;
        this.citasRepository = citasRepository;
    }

    @Override
    public List<TerapeutaEntity> findAll() {
        return terapeutasRepository.findAll();
    }

    @Override
    public Optional<TerapeutaEntity> findById(Long id) {
        return terapeutasRepository.findById(id);
    }

    @Override
    public TerapeutaEntity save(TerapeutaEntity terapeuta) {
        if (terapeuta.getNombre() == null || terapeuta.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de terapeuta requerido");
        }
        if (terapeuta.getDisponibilidadTerapeuta() == null) {
            terapeuta.setDisponibilidadTerapeuta(DisponibilidadTerapeuta.NO_DISPONIBLE);
        }
        return terapeutasRepository.save(terapeuta);
    }

    @Override
    public TerapeutaEntity update(Long id, TerapeutaEntity terapeuta) {
        TerapeutaEntity existente = terapeutasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Terapeuta no encontrado: " + id));
        
        if (terapeuta.getNombre() != null) {
            existente.setNombre(terapeuta.getNombre());
        }
        if (terapeuta.getEspecialidad() != null) {
            existente.setEspecialidad(terapeuta.getEspecialidad());
        }
        if (terapeuta.getDisponibilidadTerapeuta() != null) {
            existente.setDisponibilidadTerapeuta(terapeuta.getDisponibilidadTerapeuta());
        }

        return terapeutasRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        // Verificar si el terapeuta tiene citas programadas
        boolean tieneCitas = citasRepository.findAll().stream()
                .anyMatch(cita -> cita.getTerapeuta() != null && 
                                 cita.getTerapeuta().getIdTerapeuta().equals(id) &&
                                 !cita.getEstadoCita().toString().equals("CANCELADA"));
        
        if (tieneCitas) {
            throw new RuntimeException("No se puede eliminar el terapeuta porque tiene citas programadas");
        }
        
        terapeutasRepository.deleteById(id);
    }
}
