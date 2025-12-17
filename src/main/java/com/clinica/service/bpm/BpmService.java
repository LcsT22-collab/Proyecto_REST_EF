package com.clinica.service.bpm;

import com.clinica.esb.EsbService;
import com.clinica.model.dto.bpm.SolicitudCitaBPM;
import com.clinica.model.dto.bpm.EventoProcesoBPM;
import com.clinica.model.entity.CitasEntity;
import com.clinica.model.enums.EstadoProcesoBPM;
import com.clinica.model.repository.CitasRepository;
import com.clinica.model.repository.PacientesRepository;
import com.clinica.model.repository.TerapeutasRepository;
import com.clinica.service.CitaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class BpmService {

    private final JmsTemplate jmsTemplate;
    private final CitaService citaService;
    private final CitasRepository citasRepository;
    private final PacientesRepository pacientesRepository;
    private final TerapeutasRepository terapeutasRepository;
    private final EsbService esbService;

    public BpmService(JmsTemplate jmsTemplate, 
                     CitaService citaService,
                     CitasRepository citasRepository,
                     PacientesRepository pacientesRepository,
                     TerapeutasRepository terapeutasRepository,
                     EsbService esbService) {
        this.jmsTemplate = jmsTemplate;
        this.citaService = citaService;
        this.citasRepository = citasRepository;
        this.pacientesRepository = pacientesRepository;
        this.terapeutasRepository = terapeutasRepository;
        this.esbService = esbService;
    }

    public SolicitudCitaBPM iniciarProcesoCita(Long pacienteId, Long terapeutaId, String tipoCita) {
        String idProceso = UUID.randomUUID().toString();
        
        SolicitudCitaBPM solicitud = new SolicitudCitaBPM();
        solicitud.setIdProceso(idProceso);
        solicitud.setPacienteId(pacienteId);
        solicitud.setTerapeutaId(terapeutaId);
        solicitud.setTipoCita(tipoCita);
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setEstado(EstadoProcesoBPM.SOLICITADO);
        solicitud.setIdTransaccion(UUID.randomUUID().toString());

        // Usar ESB para enrutar el mensaje
        esbService.routeMessage("CITA_NUEVA", solicitud);
        
        registrarEvento(idProceso, "INICIO", "Proceso BPM iniciado para crear cita", solicitud);
        
        log.info("Proceso BPM iniciado: {}", idProceso);
        return solicitud;
    }

    @Transactional
    public void procesarSolicitudCita(SolicitudCitaBPM solicitud) {
        try {
            log.info("Procesando solicitud BPM: {}", solicitud.getIdProceso());
            
            solicitud.setEstado(EstadoProcesoBPM.VALIDANDO);
            registrarEvento(solicitud.getIdProceso(), "VALIDACION", "Validando datos del paciente y terapeuta", null);
            
            boolean datosValidos = validarDatos(solicitud);
            if (!datosValidos) {
                throw new RuntimeException("Datos inválidos en la solicitud");
            }
            
            solicitud.setEstado(EstadoProcesoBPM.VALIDADO);
            
            solicitud.setEstado(EstadoProcesoBPM.ASIGNANDO);
            registrarEvento(solicitud.getIdProceso(), "ASIGNACION", "Asignando recursos para la cita", null);
            
            boolean recursosAsignados = asignarRecursos(solicitud);
            if (!recursosAsignados) {
                throw new RuntimeException("No se pudieron asignar recursos");
            }
            
            solicitud.setEstado(EstadoProcesoBPM.ASIGNADO);
            
            solicitud.setEstado(EstadoProcesoBPM.CONFIRMANDO);
            registrarEvento(solicitud.getIdProceso(), "CONFIRMACION", "Confirmando cita con paciente", null);
            
            CitasEntity cita = crearCita(solicitud);
            if (cita == null) {
                throw new RuntimeException("No se pudo crear la cita");
            }
            
            solicitud.setEstado(EstadoProcesoBPM.COMPLETADO);
            registrarEvento(solicitud.getIdProceso(), "EXITO", "Cita creada exitosamente", cita);
            
            enviarNotificacion(solicitud, cita);
            
            log.info("Proceso BPM completado exitosamente: {}", solicitud.getIdProceso());
            
        } catch (Exception e) {
            log.error("Error en proceso BPM: {}", solicitud.getIdProceso(), e);
            solicitud.setEstado(EstadoProcesoBPM.ERROR);
            solicitud.setErrorMensaje(e.getMessage());
            
            registrarEvento(solicitud.getIdProceso(), "ERROR", "Error en proceso BPM: " + e.getMessage(), null);
            
            // Usar ESB para manejar el flujo alterno de error
            esbService.handleAlternateFlow(solicitud, e);
        }
    }

    @Transactional
    public void compensarProceso(SolicitudCitaBPM solicitud) {
        try {
            log.info("Iniciando compensación para proceso: {}", solicitud.getIdProceso());
            
            revertirCambios(solicitud);
            
            solicitud.setEstado(EstadoProcesoBPM.COMPENSADO);
            registrarEvento(solicitud.getIdProceso(), "COMPENSACION", "Proceso compensado exitosamente", null);
            
            log.info("Compensación completada para proceso: {}", solicitud.getIdProceso());
            
        } catch (Exception e) {
            log.error("Error en compensación: {}", solicitud.getIdProceso(), e);
            registrarEvento(solicitud.getIdProceso(), "ERROR_COMPENSACION", "Error en compensación: " + e.getMessage(), null);
        }
    }

    private boolean validarDatos(SolicitudCitaBPM solicitud) {
        boolean pacienteExiste = pacientesRepository.existsById(solicitud.getPacienteId());
        if (!pacienteExiste) {
            throw new RuntimeException("Paciente no encontrado: " + solicitud.getPacienteId());
        }
        
        boolean terapeutaExiste = terapeutasRepository.existsById(solicitud.getTerapeutaId());
        if (!terapeutaExiste) {
            throw new RuntimeException("Terapeuta no encontrado: " + solicitud.getTerapeutaId());
        }
        
        return true;
    }

    private boolean asignarRecursos(SolicitudCitaBPM solicitud) {
        return true;
    }

    private CitasEntity crearCita(SolicitudCitaBPM solicitud) {
        try {
            CitasEntity cita = new CitasEntity();
            cita.setPaciente(pacientesRepository.findById(solicitud.getPacienteId())
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado")));
            cita.setTerapeuta(terapeutasRepository.findById(solicitud.getTerapeutaId())
                    .orElseThrow(() -> new RuntimeException("Terapeuta no encontrado")));
            cita.setFechaCita(LocalDateTime.now().plusDays(1));
            cita.setDuracionMinutos(60);
            cita.setEstadoCita("PROGRAMADA");
            cita.setObservaciones("Cita creada via BPM - " + solicitud.getTipoCita());
            
            return citaService.save(cita);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al crear cita: " + e.getMessage(), e);
        }
    }

    private void revertirCambios(SolicitudCitaBPM solicitud) {
        log.info("Revertiendo cambios para proceso: {}", solicitud.getIdProceso());
    }

    private void enviarNotificacion(SolicitudCitaBPM solicitud, CitasEntity cita) {
        try {
            EventoProcesoBPM evento = new EventoProcesoBPM();
            evento.setIdProceso(solicitud.getIdProceso());
            evento.setTipoEvento("NOTIFICACION");
            evento.setMensaje("Notificación enviada al paciente");
            evento.setTimestamp(LocalDateTime.now());
            evento.setDatos(cita);
            
            jmsTemplate.convertAndSend("bpm.notificaciones.queue", evento);
            
            solicitud.setEstado(EstadoProcesoBPM.NOTIFICADO);
            
        } catch (Exception e) {
            log.warn("Error al enviar notificación: {}", e.getMessage());
        }
    }

    private void registrarEvento(String idProceso, String tipo, String mensaje, Object datos) {
        EventoProcesoBPM evento = new EventoProcesoBPM();
        evento.setIdProceso(idProceso);
        evento.setTipoEvento(tipo);
        evento.setMensaje(mensaje);
        evento.setTimestamp(LocalDateTime.now());
        evento.setDatos(datos);
        
        log.info("Evento BPM: {} - {} - {}", idProceso, tipo, mensaje);
    }
}