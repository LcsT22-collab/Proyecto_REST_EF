package com.clinica.service.impl;

import com.clinica.model.entity.DisciplinaEntity;
import com.clinica.model.enums.EstadoDisciplina;
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

        if (disciplina.getCodigo() == null || disciplina.getCodigo().trim().isEmpty()) {
            String codigoBase = disciplina.getNombre().length() >= 3
                    ? disciplina.getNombre().substring(0, 3).toUpperCase()
                    : disciplina.getNombre().toUpperCase();

            String codigo = codigoBase;
            int contador = 1;

            while (true) {
                boolean existe = false;
                List<DisciplinaEntity> todas = disciplinaRepository.findAll();
                for (DisciplinaEntity d : todas) {
                    if (d.getCodigo() != null && d.getCodigo().equals(codigo)) {
                        existe = true;
                        break;
                    }
                }
                if (!existe)
                    break;
                codigo = codigoBase + contador;
                contador++;
            }
            disciplina.setCodigo(codigo);
        }

        if (disciplina.getEstado() == null) {
            disciplina.setEstado(EstadoDisciplina.ACTIVA);
        }

        if (disciplina.getFechaCreacion() == null) {
            disciplina.setFechaCreacion(LocalDateTime.now());
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

        if (disciplina.getCodigo() != null && !disciplina.getCodigo().trim().isEmpty()) {
            existente.setCodigo(disciplina.getCodigo());
        }

        existente.setFechaActualizacion(LocalDateTime.now());

        return disciplinaRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!disciplinaRepository.existsById(id)) {
            throw new RuntimeException("Disciplina no encontrada con ID: " + id);
        }

        DisciplinaEntity disciplina = disciplinaRepository.findById(id).orElse(null);
        if (disciplina != null && disciplina.getTerapeutas() != null && !disciplina.getTerapeutas().isEmpty()) {
            throw new RuntimeException(
                    "No se puede eliminar la disciplina porque tiene terapeutas asociados. " +
                            "Primero reasigne o elimine los terapeutas.");
        }

        disciplinaRepository.deleteById(id);
    }
}