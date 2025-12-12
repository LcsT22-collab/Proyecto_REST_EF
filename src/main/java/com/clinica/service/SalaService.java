package com.clinica.service;

import com.clinica.model.entity.SalaEntity;
import java.util.List;
import java.util.Optional;

public interface SalaService {

    List<SalaEntity> findAll();

    Optional<SalaEntity> findById(Long id);

    SalaEntity save(SalaEntity sala);

    SalaEntity update(Long id, SalaEntity sala);

    void delete(Long id);
}
