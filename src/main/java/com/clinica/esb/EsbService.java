package com.clinica.esb;

import com.clinica.model.dto.bpm.EventoProcesoBPM;
import com.clinica.model.dto.bpm.SolicitudCitaBPM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EsbService {

    private final JmsTemplate jmsTemplate;

    public EsbService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

   
    public void routeMessage(String eventType, Object payload) {
        log.info("ESB - Enrutando mensaje de tipo: {}", eventType);
        
        switch (eventType) {
            case "CITA_NUEVA":
                routeToBpmMainQueue(payload);
                break;
            case "ERROR":
                routeToErrorQueue(payload);
                break;
            case "COMPENSACION":
                routeToCompensacionQueue(payload);
                break;
            case "NOTIFICACION":
                routeToNotificacionQueue(payload);
                break;
            default:
                log.warn("ESB - Tipo de evento desconocido: {}", eventType);
        }
    }

    /**
     * Transforma y envía mensaje a la cola principal de BPM
     */
    private void routeToBpmMainQueue(Object payload) {
        log.info("ESB - Enviando a cola BPM principal");
        jmsTemplate.convertAndSend("bpm.main.queue", payload);
        auditarMensaje("BPM_MAIN", payload);
    }

    /**
     * Maneja errores y los envía a la cola de errores
     */
    private void routeToErrorQueue(Object payload) {
        log.error("ESB - Enrutando mensaje a cola de errores");
        jmsTemplate.convertAndSend("bpm.error.queue", payload);
        auditarMensaje("ERROR", payload);
    }

    /**
     * Envía mensaje a la cola de compensación para rollback
     */
    private void routeToCompensacionQueue(Object payload) {
        log.warn("ESB - Enviando a cola de compensación");
        jmsTemplate.convertAndSend("bpm.compensacion.queue", payload);
        auditarMensaje("COMPENSACION", payload);
    }

    /**
     * Envía notificaciones a los usuarios
     */
    private void routeToNotificacionQueue(Object payload) {
        log.info("ESB - Enviando a cola de notificaciones");
        jmsTemplate.convertAndSend("bpm.notificaciones.queue", payload);
        auditarMensaje("NOTIFICACION", payload);
    }

    /**
     * Audita todos los mensajes que pasan por el ESB
     */
    private void auditarMensaje(String destino, Object payload) {
        log.info("ESB AUDIT - Destino: {} | Timestamp: {} | Payload: {}", 
                 destino, LocalDateTime.now(), payload.getClass().getSimpleName());
    }

    /**
     * Maneja el flujo alterno cuando ocurre un error crítico
     */
    public void handleAlternateFlow(SolicitudCitaBPM solicitud, Exception error) {
        log.error("ESB - Activando flujo alterno para proceso: {}", solicitud.getIdProceso());
        
        // 1. Registrar el error
        EventoProcesoBPM eventoError = new EventoProcesoBPM();
        eventoError.setIdProceso(solicitud.getIdProceso());
        eventoError.setTipoEvento("ERROR_CRITICO");
        eventoError.setMensaje("Error crítico: " + error.getMessage());
        eventoError.setTimestamp(LocalDateTime.now());
        eventoError.setDatos(solicitud);
        
        // 2. Enviar a cola de errores
        routeToErrorQueue(solicitud);
        
        // 3. Iniciar compensación
        routeToCompensacionQueue(solicitud);
        
        // 4. Notificar administradores
        EventoProcesoBPM notificacion = new EventoProcesoBPM();
        notificacion.setIdProceso(solicitud.getIdProceso());
        notificacion.setTipoEvento("ALERTA_ADMINISTRADOR");
        notificacion.setMensaje("Se requiere intervención manual para el proceso: " + solicitud.getIdProceso());
        notificacion.setTimestamp(LocalDateTime.now());
        notificacion.setDatos(error.getMessage());
        
        routeToNotificacionQueue(notificacion);
        
        log.info("ESB - Flujo alterno completado para proceso: {}", solicitud.getIdProceso());
    }

    /**
     * Transforma mensajes entre diferentes formatos si es necesario
     */
    public Object transformMessage(Object source, String targetFormat) {
        log.info("ESB - Transformando mensaje a formato: {}", targetFormat);
        // Aquí se puede implementar lógica de transformación según sea necesario
        return source;
    }
}
