package com.clinica.service.impl;

import com.clinica.model.entity.DisciplinaEntity;
import com.clinica.model.entity.TerapeutaEntity;
import com.clinica.model.repository.CitasRepository;
import com.clinica.model.repository.DisciplinaRepository;
import com.clinica.model.repository.TerapeutasRepository;
import com.clinica.service.TerapeutaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TerapeutaServiceImpl implements TerapeutaService {

    private final TerapeutasRepository terapeutasRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final CitasRepository citasRepository;

    public TerapeutaServiceImpl(TerapeutasRepository terapeutasRepository,
                               DisciplinaRepository disciplinaRepository,
                               CitasRepository citasRepository) {
        this.terapeutasRepository = terapeutasRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.citasRepository = citasRepository;
    }

    @Override
    public List<TerapeutaEntity> findAll() {
        return terapeutasRepository.findAll();
    }

    @Override
    public Optional<TerapeutaEntity> findById(Long id) {
        return terapeutasRepository.findById(id);
    }

    @Override
    public TerapeutaEntity save(TerapeutaEntity terapeuta) {
        // Validar datos requeridos
        if (terapeuta.getNombre() == null || terapeuta.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del terapeuta es requerido");
        }
        
        // Validar disciplina si se proporciona
        if (terapeuta.getDisciplina() != null && terapeuta.getDisciplina().getIdDisciplina() != null) {
            DisciplinaEntity disciplina = disciplinaRepository.findById(terapeuta.getDisciplina().getIdDisciplina())
                    .orElseThrow(() -> new RuntimeException("Disciplina no encontrada con ID: " + terapeuta.getDisciplina().getIdDisciplina()));
            terapeuta.setDisciplina(disciplina);
        }
        
        // Validar código de licencia si se proporciona
        if (terapeuta.getCodigoLicencia() != null && !terapeuta.getCodigoLicencia().trim().isEmpty()) {
            // Verificar que el código de licencia sea único
            List<TerapeutaEntity> terapeutasExistentes = terapeutasRepository.findAll();
            boolean codigoExiste = terapeutasExistentes.stream()
                    .anyMatch(t -> t.getCodigoLicencia() != null && 
                                  t.getCodigoLicencia().equalsIgnoreCase(terapeuta.getCodigoLicencia()));
            
            if (codigoExiste) {
                throw new IllegalArgumentException("El código de licencia ya está registrado: " + terapeuta.getCodigoLicencia());
            }
        }
        
        // Validar email si se proporciona
        if (terapeuta.getCorreo() != null && !terapeuta.getCorreo().trim().isEmpty()) {
            if (!terapeuta.getCorreo().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("El correo electrónico no tiene un formato válido");
            }
        }
        
        return terapeutasRepository.save(terapeuta);
    }

    @Override
    public TerapeutaEntity update(Long id, TerapeutaEntity terapeuta) {
        TerapeutaEntity existente = terapeutasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Terapeuta no encontrado con ID: " + id));
        
        if (terapeuta.getNombre() != null && !terapeuta.getNombre().trim().isEmpty()) {
            existente.setNombre(terapeuta.getNombre());
        }
        
        if (terapeuta.getEspecialidad() != null) {
            existente.setEspecialidad(terapeuta.getEspecialidad());
        }
        
        if (terapeuta.getDisponibilidad() != null) {
            existente.setDisponibilidad(terapeuta.getDisponibilidad());
        }
        
        if (terapeuta.getTelefono() != null) {
            existente.setTelefono(terapeuta.getTelefono());
        }
        
        if (terapeuta.getCorreo() != null) {
            existente.setCorreo(terapeuta.getCorreo());
        }
        
        if (terapeuta.getActivo() != null) {
            existente.setActivo(terapeuta.getActivo());
        }
        
        // Actualizar disciplina si se proporciona
        if (terapeuta.getDisciplina() != null && terapeuta.getDisciplina().getIdDisciplina() != null) {
            DisciplinaEntity disciplina = disciplinaRepository.findById(terapeuta.getDisciplina().getIdDisciplina())
                    .orElseThrow(() -> new RuntimeException("Disciplina no encontrada con ID: " + terapeuta.getDisciplina().getIdDisciplina()));
            existente.setDisciplina(disciplina);
        }
        
        return terapeutasRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        TerapeutaEntity terapeuta = terapeutasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Terapeuta no encontrado con ID: " + id));
        
        // Verificar si tiene citas futuras
        boolean tieneCitasFuturas = citasRepository.findAll().stream()
                .filter(c -> c.getTerapeuta().getIdTerapeuta().equals(id))
                .anyMatch(c -> !c.getEstadoCita().equals("CANCELADA") && 
                             !c.getEstadoCita().equals("COMPLETADA"));
        
        if (tieneCitasFuturas) {
            throw new RuntimeException("No se puede eliminar el terapeuta porque tiene citas programadas");
        }
        
        // En lugar de eliminar, marcar como inactivo
        terapeuta.setActivo(false);
        terapeuta.setDisponibilidad("NO_DISPONIBLE");
        terapeutasRepository.save(terapeuta);
    }
}