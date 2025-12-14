package com.clinica.service;

import com.clinica.model.entity.CitasEntity;
import java.util.List;
import java.util.Optional;

public interface CitaService {
    List<CitasEntity> findAll();
    Optional<CitasEntity> findById(Long id);
    CitasEntity save(CitasEntity cita);
    CitasEntity update(Long id, CitasEntity cita);
    void delete(Long id);
}