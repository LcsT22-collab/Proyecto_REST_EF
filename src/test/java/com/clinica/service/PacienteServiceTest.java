package com.clinica.service;

import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.repository.PacientesRepository;
import com.clinica.service.impl.PacienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacientesRepository pacientesRepository;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    private PacientesEntity paciente;

    @BeforeEach
    void setUp() {
        paciente = new PacientesEntity();
        paciente.setIdPaciente(1L);
        paciente.setNombre("Juan Pérez");
        paciente.setFechaNacimiento(LocalDate.of(2010, 5, 15));
        paciente.setNombreTutor("María Pérez");
        paciente.setTelefono("555-1234");
        paciente.setCorreo("maria.perez@email.com");
        paciente.setActivo(true);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<PacientesEntity> pacientes = Arrays.asList(paciente);
        when(pacientesRepository.findAll()).thenReturn(pacientes);

        // Act
        List<PacientesEntity> result = pacienteService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).getNombre());
        verify(pacientesRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        when(pacientesRepository.findById(1L)).thenReturn(Optional.of(paciente));

        // Act
        Optional<PacientesEntity> result = pacienteService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Juan Pérez", result.get().getNombre());
        verify(pacientesRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveValidPaciente() {
        // Arrange
        when(pacientesRepository.save(any(PacientesEntity.class))).thenReturn(paciente);

        // Act
        PacientesEntity result = pacienteService.save(paciente);

        // Assert
        assertNotNull(result);
        assertEquals("Juan Pérez", result.getNombre());
        verify(pacientesRepository, times(1)).save(paciente);
    }

    @Test
    void testSaveInvalidNombre() {
        // Arrange
        paciente.setNombre("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            pacienteService.save(paciente);
        });
    }

    @Test
    void testSaveInvalidFechaNacimiento() {
        // Arrange
        paciente.setFechaNacimiento(LocalDate.now().plusDays(1));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            pacienteService.save(paciente);
        });
    }

    @Test
    void testSaveInvalidEmail() {
        // Arrange
        paciente.setCorreo("email-invalido");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            pacienteService.save(paciente);
        });
    }

    @Test
    void testUpdate() {
        // Arrange
        PacientesEntity pacienteActualizado = new PacientesEntity();
        pacienteActualizado.setNombre("Juan Pérez Actualizado");
        pacienteActualizado.setTelefono("555-9999");

        when(pacientesRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacientesRepository.save(any(PacientesEntity.class))).thenReturn(paciente);

        // Act
        PacientesEntity result = pacienteService.update(1L, pacienteActualizado);

        // Assert
        assertNotNull(result);
        verify(pacientesRepository, times(1)).findById(1L);
        verify(pacientesRepository, times(1)).save(any(PacientesEntity.class));
    }

    @Test
    void testUpdatePacienteNoEncontrado() {
        // Arrange
        when(pacientesRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            pacienteService.update(999L, paciente);
        });
    }

    @Test
    void testDelete() {
        // Arrange
        when(pacientesRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacientesRepository.save(any(PacientesEntity.class))).thenReturn(paciente);

        // Act
        pacienteService.delete(1L);

        // Assert
        verify(pacientesRepository, times(1)).findById(1L);
        verify(pacientesRepository, times(1)).save(any(PacientesEntity.class));
    }
}
