package com.clinica.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import jakarta.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;

@Configuration
public class JmsConfig {

    @Value("${jms.queue.bpm.main:bpm.main.queue}")
    private String bpmMainQueue;

    @Value("${jms.queue.bpm.compensacion:bpm.compensacion.queue}")
    private String bpmCompensacionQueue;

    @Value("${jms.queue.bpm.notificaciones:bpm.notificaciones.queue}")
    private String bpmNotificacionesQueue;

    @Value("${jms.queue.bpm.error:bpm.error.queue}")
    private String bpmErrorQueue;

    @Bean
    public Queue bpmMainQueue() {
        return new ActiveMQQueue(bpmMainQueue);
    }

    @Bean
    public Queue bpmCompensacionQueue() {
        return new ActiveMQQueue(bpmCompensacionQueue);
    }

    @Bean
    public Queue bpmNotificacionesQueue() {
        return new ActiveMQQueue(bpmNotificacionesQueue);
    }

    @Bean
    public Queue bpmErrorQueue() {
        return new ActiveMQQueue(bpmErrorQueue);
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}