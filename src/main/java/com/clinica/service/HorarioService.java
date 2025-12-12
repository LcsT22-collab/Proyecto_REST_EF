package com.clinica.service;

import com.clinica.model.entity.HorarioEntity;
import java.util.List;
import java.util.Optional;

public interface HorarioService {

    List<HorarioEntity> findAll();

    Optional<HorarioEntity> findById(Long id);

    HorarioEntity save(HorarioEntity horario);

    HorarioEntity update(Long id, HorarioEntity horario);

    void delete(Long id);
}
