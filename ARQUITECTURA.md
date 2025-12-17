# 📐 DOCUMENTO DE ARQUITECTURA

## Sistema de Gestión de Clínica - Backend REST API

---

## 1. VISIÓN GENERAL

### 1.1 Propósito
Sistema backend empresarial para gestión integral de una clínica de rehabilitación que implementa servicios RESTful, SOAP, mensajería JMS y procesos de negocio (BPM).

### 1.2 Alcance
- Gestión de pacientes, terapeutas, citas y servicios clínicos
- Procesos de negocio automatizados con BPM
- Integración de servicios mediante ESB
- Manejo robusto de errores con flujo alterno
- APIs REST y SOAP para diferentes tipos de clientes

---

## 2. ARQUITECTURA DEL SISTEMA

### 2.1 Patrón Arquitectónico: Arquitectura en Capas

```
┌─────────────────────────────────────────────────────────────┐
│                 CAPA DE PRESENTACIÓN                         │
│ ┌─────────────────┐  ┌──────────────┐  ┌────────────────┐  │
│ │ REST Controllers │  │ SOAP Services│  │  Swagger UI    │  │
│ └─────────────────┘  └──────────────┘  └────────────────┘  │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                    CAPA DE NEGOCIO                           │
│ ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│ │   Services   │  │  BPM Service │  │   ESB Service    │   │
│ │   (Impl)     │  │              │  │                  │   │
│ └──────────────┘  └──────────────┘  └──────────────────┘   │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                 CAPA DE INTEGRACIÓN                          │
│ ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│ │     JMS      │  │ Message      │  │    ActiveMQ      │   │
│ │  Consumers   │  │   Queues     │  │                  │   │
│ └──────────────┘  └──────────────┘  └──────────────────┘   │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                  CAPA DE PERSISTENCIA                        │
│ ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│ │ Repositories │  │   Entities   │  │  H2 / MySQL      │   │
│ │     (JPA)    │  │     (JPA)    │  │   Database       │   │
│ └──────────────┘  └──────────────┘  └──────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Principios de Diseño

1. **Separación de Responsabilidades**: Cada capa tiene una responsabilidad específica
2. **Inversión de Dependencias**: Uso de interfaces y inyección de dependencias
3. **Single Responsibility**: Cada clase tiene una única razón para cambiar
4. **Open/Closed**: Abierto para extensión, cerrado para modificación
5. **DRY (Don't Repeat Yourself)**: Código reutilizable

---

## 3. COMPONENTES PRINCIPALES

### 3.1 Capa de Presentación

#### 3.1.1 REST Controllers
- **PacienteController**: Gestión de pacientes
- **TerapeutaController**: Gestión de terapeutas
- **CitaController**: Gestión de citas
- **DisciplinaController**: Gestión de disciplinas
- **EvaluacionController**: Gestión de evaluaciones
- **PagoController**: Gestión de pagos
- **HorarioController**: Gestión de horarios
- **BpmController**: Gestión de procesos BPM
- **HealthController**: Verificación de estado del sistema

#### 3.1.2 SOAP Endpoints
- **PacienteEndpoint**: Servicios SOAP para pacientes
  - ConsultarPaciente
  - ListarPacientes

#### 3.1.3 Documentación
- **Swagger UI**: Documentación interactiva de APIs
- **OpenAPI**: Especificación estándar

### 3.2 Capa de Negocio

#### 3.2.1 Services
Interfaz de servicios con implementaciones:
- PacienteService / PacienteServiceImpl
- TerapeutaService / TerapeutaServiceImpl
- CitaService / CitaServiceImpl
- DisciplinaService / DisciplinaServiceImpl
- EvaluacionService / EvaluacionServiceImpl
- PagoService / PagoServiceImpl
- HorarioService / HorarioServiceImpl

#### 3.2.2 BPM Service
Gestión de procesos de negocio:
- Orquestación de flujos
- Validación de datos
- Asignación de recursos
- Creación de citas
- Manejo de errores

#### 3.2.3 ESB Service
Enterprise Service Bus:
- Enrutamiento de mensajes
- Transformación de datos
- Integración de servicios
- Flujo alterno de errores
- Auditoría

### 3.3 Capa de Integración

#### 3.3.1 JMS Consumers
- **BpmMainConsumer**: Procesa solicitudes principales
- **BpmCompensacionConsumer**: Maneja compensaciones (rollback)
- **BpmErrorConsumer**: Procesa errores críticos
- **BpmNotificacionConsumer**: Envía notificaciones

#### 3.3.2 Message Queues
- bpm.main.queue
- bpm.compensacion.queue
- bpm.error.queue
- bpm.notificaciones.queue

### 3.4 Capa de Persistencia

#### 3.4.1 Entities (JPA)
- PacientesEntity
- TerapeutaEntity
- CitasEntity
- DisciplinaEntity
- EvaluacionesEntity
- PagosEntity
- HorarioEntity

#### 3.4.2 Repositories (Spring Data JPA)
- PacientesRepository
- TerapeutasRepository
- CitasRepository
- DisciplinaRepository
- EvaluacionesRepository
- PagosRepository
- HorarioRepository

---

## 4. FLUJOS DE PROCESO

### 4.1 Flujo BPM Principal

```
Cliente REST
    │
    ▼
BpmController.iniciarProcesoCita()
    │
    ▼
BpmService.iniciarProcesoCita()
    │
    ▼
ESB.routeMessage("CITA_NUEVA")
    │
    ▼
bpm.main.queue
    │
    ▼
BpmMainConsumer.procesarSolicitud()
    │
    ▼
BpmService.procesarSolicitudCita()
    │
    ├─► VALIDANDO → Valida paciente/terapeuta
    │
    ├─► ASIGNANDO → Asigna recursos
    │
    ├─► CONFIRMANDO → Crea cita
    │
    ├─► COMPLETADO → Proceso exitoso
    │
    └─► NOTIFICADO → Envía notificación
```

### 4.2 Flujo Alterno de Error

```
Error en BPM Service
    │
    ▼
ESB.handleAlternateFlow()
    │
    ├─► bpm.error.queue
    │   └─► BpmErrorConsumer
    │       └─► Notifica administradores
    │
    ├─► bpm.compensacion.queue
    │   └─► BpmCompensacionConsumer
    │       └─► Revierte cambios
    │
    └─► bpm.notificaciones.queue
        └─► BpmNotificacionConsumer
            └─► Alerta al sistema
```

### 4.3 Flujo REST Típico

```
Cliente HTTP
    │
    ▼
Controller (ej: PacienteController)
    │
    ├─► Valida request
    │
    ▼
Service (ej: PacienteService)
    │
    ├─► Lógica de negocio
    ├─► Validaciones
    │
    ▼
Repository (ej: PacientesRepository)
    │
    ▼
Base de Datos (H2/MySQL)
    │
    ▼
Response al cliente
```

---

## 5. CONFIGURACIONES

### 5.1 JMS Configuration (JmsConfig.java)
- Definición de colas
- Conversor de mensajes JSON
- Configuración de ActiveMQ

### 5.2 Web Service Configuration (WebServiceConfig.java)
- Registro de MessageDispatcherServlet
- Definición WSDL
- Esquemas XSD

### 5.3 Swagger Configuration (SwaggerConfig.java)
- Documentación API
- Personalización UI

### 5.4 Application Properties
- Base de datos (H2/MySQL)
- Servidor web
- ActiveMQ
- Logging
- JMS queues

---

## 6. MANEJO DE ERRORES

### 6.1 Global Exception Handler
- ResourceNotFoundException → 404
- IllegalArgumentException → 400
- RuntimeException → 500
- Exception → 500

### 6.2 Validaciones
- Datos de entrada en Services
- Existencia de recursos
- Reglas de negocio

### 6.3 Transacciones
- @Transactional en operaciones críticas
- Rollback automático en errores

---

## 7. SEGURIDAD Y VALIDACIÓN

### 7.1 Validación de Datos
- Email formato válido
- Fechas lógicas
- Campos requeridos
- Referencias válidas

### 7.2 Soft Delete
- No se eliminan registros físicamente
- Campo "activo" para desactivación

---

## 8. TECNOLOGÍAS UTILIZADAS

### 8.1 Framework Principal
- **Spring Boot 3.3.6**: Framework base
- **Spring Data JPA**: Persistencia
- **Spring Web**: REST APIs
- **Spring Web Services**: SOAP
- **Spring JMS**: Mensajería

### 8.2 Base de Datos
- **H2**: Desarrollo (en memoria)
- **MySQL**: Producción

### 8.3 Mensajería
- **ActiveMQ**: Message broker

### 8.4 Documentación
- **SpringDoc OpenAPI**: Swagger

### 8.5 Utilidades
- **Lombok**: Reducción de boilerplate
- **ModelMapper**: Mapeo DTO/Entity

---

## 9. PATRONES DE DISEÑO IMPLEMENTADOS

### 9.1 Repository Pattern
Abstracción de la capa de persistencia

### 9.2 Service Layer Pattern
Lógica de negocio separada de controladores

### 9.3 DTO Pattern
Transferencia de datos entre capas

### 9.4 Dependency Injection
Inyección de dependencias vía constructor

### 9.5 Observer Pattern
Listeners de JMS para mensajes

### 9.6 Strategy Pattern
Enrutamiento en ESB según tipo de mensaje

---

## 10. ESCALABILIDAD Y RENDIMIENTO

### 10.1 Estrategias de Escalabilidad
- Arquitectura stateless
- Uso de colas para procesamiento asíncrono
- Separación de responsabilidades

### 10.2 Optimizaciones
- Lazy loading en JPA
- Connection pooling
- Caché (potencial mejora futura)

---

## 11. MEJORAS FUTURAS

### 11.1 Corto Plazo
- [ ] Implementar seguridad (Spring Security)
- [ ] Añadir autenticación JWT
- [ ] Implementar caché con Redis
- [ ] Añadir más pruebas unitarias

### 11.2 Mediano Plazo
- [ ] Microservicios
- [ ] API Gateway
- [ ] Circuit breaker (Resilience4j)
- [ ] Observabilidad (Prometheus, Grafana)

### 11.3 Largo Plazo
- [ ] Event sourcing
- [ ] CQRS
- [ ] Arquitectura hexagonal
- [ ] Kubernetes deployment

---

## 12. CONCLUSIÓN

Este sistema implementa una arquitectura robusta, escalable y mantenible que cumple con:
- ✅ Buenas prácticas de desarrollo
- ✅ Separación de responsabilidades
- ✅ Patrones de diseño estándar
- ✅ Integración de múltiples tecnologías
- ✅ Manejo robusto de errores
- ✅ Documentación completa

La arquitectura permite futuras extensiones y modificaciones sin afectar componentes existentes.
