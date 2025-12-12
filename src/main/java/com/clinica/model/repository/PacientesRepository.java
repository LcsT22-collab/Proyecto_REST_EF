package com.clinica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinica.model.entity.PacientesEntity;

@Repository
public interface PacientesRepository extends JpaRepository<PacientesEntity, Long> {

}
