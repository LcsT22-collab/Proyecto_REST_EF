package com.clinica.service.impl;

import com.clinica.model.entity.DisciplinaEntity;
import com.clinica.model.repository.DisciplinaRepository;
import com.clinica.service.DisciplinaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DisciplinaServiceImpl implements DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaServiceImpl(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @Override
    public List<DisciplinaEntity> findAll() {
        return disciplinaRepository.findAll();
    }

    @Override
    public Optional<DisciplinaEntity> findById(Long id) {
        return disciplinaRepository.findById(id);
    }

    @Override
    public DisciplinaEntity save(DisciplinaEntity disciplina) {
        if (disciplina.getNombre() == null || disciplina.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la disciplina es requerido");
        }
        
        // Generar código automático si no se proporciona
        if (disciplina.getCodigo() == null || disciplina.getCodigo().trim().isEmpty()) {
            String codigoBase = disciplina.getNombre().length() >= 3 
                    ? disciplina.getNombre().substring(0, 3).toUpperCase() 
                    : disciplina.getNombre().toUpperCase();
            
            disciplina.setCodigo(codigoBase);
        }
        
        // Validar que el código sea único
        List<DisciplinaEntity> disciplinasExistentes = disciplinaRepository.findAll();
        boolean codigoExiste = disciplinasExistentes.stream()
                .anyMatch(d -> d.getCodigo() != null && d.getCodigo().equals(disciplina.getCodigo()));
        
        if (codigoExiste) {
            throw new IllegalArgumentException("El código de disciplina ya existe: " + disciplina.getCodigo());
        }
        
        return disciplinaRepository.save(disciplina);
    }

    @Override
    public DisciplinaEntity update(Long id, DisciplinaEntity disciplina) {
        DisciplinaEntity existente = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada con ID: " + id));
        
        if (disciplina.getNombre() != null && !disciplina.getNombre().trim().isEmpty()) {
            existente.setNombre(disciplina.getNombre());
        }
        
        if (disciplina.getDescripcion() != null) {
            existente.setDescripcion(disciplina.getDescripcion());
        }
        
        if (disciplina.getEstado() != null) {
            existente.setEstado(disciplina.getEstado());
        }
        
        if (disciplina.getColor() != null) {
            existente.setColor(disciplina.getColor());
        }
        
        existente.setFechaActualizacion(LocalDateTime.now());
        
        return disciplinaRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        DisciplinaEntity disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada con ID: " + id));
        
        // Validar que no tenga terapeutas asociados
        if (disciplina.getTerapeutas() != null && !disciplina.getTerapeutas().isEmpty()) {
            throw new RuntimeException("No se puede eliminar la disciplina porque tiene terapeutas asociados");
        }
        
        disciplinaRepository.deleteById(id);
    }
}