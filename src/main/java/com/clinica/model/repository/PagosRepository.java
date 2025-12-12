package com.clinica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinica.model.entity.PagosEntity;

@Repository
public interface PagosRepository extends JpaRepository<PagosEntity, Long> {

}
