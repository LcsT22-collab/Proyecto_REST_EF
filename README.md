# 🏥 Sistema de Gestión de Clínica - Backend REST API

## 📝 Descripción del Proyecto

Sistema completo de gestión para una clínica de rehabilitación que implementa:
- **7 Servicios RESTful** completos con CRUD
- **Flujo BPM (Business Process Management)** para gestión de procesos de negocio
- **Flujo Alterno de Excepción** con manejo robusto de errores
- **ESB (Enterprise Service Bus)** para integración de servicios
- **Mensajería JMS** con ActiveMQ
- **Servicios Web SOAP** para integración legacy
- **Arquitectura en capas** (Controller, Service, Repository)

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE PRESENTACIÓN                      │
│  REST Controllers + SOAP Endpoints + Swagger UI              │
└──────────────────┬──────────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────────┐
│                    CAPA DE NEGOCIO                           │
│  Services + BPM Service + ESB Service                        │
└──────────────────┬──────────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────────┐
│                 CAPA DE INTEGRACIÓN                          │
│  JMS Consumers + Message Queues + ActiveMQ                   │
└──────────────────┬──────────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────────┐
│                  CAPA DE PERSISTENCIA                        │
│  JPA Repositories + Entities + H2/MySQL Database             │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 Servicios RESTful Implementados

### 1. **Pacientes** (`/api/v1/pacientes`)
- Gestión completa de pacientes
- Validación de datos
- Soft delete (desactivación)

### 2. **Terapeutas** (`/api/v1/terapeutas`)
- Gestión de terapeutas
- Control de disponibilidad
- Asignación por disciplina

### 3. **Citas** (`/api/v1/citas`)
- Programación de citas
- Estados: PROGRAMADA, COMPLETADA, CANCELADA
- Validación de disponibilidad

### 4. **Disciplinas** (`/api/v1/disciplinas`)
- Catálogo de disciplinas terapéuticas
- Estados activas/inactivas

### 5. **Evaluaciones** (`/api/v1/evaluaciones`)
- Registro de evaluaciones de pacientes
- Seguimiento de progreso
- Recomendaciones

### 6. **Pagos** (`/api/v1/pagos`)
- Gestión de pagos
- Métodos: EFECTIVO, TARJETA, TRANSFERENCIA
- Estados: PENDIENTE, PAGADO, CANCELADO

### 7. **Horarios** (`/api/v1/horarios`)
- Gestión de horarios de terapeutas
- Disponibilidad por día de la semana

---

## 🔄 Flujo BPM (Business Process Management)

### Endpoint: `/api/v1/bpm/cita`

### Flujo Principal:
```
1. SOLICITADO      → Usuario inicia proceso
2. VALIDANDO       → Valida paciente y terapeuta
3. VALIDADO        → Datos correctos
4. ASIGNANDO       → Asigna recursos
5. ASIGNADO        → Recursos confirmados
6. CONFIRMANDO     → Crea la cita
7. COMPLETADO      → Proceso exitoso
8. NOTIFICADO      → Notificación enviada
```

### Flujo Alterno (Manejo de Excepciones):
```
ERROR DETECTADO
    ↓
ESB ACTIVA FLUJO ALTERNO
    ↓
┌───────────────────────────────┐
│ 1. Cola de Errores            │ → Registro del error
├───────────────────────────────┤
│ 2. Cola de Compensación       │ → Rollback de cambios
├───────────────────────────────┤
│ 3. Cola de Notificaciones     │ → Alerta a administradores
└───────────────────────────────┘
    ↓
PROCESO COMPENSADO
```

---

## 🚌 ESB (Enterprise Service Bus)

El ESB actúa como intermediario centralizado que:
- **Enruta mensajes** a las colas apropiadas
- **Transforma datos** entre diferentes formatos
- **Maneja el flujo alterno** cuando ocurren errores
- **Audita** todas las transacciones

### Métodos principales:
- `routeMessage()` - Enrutamiento inteligente
- `handleAlternateFlow()` - Gestión de errores
- `transformMessage()` - Transformación de datos

---

## 🧼 Servicios Web SOAP

### WSDL: `http://localhost:8080/clinica/ws/pacientes.wsdl`

Operaciones disponibles:
- **ConsultarPaciente** - Obtener datos de un paciente
- **ListarPacientes** - Listar todos los pacientes

---

## 📨 Mensajería JMS con ActiveMQ

### Colas Configuradas:

| Cola | Propósito |
|------|-----------|
| `bpm.main.queue` | Procesamiento principal BPM |
| `bpm.compensacion.queue` | Compensación de transacciones |
| `bpm.notificaciones.queue` | Envío de notificaciones |
| `bpm.error.queue` | Manejo de errores críticos |

---

## 🛠️ Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.3.6**
- **Spring Data JPA**
- **Spring Web Services (SOAP)**
- **ActiveMQ (JMS)**
- **H2 Database** (desarrollo)
- **MySQL** (producción)
- **Lombok**
- **SpringDoc OpenAPI** (Swagger)
- **Maven**

---

## 🚀 Instalación y Ejecución

### Prerrequisitos:
- Java 21 o superior
- Maven 3.6+
- ActiveMQ (opcional, usa broker embebido)

### 1. Clonar el repositorio
```bash
git clone <tu-repo>
cd Proyecto_REST_EF
```

### 2. Compilar el proyecto
```bash
./mvnw clean install
```

### 3. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

### 4. Acceder a la aplicación
- **API REST**: http://localhost:8080/clinica/api/v1
- **Swagger UI**: http://localhost:8080/clinica/swagger-ui.html
- **H2 Console**: http://localhost:8080/clinica/h2-console
- **SOAP WSDL**: http://localhost:8080/clinica/ws/pacientes.wsdl

---

## 📊 Estructura del Proyecto

```
src/main/java/com/clinica/
├── config/                 # Configuraciones
│   ├── JmsConfig.java     # Configuración JMS
│   ├── SwaggerConfig.java # Configuración Swagger
│   └── WebServiceConfig.java # Configuración SOAP
├── controller/            # Controladores REST
│   ├── bpm/
│   │   └── BpmController.java
│   ├── PacienteController.java
│   ├── TerapeutaController.java
│   ├── CitaController.java
│   └── ...
├── service/               # Servicios de negocio
│   ├── impl/             # Implementaciones
│   └── bpm/
│       └── BpmService.java
├── esb/                   # Enterprise Service Bus
│   └── EsbService.java
├── jms/                   # Consumidores JMS
│   └── consumer/
│       ├── BpmMainConsumer.java
│       ├── BpmCompensacionConsumer.java
│       ├── BpmErrorConsumer.java
│       └── BpmNotificacionConsumer.java
├── soap/                  # Endpoints SOAP
│   └── PacienteEndpoint.java
├── model/
│   ├── entity/           # Entidades JPA
│   ├── dto/              # Data Transfer Objects
│   ├── enums/            # Enumeraciones
│   └── repository/       # Repositorios JPA
└── exception/            # Manejo de excepciones
    ├── GlobalExceptionHandler.java
    └── ResourceNotFoundException.java
```

---

## 🧪 Pruebas

Ver el archivo [TESTING_GUIDE.md](TESTING_GUIDE.md) para:
- Ejemplos de requests REST
- Pruebas de flujo BPM
- Pruebas SOAP
- Validación de colas JMS
- Scripts de prueba

---

## 📈 Mejoras Implementadas

✅ **Actualización de dependencias** a versiones estables  
✅ **Manejo global de excepciones** con respuestas HTTP apropiadas  
✅ **Validación de datos** en servicios  
✅ **Soft delete** en lugar de eliminación física  
✅ **Logging completo** en todos los niveles  
✅ **Documentación Swagger** automática  
✅ **Datos de prueba** pre-cargados  
✅ **ESB para integración** de servicios  
✅ **Flujo alterno robusto** para errores  
✅ **Servicios SOAP** para integración legacy  

---

## 📋 Buenas Prácticas Implementadas

- ✅ Separación clara de capas (MVC)
- ✅ Inyección de dependencias
- ✅ DTOs para transferencia de datos
- ✅ Manejo centralizado de excepciones
- ✅ Validación de datos de entrada
- ✅ Transacciones con @Transactional
- ✅ Logging estructurado
- ✅ Configuración externalizada
- ✅ Código limpio y documentado

---

## 🎓 Evaluación Académica

Este proyecto cumple con todos los requisitos para evaluación:

| Requisito | Estado |
|-----------|--------|
| 7 Servicios RESTful | ✅ Completado |
| BPM Principal | ✅ Completado |
| Flujo Alterno de Excepción | ✅ Completado |
| ESB | ✅ Completado |
| JMS (Colas de mensajes) | ✅ Completado |
| SOAP | ✅ Completado |
| REST | ✅ Completado |
| Sin errores de compilación | ✅ Completado |
| Buenas prácticas | ✅ Completado |

---

## 👨‍💻 Autor

Sistema desarrollado para evaluación académica - Curso de Arquitectura Empresarial

---

## 📄 Licencia

Este proyecto es de uso académico.
