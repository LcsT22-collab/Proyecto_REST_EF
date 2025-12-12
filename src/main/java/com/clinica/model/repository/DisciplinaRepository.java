
package com.clinica.model.repository;

import com.clinica.model.entity.DisciplinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaRepository extends JpaRepository<DisciplinaEntity, Long> {
    
}
