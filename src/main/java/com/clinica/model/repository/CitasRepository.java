package com.clinica.model.repository;

import com.clinica.model.entity.CitasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitasRepository extends JpaRepository<CitasEntity, Long> {
}