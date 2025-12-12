package com.clinica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinica.model.entity.EvaluacionesEntity;

@Repository
public interface EvaluacionesRepository extends JpaRepository<EvaluacionesEntity, Long> {

}
