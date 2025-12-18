# Sistema de Gestión de Clínica - SOA/ESB/BPM

Sistema REST con arquitectura SOA, orquestación BPM, integración ESB mediante colas JMS en ActiveMQ.

**Stack:** Spring Boot 3.5.1 | Java 21 | MySQL | ActiveMQ | Hibernate | Swagger OpenAPI

---

## Arrancar la app

```bash
cd c:\Users\Angelo\Desktop\EF-CHE
mvnw.cmd spring-boot:run
```

## URLs principales

- **Swagger**: http://localhost:8080/clinica/swagger-ui.html
- **Health**: http://localhost:8080/clinica/actuator/health
- **ActiveMQ Console**: http://localhost:8161/admin (admin/admin)

## JSON de ejemplo

### Paciente
```json
{"nombre":"Juan","apellidos":"Perez","dni":"12345678"}
{"nombre":"Ana","apellidos":"Lopez","dni":"87654321"}
{"nombre":"Carlos","apellidos":"Diaz","dni":"44556677"}
```

### Terapeuta
```json
{"nombre":"Laura","apellidos":"Garcia","especialidad":"Fisio"}
{"nombre":"Miguel","apellidos":"Ramos","especialidad":"Trauma"}
{"nombre":"Sofia","apellidos":"Vega","especialidad":"Pediatria"}
```

### Disciplina
```json
{"nombre":"Fisioterapia","descripcion":"Rehab general"}
{"nombre":"Traumatologia","descripcion":"Lesiones deportivas"}
{"nombre":"Pediatria","descripcion":"Niños"}
```

### Horario
```json
{"terapeutaId":1,"dia":"LUNES","horaInicio":"09:00","horaFin":"12:00"}
{"terapeutaId":1,"dia":"MARTES","horaInicio":"14:00","horaFin":"18:00"}
{"terapeutaId":2,"dia":"MIERCOLES","horaInicio":"10:00","horaFin":"13:00"}
```

### Evaluación
```json
{"pacienteId":1,"terapeutaId":1,"diagnostico":"Lumbalgia","planTratamiento":"10 sesiones"}
{"pacienteId":2,"terapeutaId":1,"diagnostico":"Esguince","planTratamiento":"Reposo y fisio"}
{"pacienteId":1,"terapeutaId":2,"diagnostico":"Tendinitis","planTratamiento":"Hielo y estiramientos"}
```

### Pago
```json
{"pacienteId":1,"monto":100.0,"metodo":"EFECTIVO","descripcion":"Sesion 1"}
{"pacienteId":1,"monto":120.5,"metodo":"TARJETA","descripcion":"Sesion 2"}
{"pacienteId":2,"monto":90.0,"metodo":"EFECTIVO","descripcion":"Sesion inicial"}
```

### Cita (feliz path)
```json
{"pacienteId":1,"terapeutaId":1,"fecha":"2025-12-20","hora":"10:00","motivo":"Rehab"}
{"pacienteId":2,"terapeutaId":1,"fecha":"2025-12-21","hora":"11:00","motivo":"Esguince"}
{"pacienteId":1,"terapeutaId":2,"fecha":"2025-12-22","hora":"15:00","motivo":"Tendinitis"}
```

### Cita (error path - compensación)
```json
{"pacienteId":9999,"terapeutaId":1,"fecha":"2025-12-20","hora":"10:00","motivo":"Test error"}
```

## Curl listos

**Paciente:**
```bash
curl -X POST http://localhost:8080/clinica/api/v1/pacientes -H "Content-Type: application/json" -d "{\"nombre\":\"Juan\",\"apellidos\":\"Perez\",\"dni\":\"12345678\"}"
```

**Terapeuta:**
```bash
curl -X POST http://localhost:8080/clinica/api/v1/terapeutas -H "Content-Type: application/json" -d "{\"nombre\":\"Laura\",\"apellidos\":\"Garcia\",\"especialidad\":\"Fisio\"}"
```

**Disciplina:**
```bash
curl -X POST http://localhost:8080/clinica/api/v1/disciplinas -H "Content-Type: application/json" -d "{\"nombre\":\"Fisioterapia\",\"descripcion\":\"Rehab general\"}"
```

**Horario:**
```bash
curl -X POST http://localhost:8080/clinica/api/v1/horarios -H "Content-Type: application/json" -d "{\"terapeutaId\":1,\"dia\":\"LUNES\",\"horaInicio\":\"09:00\",\"horaFin\":\"12:00\"}"
```

**Evaluación:**
```bash
curl -X POST http://localhost:8080/clinica/api/v1/evaluaciones -H "Content-Type: application/json" -d "{\"pacienteId\":1,\"terapeutaId\":1,\"diagnostico\":\"Lumbalgia\",\"planTratamiento\":\"10 sesiones\"}"
```

**Pago:**
```bash
curl -X POST http://localhost:8080/clinica/api/v1/pagos -H "Content-Type: application/json" -d "{\"pacienteId\":1,\"monto\":100.0,\"metodo\":\"EFECTIVO\",\"descripcion\":\"Sesion 1\"}"
```

**Cita (feliz):**
```bash
curl -X POST http://localhost:8080/clinica/api/v1/citas -H "Content-Type: application/json" -d "{\"pacienteId\":1,\"terapeutaId\":1,\"fecha\":\"2025-12-20\",\"hora\":\"10:00\",\"motivo\":\"Rehab\"}"
```

**Cita (error):**
```bash
curl -X POST http://localhost:8080/clinica/api/v1/citas -H "Content-Type: application/json" -d "{\"pacienteId\":9999,\"terapeutaId\":1,\"fecha\":\"2025-12-20\",\"hora\":\"10:00\",\"motivo\":\"Test error\"}"
```

## Ver colas JMS

**Conteo de mensajes:**
```
GET http://localhost:8080/clinica/api/v1/jms/count?queue=bpm.main.queue
GET http://localhost:8080/clinica/api/v1/jms/count?queue=bpm.notificaciones.queue
GET http://localhost:8080/clinica/api/v1/jms/count?queue=bpm.error.queue
GET http://localhost:8080/clinica/api/v1/jms/count?queue=bpm.compensacion.queue
```

**Listar mensajes (máx 10):**
```
GET http://localhost:8080/clinica/api/v1/jms/messages?queue=bpm.main.queue&max=10
GET http://localhost:8080/clinica/api/v1/jms/messages?queue=bpm.error.queue&max=10
```

## Requisitos

- **Java 21+**
- **Maven 3.8.9+**
- **MySQL 8.0+** con BD `clinicadb`
- **ActiveMQ 5.15+** corriendo en `tcp://localhost:61616`

## Configuración

- `src/main/resources/application.properties` - Conexión MySQL, ActiveMQ, JMS
- `src/main/resources/application-test.properties` - Perfil test con H2 en memoria

## Estructura

```
src/
├── main/
│   ├── java/com/clinica/
│   │   ├── controller/        (7 REST services)
│   │   ├── service/           (BPM orchestration, CRUD)
│   │   ├── jms/consumer/      (4 JMS queue consumers)
│   │   ├── model/             (DTO, Entity, Enum, Repository)
│   │   └── config/            (Swagger, JMS configuration)
│   └── resources/
│       ├── application.properties
│       └── application-test.properties
└── test/
    └── java/com/clinica/
        └── ClinicaApplicationTests.java
```

## BPM + ESB + JMS

- **BPM:** Flujo orquestado al crear cita (validación, notificación, compensación).
- **ESB:** Desacoplamiento entre servicios vía colas JMS.
- **JMS:** 4 colas (main, notificaciones, error, compensación).
- **Consumers:** 4 listeners escuchando colas para procesar mensajes.

---

**Autor:** Angelo  
**Versión:** 1.0.0  
**Última actualización:** Diciembre 17, 2025
