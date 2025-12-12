package com.clinica.service;

import java.util.List;
import java.util.Optional;

import com.clinica.model.entity.TerapeutaEntity;

public interface TerapeutaService {

    List<TerapeutaEntity> findAll();

    Optional<TerapeutaEntity> findById(Long id);

    TerapeutaEntity save(TerapeutaEntity terapeuta);

    TerapeutaEntity update(Long id, TerapeutaEntity terapeuta);

    void delete(Long id);

}