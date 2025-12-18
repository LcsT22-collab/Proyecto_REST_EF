package com.clinica.service.bpm;

import com.clinica.esb.EsbService;
import com.clinica.model.dto.bpm.SolicitudCitaBPM;
import com.clinica.model.entity.PacientesEntity;
import com.clinica.model.entity.TerapeutaEntity;
import com.clinica.model.enums.EstadoProcesoBPM;
import com.clinica.model.repository.CitasRepository;
import com.clinica.model.repository.PacientesRepository;
import com.clinica.model.repository.TerapeutasRepository;
import com.clinica.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BpmServiceTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private CitaService citaService;

    @Mock
    private CitasRepository citasRepository;

    @Mock
    private PacientesRepository pacientesRepository;

    @Mock
    private TerapeutasRepository terapeutasRepository;

    @Mock
    private EsbService esbService;

    @InjectMocks
    private BpmService bpmService;

    private PacientesEntity paciente;
    private TerapeutaEntity terapeuta;

    @BeforeEach
    void setUp() {
        paciente = new PacientesEntity();
        paciente.setIdPaciente(1L);
        paciente.setNombre("Juan Pérez");
        paciente.setFechaNacimiento(LocalDate.of(2010, 5, 15));
        paciente.setActivo(true);

        terapeuta = new TerapeutaEntity();
        terapeuta.setIdTerapeuta(1L);
        terapeuta.setNombre("Dra. María López");
        terapeuta.setActivo(true);
    }

    @Test
    void testIniciarProcesoCita() {
        // Act
        SolicitudCitaBPM resultado = bpmService.iniciarProcesoCita(1L, 1L, "CONSULTA");

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getIdProceso());
        assertEquals(1L, resultado.getPacienteId());
        assertEquals(1L, resultado.getTerapeutaId());
        assertEquals("CONSULTA", resultado.getTipoCita());
        assertEquals(EstadoProcesoBPM.SOLICITADO, resultado.getEstado());
        assertNotNull(resultado.getFechaSolicitud());

        // Verify ESB was called
        verify(esbService, times(1)).routeMessage(eq("CITA_NUEVA"), any(SolicitudCitaBPM.class));
    }

    @Test
    void testValidarDatosPacienteNoExiste() {
        // Arrange
        SolicitudCitaBPM solicitud = new SolicitudCitaBPM();
        solicitud.setIdProceso("test-123");
        solicitud.setPacienteId(999L);
        solicitud.setTerapeutaId(1L);

        when(pacientesRepository.existsById(999L)).thenReturn(false);

        // Act
        bpmService.procesarSolicitudCita(solicitud);

        // Assert: servicio maneja el error vía ESB sin propagar excepción
        assertEquals(EstadoProcesoBPM.ERROR, solicitud.getEstado());
        verify(esbService, times(1)).handleAlternateFlow(eq(solicitud), any(Exception.class));
    }

    @Test
    void testValidarDatosTerapeutaNoExiste() {
        // Arrange
        SolicitudCitaBPM solicitud = new SolicitudCitaBPM();
        solicitud.setIdProceso("test-123");
        solicitud.setPacienteId(1L);
        solicitud.setTerapeutaId(999L);

        when(pacientesRepository.existsById(1L)).thenReturn(true);
        when(terapeutasRepository.existsById(999L)).thenReturn(false);

        // Act
        bpmService.procesarSolicitudCita(solicitud);

        // Assert: servicio maneja el error vía ESB sin propagar excepción
        assertEquals(EstadoProcesoBPM.ERROR, solicitud.getEstado());
        verify(esbService, times(1)).handleAlternateFlow(eq(solicitud), any(Exception.class));
    }

    @Test
    void testCompensarProceso() {
        // Arrange
        SolicitudCitaBPM solicitud = new SolicitudCitaBPM();
        solicitud.setIdProceso("test-123");
        solicitud.setEstado(EstadoProcesoBPM.ERROR);
        solicitud.setErrorMensaje("Error de prueba");

        // Act
        bpmService.compensarProceso(solicitud);

        // Assert
        assertEquals(EstadoProcesoBPM.COMPENSADO, solicitud.getEstado());
    }

    @Test
    void testIniciarProcesoCitaConDiferentesTipos() {
        // Test con diferentes tipos de cita
        String[] tiposCita = {"CONSULTA", "EVALUACION", "SEGUIMIENTO"};

        for (String tipo : tiposCita) {
            SolicitudCitaBPM resultado = bpmService.iniciarProcesoCita(1L, 1L, tipo);
            assertNotNull(resultado);
            assertEquals(tipo, resultado.getTipoCita());
        }

        verify(esbService, times(tiposCita.length)).routeMessage(eq("CITA_NUEVA"), any(SolicitudCitaBPM.class));
    }
}
