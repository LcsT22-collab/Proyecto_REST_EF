package com.clinica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinica.model.entity.CitasEntity;

@Repository
public interface CitasRepository extends JpaRepository<CitasEntity, Long> {

}
