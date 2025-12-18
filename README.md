# PRUEBAS RAPIDAS

## Arrancar app
cd c:\Users\Angelo\Desktop\EF-CHE
mvnw.cmd spring-boot:run

## Swagger
http://localhost:8080/clinica/swagger-ui.html

## Actuator (health/info)
http://localhost:8080/clinica/actuator/health
http://localhost:8080/clinica/actuator/info

## JSON de ejemplo
Paciente:
{"nombre":"Juan","apellidos":"Perez","dni":"12345678"}
{"nombre":"Ana","apellidos":"Lopez","dni":"87654321"}
{"nombre":"Carlos","apellidos":"Diaz","dni":"44556677"}

Terapeuta:
{"nombre":"Laura","apellidos":"Garcia","especialidad":"Fisio"}
{"nombre":"Miguel","apellidos":"Ramos","especialidad":"Trauma"}
{"nombre":"Sofia","apellidos":"Vega","especialidad":"Pediatria"}

Disciplina:
{"nombre":"Fisioterapia","descripcion":"Rehab general"}
{"nombre":"Traumatologia","descripcion":"Lesiones deportivas"}
{"nombre":"Pediatria","descripcion":"Niños"}

Horario:
{"terapeutaId":1,"dia":"LUNES","horaInicio":"09:00","horaFin":"12:00"}
{"terapeutaId":1,"dia":"MARTES","horaInicio":"14:00","horaFin":"18:00"}
{"terapeutaId":2,"dia":"MIERCOLES","horaInicio":"10:00","horaFin":"13:00"}

Evaluacion:
{"pacienteId":1,"terapeutaId":1,"diagnostico":"Lumbalgia","planTratamiento":"10 sesiones"}
{"pacienteId":2,"terapeutaId":1,"diagnostico":"Esguince","planTratamiento":"Reposo y fisio"}
{"pacienteId":1,"terapeutaId":2,"diagnostico":"Tendinitis","planTratamiento":"Hielo y estiramientos"}

Pago:
{"pacienteId":1,"monto":100.0,"metodo":"EFECTIVO","descripcion":"Sesion 1"}
{"pacienteId":1,"monto":120.5,"metodo":"TARJETA","descripcion":"Sesion 2"}
{"pacienteId":2,"monto":90.0,"metodo":"EFECTIVO","descripcion":"Sesion inicial"}

Cita feliz:
{"pacienteId":1,"terapeutaId":1,"fecha":"2025-12-20","hora":"10:00","motivo":"Rehab"}
{"pacienteId":2,"terapeutaId":1,"fecha":"2025-12-21","hora":"11:00","motivo":"Esguince"}
{"pacienteId":1,"terapeutaId":2,"fecha":"2025-12-22","hora":"15:00","motivo":"Tendinitis"}

Cita error:
{"pacienteId":9999,"terapeutaId":1,"fecha":"2025-12-20","hora":"10:00","motivo":"Test error"}

## Curl listos
Paciente:
curl -X POST http://localhost:8080/clinica/api/v1/pacientes -H "Content-Type: application/json" -d "{\"nombre\":\"Juan\",\"apellidos\":\"Perez\",\"dni\":\"12345678\"}"

Terapeuta:
curl -X POST http://localhost:8080/clinica/api/v1/terapeutas -H "Content-Type: application/json" -d "{\"nombre\":\"Laura\",\"apellidos\":\"Garcia\",\"especialidad\":\"Fisio\"}"

Disciplina:
curl -X POST http://localhost:8080/clinica/api/v1/disciplinas -H "Content-Type: application/json" -d "{\"nombre\":\"Fisioterapia\",\"descripcion\":\"Rehab general\"}"

Horario:
curl -X POST http://localhost:8080/clinica/api/v1/horarios -H "Content-Type: application/json" -d "{\"terapeutaId\":1,\"dia\":\"LUNES\",\"horaInicio\":\"09:00\",\"horaFin\":\"12:00\"}"

Evaluacion:
curl -X POST http://localhost:8080/clinica/api/v1/evaluaciones -H "Content-Type: application/json" -d "{\"pacienteId\":1,\"terapeutaId\":1,\"diagnostico\":\"Lumbalgia\",\"planTratamiento\":\"10 sesiones\"}"

Pago:
curl -X POST http://localhost:8080/clinica/api/v1/pagos -H "Content-Type: application/json" -d "{\"pacienteId\":1,\"monto\":100.0,\"metodo\":\"EFECTIVO\",\"descripcion\":\"Sesion 1\"}"

Cita feliz:
curl -X POST http://localhost:8080/clinica/api/v1/citas -H "Content-Type: application/json" -d "{\"pacienteId\":1,\"terapeutaId\":1,\"fecha\":\"2025-12-20\",\"hora\":\"10:00\",\"motivo\":\"Rehab\"}"

Cita error:
curl -X POST http://localhost:8080/clinica/api/v1/citas -H "Content-Type: application/json" -d "{\"pacienteId\":9999,\"terapeutaId\":1,\"fecha\":\"2025-12-20\",\"hora\":\"10:00\",\"motivo\":\"Test error\"}"

## Ver colas via REST (no consume mensajes)
Conteo:
GET http://localhost:8080/clinica/api/v1/jms/count?queue=bpm.main.queue
GET http://localhost:8080/clinica/api/v1/jms/count?queue=bpm.notificaciones.queue
GET http://localhost:8080/clinica/api/v1/jms/count?queue=bpm.error.queue
GET http://localhost:8080/clinica/api/v1/jms/count?queue=bpm.compensacion.queue

Mensajes (max 10):
GET http://localhost:8080/clinica/api/v1/jms/messages?queue=bpm.main.queue&max=10
GET http://localhost:8080/clinica/api/v1/jms/messages?queue=bpm.error.queue&max=10

## Consola web ActiveMQ
http://localhost:8161/admin  (admin / admin)
Revisar colas: bpm.main.queue, bpm.notificaciones.queue, bpm.error.queue, bpm.compensacion.queue
