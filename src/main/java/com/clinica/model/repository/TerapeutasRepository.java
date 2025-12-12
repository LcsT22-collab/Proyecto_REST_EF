package com.clinica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinica.model.entity.TerapeutaEntity;

@Repository
public interface TerapeutasRepository extends JpaRepository<TerapeutaEntity, Long>{
}
