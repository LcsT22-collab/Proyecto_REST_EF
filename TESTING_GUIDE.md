# GUÍA DE PRUEBAS - Sistema de Gestión de Clínica

## 🚀 Inicio Rápido

### 1. Compilar el proyecto
```bash
./mvnw clean install
```

### 2. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080/clinica`

---

## 📡 ENDPOINTS REST API

### Base URL: `http://localhost:8080/clinica/api/v1`

### 1️⃣ **Pacientes** (`/pacientes`)

#### Listar todos los pacientes
```http
GET /pacientes
```

#### Obtener paciente por ID
```http
GET /pacientes/{id}
```

#### Crear paciente
```http
POST /pacientes
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "fechaNacimiento": "2010-05-15",
  "nombreTutor": "María Pérez",
  "telefono": "555-1234",
  "correo": "maria.perez@email.com",
  "direccion": "Av. Principal 123"
}
```

#### Actualizar paciente
```http
PUT /pacientes/{id}
Content-Type: application/json

{
  "nombre": "Juan Pérez Actualizado",
  "telefono": "555-5678"
}
```

#### Desactivar paciente
```http
DELETE /pacientes/{id}
```

---

### 2️⃣ **Terapeutas** (`/terapeutas`)

#### Listar todos los terapeutas
```http
GET /terapeutas
```

#### Crear terapeuta
```http
POST /terapeutas
Content-Type: application/json

{
  "nombre": "Dra. Ana García",
  "disciplinaId": 1,
  "telefono": "555-9999",
  "correo": "ana.garcia@clinica.com",
  "disponibilidad": "DISPONIBLE"
}
```

---

### 3️⃣ **Citas** (`/citas`)

#### Listar todas las citas
```http
GET /citas
```

#### Crear cita
```http
POST /citas
Content-Type: application/json

{
  "pacienteId": 1,
  "terapeutaId": 1,
  "fechaCita": "2024-12-20T10:00:00",
  "duracionMinutos": 60,
  "estadoCita": "PROGRAMADA",
  "observaciones": "Primera sesión"
}
```

---

### 4️⃣ **Disciplinas** (`/disciplinas`)

#### Listar todas las disciplinas
```http
GET /disciplinas
```

#### Crear disciplina
```http
POST /disciplinas
Content-Type: application/json

{
  "nombre": "Fisioterapia",
  "descripcion": "Tratamiento de lesiones",
  "estadoDisciplina": "ACTIVA"
}
```

---

### 5️⃣ **Evaluaciones** (`/evaluaciones`)

#### Listar evaluaciones
```http
GET /evaluaciones
```

#### Crear evaluación
```http
POST /evaluaciones
Content-Type: application/json

{
  "pacienteId": 1,
  "fechaEvaluacion": "2024-12-17",
  "tipoEvaluacion": "Evaluación Inicial",
  "resultados": "Progreso satisfactorio",
  "recomendaciones": "Continuar con sesiones semanales"
}
```

---

### 6️⃣ **Pagos** (`/pagos`)

#### Listar pagos
```http
GET /pagos
```

#### Crear pago
```http
POST /pagos
Content-Type: application/json

{
  "pacienteId": 1,
  "citaId": 1,
  "monto": 150.00,
  "fechaPago": "2024-12-17",
  "metodoPago": "EFECTIVO",
  "estadoPago": "PAGADO"
}
```

---

### 7️⃣ **Horarios** (`/horarios`)

#### Listar horarios
```http
GET /horarios
```

#### Crear horario
```http
POST /horarios
Content-Type: application/json

{
  "terapeutaId": 1,
  "diaSemana": "LUNES",
  "horaInicio": "08:00:00",
  "horaFin": "14:00:00"
}
```

---

## 🔄 ENDPOINTS BPM (Business Process Management)

### Base URL: `http://localhost:8080/clinica/api/v1/bpm`

#### Iniciar proceso BPM para crear cita
```http
POST /bpm/cita?pacienteId=1&terapeutaId=1&tipoCita=CONSULTA
```

**Respuesta exitosa:**
```json
{
  "idProceso": "550e8400-e29b-41d4-a716-446655440000",
  "pacienteId": 1,
  "terapeutaId": 1,
  "tipoCita": "CONSULTA",
  "estado": "SOLICITADO",
  "fechaSolicitud": "2024-12-17T10:30:00"
}
```

#### Verificar estado del sistema BPM
```http
GET /bpm/status
```

---

## 🧪 FLUJO BPM COMPLETO

### Flujo Normal (Happy Path)
1. **SOLICITADO** → Usuario solicita crear una cita
2. **VALIDANDO** → Sistema valida datos del paciente y terapeuta
3. **VALIDADO** → Datos correctos
4. **ASIGNANDO** → Sistema asigna recursos
5. **ASIGNADO** → Recursos asignados correctamente
6. **CONFIRMANDO** → Sistema crea la cita
7. **COMPLETADO** → Cita creada exitosamente
8. **NOTIFICADO** → Notificación enviada al paciente

### Flujo Alterno (Error Flow)
1. **ERROR** → Ocurre un error en cualquier etapa
2. **ESB enruta** → El ESB detecta el error y activa flujo alterno
3. **ERROR QUEUE** → Mensaje enviado a cola de errores
4. **COMPENSACION QUEUE** → Mensaje enviado a cola de compensación
5. **NOTIFICACION QUEUE** → Alerta enviada a administradores
6. **COMPENSADO** → Proceso compensado (rollback)

---

## 🧼 SERVICIOS SOAP

### WSDL Endpoint
```
http://localhost:8080/clinica/ws/pacientes.wsdl
```

### Consultar Paciente por ID
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:pac="http://clinica.com/pacientes">
   <soapenv:Header/>
   <soapenv:Body>
      <pac:ConsultarPacienteRequest>
         <pac:idPaciente>1</pac:idPaciente>
      </pac:ConsultarPacienteRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### Listar Todos los Pacientes
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:pac="http://clinica.com/pacientes">
   <soapenv:Header/>
   <soapenv:Body>
      <pac:ListarPacientesRequest/>
   </soapenv:Body>
</soapenv:Envelope>
```

---

## 📊 COLAS JMS (ActiveMQ)

### Configuración
- **Broker URL**: `tcp://localhost:61616`
- **Usuario**: admin
- **Contraseña**: admin

### Colas Disponibles
1. **bpm.main.queue** - Cola principal para procesos BPM
2. **bpm.compensacion.queue** - Cola para procesos de compensación
3. **bpm.notificaciones.queue** - Cola para notificaciones
4. **bpm.error.queue** - Cola para manejo de errores

### Consola ActiveMQ
```
http://localhost:8161/admin
Usuario: admin
Contraseña: admin
```

---

## 📚 DOCUMENTACIÓN API (Swagger)

### Swagger UI
```
http://localhost:8080/clinica/swagger-ui.html
```

### OpenAPI JSON
```
http://localhost:8080/clinica/api-docs
```

---

## 🗄️ BASE DE DATOS H2

### Consola H2
```
http://localhost:8080/clinica/h2-console
```

**Credenciales:**
- **JDBC URL**: `jdbc:h2:mem:clinicadb`
- **Usuario**: `sa`
- **Contraseña**: (vacío)

---

## ✅ PRUEBAS CON CURL

### Crear Paciente
```bash
curl -X POST http://localhost:8080/clinica/api/v1/pacientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test Paciente",
    "fechaNacimiento": "2010-01-01",
    "nombreTutor": "Tutor Test",
    "telefono": "555-TEST",
    "correo": "test@test.com"
  }'
```

### Iniciar Proceso BPM
```bash
curl -X POST "http://localhost:8080/clinica/api/v1/bpm/cita?pacienteId=1&terapeutaId=1&tipoCita=CONSULTA"
```

### Listar Pacientes
```bash
curl http://localhost:8080/clinica/api/v1/pacientes
```

---

## 🧪 PRUEBAS UNITARIAS

### Ejecutar todas las pruebas
```bash
./mvnw test
```

### Ejecutar con cobertura
```bash
./mvnw test jacoco:report
```

---

## 🐛 LOGS Y DEBUGGING

Los logs están configurados en diferentes niveles:
- **INFO**: General de la aplicación
- **DEBUG**: BPM Service y JMS Consumers
- **ERROR**: Errores y excepciones

Verifica los logs en la consola para seguir el flujo completo de los procesos BPM.

---

## 📋 CHECKLIST DE VALIDACIÓN

- [ ] ✅ La aplicación compila sin errores
- [ ] ✅ Todos los 7 servicios REST funcionan correctamente
- [ ] ✅ Swagger UI muestra toda la documentación
- [ ] ✅ H2 Console es accesible
- [ ] ✅ Proceso BPM se ejecuta completamente
- [ ] ✅ Flujo alterno de error funciona
- [ ] ✅ Colas JMS procesan mensajes
- [ ] ✅ ESB enruta mensajes correctamente
- [ ] ✅ Servicio SOAP responde correctamente
- [ ] ✅ Datos de prueba se cargan automáticamente
