
package com.clinica.model.repository;

import com.clinica.model.entity.HorarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends JpaRepository<HorarioEntity, Long> {

}
