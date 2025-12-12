package com.clinica.service;

import java.util.List;
import java.util.Optional;

import com.clinica.model.entity.CitasEntity;

public interface CitaService {

    List<CitasEntity> findAll();

    Optional<CitasEntity> findById(Long id);

    CitasEntity save(CitasEntity citas);

    CitasEntity update(Long id, CitasEntity citas);

    void delete(Long id);

}
