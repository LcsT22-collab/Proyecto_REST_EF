package com.clinica.service;

import com.clinica.model.entity.DisciplinaEntity;
import java.util.List;
import java.util.Optional;

public interface DisciplinaService {

    List<DisciplinaEntity> findAll();
    
    Optional<DisciplinaEntity> findById(Long id);
    
    DisciplinaEntity save(DisciplinaEntity disciplina);
    
    DisciplinaEntity update(Long id, DisciplinaEntity disciplina);
    
    void delete(Long id);
}