package com.clinica.service;

import com.clinica.model.entity.PagosEntity;
import java.util.List;
import java.util.Optional;

public interface PagoService {
    List<PagosEntity> findAll();
    Optional<PagosEntity> findById(Long id);
    PagosEntity save(PagosEntity pago);
    PagosEntity update(Long id, PagosEntity pago);
    void delete(Long id);
}