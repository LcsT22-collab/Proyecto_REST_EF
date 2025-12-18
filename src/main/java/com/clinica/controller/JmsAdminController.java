package com.clinica.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

@RestController
@RequestMapping("/api/v1/jms")
@Tag(name = "JMS Admin", description = "Endpoints para inspeccionar colas en ActiveMQ")
public class JmsAdminController {

    private final ConnectionFactory connectionFactory;

    public JmsAdminController(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @GetMapping("/count")
    @Operation(summary = "Contar mensajes en una cola", description = "Devuelve la cantidad de mensajes en la cola especificada")
    public ResponseEntity<Map<String, Object>> count(@RequestParam String queue) throws JMSException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("queue", queue);
        payload.put("count", browse(queue, 0).size());
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/messages")
    @Operation(summary = "Listar mensajes de una cola", description = "Navega mensajes sin consumirlos (máximo configurable)")
    public ResponseEntity<Map<String, Object>> messages(
            @RequestParam String queue,
            @RequestParam(name = "max", defaultValue = "10") int max) throws JMSException {
        List<String> messages = browse(queue, max);
        Map<String, Object> payload = new HashMap<>();
        payload.put("queue", queue);
        payload.put("messages", messages);
        payload.put("count", messages.size());
        return ResponseEntity.ok(payload);
    }

    private List<String> browse(String queueName, int max) throws JMSException {
        List<String> bodies = new ArrayList<>();
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            Queue queue = session.createQueue(queueName);
            QueueBrowser browser = session.createBrowser(queue);
            connection.start();
            Enumeration<?> enumeration = browser.getEnumeration();
            int read = 0;
            while (enumeration.hasMoreElements()) {
                Message message = (Message) enumeration.nextElement();
                String body = extractBody(message);
                bodies.add(body);
                read++;
                if (max > 0 && read >= max) {
                    break;
                }
            }
            browser.close();
        }
        return bodies;
    }

    private String extractBody(Message message) throws JMSException {
        if (message instanceof TextMessage textMessage) {
            return textMessage.getText();
        }
        return "Unsupported message type: " + message.getClass().getSimpleName();
    }
}
