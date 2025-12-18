# Endpoints REST para Integración Bonita BPM

## Base URL
`http://localhost:8080/clinica`

## Headers Requeridos
- **Content-Type**: `application/json`
- **Accept**: `application/json`

---

## 1. Validación de Paciente

**GET** `/api/v1/pacientes/{pacienteId}`

### Descripción
Valida que existe un paciente en el sistema.

### Parámetros
- `pacienteId` (path): ID del paciente

### Respuestas
- **200 OK**: Paciente encontrado
  ```json
  {
    "idPaciente": 1,
    "nombre": "Juan Pérez",
    "fechaNacimiento": "2010-05-15",
    "nombreTutor": "María Pérez",
    "telefono": "555-1234",
    "correo": "maria@example.com",
    "direccion": "Calle 123",
    "fechaRegistro": "2024-01-15T10:00:00",
    "activo": true
  }
  ```
- **404 Not Found**: Paciente no existe

---

## 2. Validación de Terapeuta

**GET** `/api/v1/terapeutas/{terapeutaId}`

### Descripción
Valida que existe un terapeuta en el sistema.

### Parámetros
- `terapeutaId` (path): ID del terapeuta

### Respuestas
- **200 OK**: Terapeuta encontrado
  ```json
  {
    "idTerapeuta": 1,
    "nombre": "Dr. González",
    "especialidad": "Fisioterapia",
    "disponibilidad": "DISPONIBLE",
    "disciplinaId": 1,
    "nombreDisciplina": "Fisioterapia",
    "codigoLicencia": "FT-12345",
    "telefono": "555-5678",
    "correo": "gonzalez@clinica.com",
    "activo": true
  }
  ```
- **404 Not Found**: Terapeuta no existe

---

## 3. Asignar Recursos (Stub)

**POST** `/api/v1/bpm/asignar`

### Descripción
Endpoint stub para asignar recursos. Siempre retorna éxito.

### Request Body
```json
{
  "terapeutaId": 1,
  "tipoCita": "CONSULTA",
  "idTransaccion": "TRX-2024-001"
}
```

### Respuesta
- **200 OK**: Recursos asignados
  ```json
  {
    "exito": true,
    "mensaje": "Recursos asignados exitosamente",
    "idTransaccion": "TRX-2024-001",
    "terapeutaId": 1,
    "tipoCita": "CONSULTA"
  }
  ```

---

## 4. Crear Cita

**POST** `/api/v1/citas`

### Descripción
Crea una nueva cita en el sistema.

### Request Body (CitaDTO)
```json
{
  "pacienteId": 1,
  "terapeutaId": 1,
  "fechaCita": "2024-12-20T10:00:00",
  "duracionMinutos": 60,
  "estadoCita": "PENDIENTE",
  "observaciones": "Primera consulta"
}
```

### Campos
- `pacienteId`: ID del paciente (requerido)
- `terapeutaId`: ID del terapeuta (requerido)
- `fechaCita`: Fecha y hora de la cita en formato ISO-8601 (requerido)
- `duracionMinutos`: Duración en minutos (requerido)
- `estadoCita`: Estado de la cita - valores: `PROGRAMADA`, `CONFIRMADA`, `CANCELADA`, `COMPLETADA`, `PENDIENTE`
- `observaciones`: Notas adicionales (opcional)

### Respuesta
- **200 OK**: Cita creada exitosamente
  ```json
  {
    "idCita": 15,
    "pacienteId": 1,
    "nombrePaciente": "Juan Pérez",
    "terapeutaId": 1,
    "nombreTerapeuta": "Dr. González",
    "fechaCita": "2024-12-20T10:00:00",
    "duracionMinutos": 60,
    "estadoCita": "PENDIENTE",
    "observaciones": "Primera consulta"
  }
  ```
- **400 Bad Request**: Datos inválidos

---

## 5. Publicar Notificación

**POST** `/api/v1/bpm/notificar`

### Descripción
Publica un mensaje en la cola `bpm.notificaciones.queue`.

### Request Body
```json
{
  "cola": "bpm.notificaciones.queue",
  "mensaje": {
    "idTransaccion": "TRX-2024-001",
    "pacienteId": 1,
    "estado": "CONFIRMADO",
    "detalles": {
      "citaId": 15,
      "fechaCita": "2024-12-20T10:00:00"
    }
  }
}
```

### Respuesta
- **200 OK**: Mensaje publicado
  ```json
  {
    "estado": "OK",
    "mensaje": "Mensaje publicado en bpm.notificaciones.queue"
  }
  ```
- **500 Internal Server Error**: Error al publicar

---

## 6. Publicar Error

**POST** `/api/v1/bpm/error`

### Descripción
Publica un mensaje en la cola `bpm.error.queue`.

### Request Body
```json
{
  "cola": "bpm.error.queue",
  "mensaje": {
    "idTransaccion": "TRX-2024-001",
    "error": "VALIDACION_FALLIDA",
    "detalles": "El terapeuta no está disponible en la fecha solicitada"
  }
}
```

### Respuesta
- **200 OK**: Error reportado
  ```json
  {
    "estado": "OK",
    "mensaje": "Error reportado en bpm.error.queue"
  }
  ```
- **500 Internal Server Error**: Error al publicar

---

## 7. Publicar Compensación

**POST** `/api/v1/bpm/compensar`

### Descripción
Publica un mensaje en la cola `bpm.compensacion.queue` para revertir operaciones.

### Request Body
```json
{
  "cola": "bpm.compensacion.queue",
  "mensaje": {
    "idTransaccion": "TRX-2024-001",
    "accion": "REVERTIR_CITA",
    "pacienteId": 1
  }
}
```

### Respuesta
- **200 OK**: Compensación iniciada
  ```json
  {
    "estado": "OK",
    "mensaje": "Compensación iniciada en bpm.compensacion.queue"
  }
  ```
- **500 Internal Server Error**: Error al publicar

---

## Colas JMS Configuradas

Las siguientes colas ActiveMQ están disponibles:

1. **bpm.main.queue** - Cola principal de procesos BPM
2. **bpm.notificaciones.queue** - Cola de notificaciones
3. **bpm.error.queue** - Cola de errores
4. **bpm.compensacion.queue** - Cola de compensaciones

---

## Ejemplos de Uso desde Bonita

### Flujo Completo de Creación de Cita

1. **Validar Paciente**
   ```
   GET http://localhost:8080/clinica/api/v1/pacientes/1
   ```

2. **Validar Terapeuta**
   ```
   GET http://localhost:8080/clinica/api/v1/terapeutas/1
   ```

3. **Asignar Recursos**
   ```
   POST http://localhost:8080/clinica/api/v1/bpm/asignar
   Body: {"terapeutaId": 1, "tipoCita": "CONSULTA", "idTransaccion": "TRX-001"}
   ```

4. **Crear Cita**
   ```
   POST http://localhost:8080/clinica/api/v1/citas
   Body: {
     "pacienteId": 1,
     "terapeutaId": 1,
     "fechaCita": "2024-12-20T10:00:00",
     "duracionMinutos": 60,
     "estadoCita": "PROGRAMADA",
     "observaciones": "Consulta inicial"
   }
   ```

5. **Notificar Éxito**
   ```
   POST http://localhost:8080/clinica/api/v1/bpm/notificar
   Body: {
     "cola": "bpm.notificaciones.queue",
     "mensaje": {
       "idTransaccion": "TRX-001",
       "estado": "COMPLETADO",
       "citaId": 15
     }
   }
   ```

### Flujo de Error con Compensación

1. **Validar Paciente** (falla)
   ```
   GET http://localhost:8080/clinica/api/v1/pacientes/999
   Response: 404 Not Found
   ```

2. **Publicar Error**
   ```
   POST http://localhost:8080/clinica/api/v1/bpm/error
   Body: {
     "cola": "bpm.error.queue",
     "mensaje": {
       "idTransaccion": "TRX-002",
       "error": "PACIENTE_NO_ENCONTRADO",
       "detalles": "No existe paciente con ID 999"
     }
   }
   ```

3. **Compensar (si es necesario)**
   ```
   POST http://localhost:8080/clinica/api/v1/bpm/compensar
   Body: {
     "cola": "bpm.compensacion.queue",
     "mensaje": {
       "idTransaccion": "TRX-002",
       "accion": "CANCELAR_PROCESO",
       "motivoCancelacion": "Paciente no encontrado"
     }
   }
   ```

---

## Notas Importantes

1. **Sin Autenticación**: Los endpoints actualmente no requieren autenticación
2. **Formato de Fechas**: Usar formato ISO-8601 (`YYYY-MM-DDTHH:mm:ss`)
3. **IDs Generados**: Al crear una cita, el `idCita` es generado automáticamente
4. **Validación**: Los endpoints GET retornan 404 si el recurso no existe
5. **Mensajes JMS**: El campo `mensaje` en los endpoints de publicación puede contener cualquier estructura JSON
6. **Content-Type**: Siempre usar `application/json` en headers

---

## Configuración de Conectores en Bonita

### REST Connector Configuration

**Base Settings:**
- Protocol: `http`
- Host: `localhost`
- Port: `8080`
- Path Context: `/clinica`

**Request Headers:**
```
Content-Type: application/json
Accept: application/json
```

**Timeout:** 30000 ms (30 segundos)

**Retry:** Activar retry en caso de fallo (máximo 3 intentos)
