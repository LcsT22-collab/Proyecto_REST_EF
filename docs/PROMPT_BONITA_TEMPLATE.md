# PROMPT PARA IA - PROCESO BONITA (VERSIÓN SIMPLE)

Copia y pega este prompt en otra IA (ChatGPT, Claude, etc.) cambiando los datos del DTO:

---

## 📋 PROMPT PARA COPIAR:

```
Actúa como experto en Bonita BPM y Spring Boot.

Necesito crear un proceso BPMN SIMPLE en Bonita Community 2024.3 para gestionar [NOMBRE_ENTIDAD] en mi sistema de clínica. Este proceso debe integrarse con mi aplicación Spring Boot existente.

IMPORTANTE: Solo quiero el flujo básico, SIN manejo de errores, SIN notificaciones JMS, SIN compensación. Solo validación y creación.

---

MI DTO EXACTO (NO cambiar nombres de campos):

[PEGAR AQUÍ LOS CAMPOS DEL DTO]

Ejemplo para EvaluacionDTO:
- idEvaluaciones (Long)
- pacienteId (Long)
- nombrePaciente (String)
- fechaEvaluacion (LocalDate)
- tipoEvaluacion (String)
- resultado (String)
- recomendaciones (String)

---

ENDPOINTS REST EXISTENTES:

Base URL: http://localhost:8080/clinica

- GET  /api/v1/[entidad]/{id} → Obtener por ID (200 si existe, 404 si no)
- POST /api/v1/[entidad] → Crear (200 OK con DTO completo)

Ejemplo para evaluaciones:
- GET  /api/v1/evaluaciones/{id}
- POST /api/v1/evaluaciones

---

VALIDACIÓN REQUERIDA:

[ESPECIFICAR QUÉ VALIDAR]

Ejemplos:
- Para Evaluacion: Validar que el pacienteId exista antes de crear
- Para Disciplina: Validar que el código no esté duplicado
- Para Horario: Validar que el terapeutaId exista
- Para Pago: Validar que el pacienteId exista

---

PROCESO BONITA QUE NECESITO:

Nombre: "Gestión de [ENTIDAD]"

Flujo básico SIMPLE:
1. Start Event (formulario con campos del DTO)
2. Service Task "Validar [CONDICIÓN]" → GET para validar
3. Gateway "¿[CONDICIÓN] válida?"
   - SÍ → Service Task "Crear [ENTIDAD]" → End Exitoso
   - NO → End Error

Variables de proceso (nombres exactos según DTO):
[LISTAR CAMPOS DEL DTO + variables de control]

---

ENTREGABLES QUE NECESITO:

Dame instrucciones PASO A PASO para:
1. Crear el proceso en Bonita Studio
2. Configurar variables de proceso
3. Diseñar el diagrama (Start → Validar → Gateway → Crear → End)
4. Configurar Service Task de validación (GET)
5. Configurar Gateway con condiciones
6. Configurar Service Task de creación (POST)
7. Configurar formulario de inicio con todos los campos
8. Casos de prueba (exitoso y error)

FORMATO: Usa pasos numerados, tablas, y código cuando sea necesario. Sé específico con nombres de variables, rutas REST, y configuraciones de conectores.

NO incluyas: Manejo de errores, boundary events, notificaciones JMS, compensación, ni logging. Solo el flujo básico funcional.
```

---

## 🔄 CÓMO USAR ESTE TEMPLATE:

### Para cada DTO diferente, reemplaza:

1. **[NOMBRE_ENTIDAD]**: `Evaluaciones`, `Horarios`, `Pagos`, etc.
2. **[PEGAR AQUÍ LOS CAMPOS DEL DTO]**: Lista completa de campos
3. **[entidad]**: `evaluaciones`, `horarios`, `pagos` (minúsculas)
4. **[ESPECIFICAR QUÉ VALIDAR]**: Qué endpoint GET usar para validar
5. **[CONDICIÓN]**: `Paciente existe`, `Código disponible`, etc.

---

## 📝 EJEMPLOS DE USO:

### Ejemplo 1: HorarioDTO

```
Actúa como experto en Bonita BPM y Spring Boot.

Necesito crear un proceso BPMN SIMPLE en Bonita Community 2024.3 para gestionar HORARIOS en mi sistema de clínica.

MI DTO EXACTO:
- idHorario (Long)
- terapeutaId (Long)
- nombreTerapeuta (String)
- diaSemana (String)
- horaInicio (LocalTime)
- horaFin (LocalTime)
- activo (Boolean)

ENDPOINTS:
- GET  /api/v1/terapeutas/{terapeutaId} → Validar terapeuta existe
- POST /api/v1/horarios → Crear horario

VALIDACIÓN: Validar que terapeutaId exista antes de crear horario

FLUJO:
Start → Validar Terapeuta (GET) → Gateway → Crear Horario (POST) → End
```

### Ejemplo 2: PagoDTO

```
Actúa como experto en Bonita BPM y Spring Boot.

Necesito crear un proceso BPMN SIMPLE en Bonita Community 2024.3 para gestionar PAGOS en mi sistema de clínica.

MI DTO EXACTO:
- idPago (Long)
- pacienteId (Long)
- nombrePaciente (String)
- monto (BigDecimal)
- fechaPago (LocalDate)
- estadoPago (String)
- metodoPago (String)
- referencia (String)
- observaciones (String)

ENDPOINTS:
- GET  /api/v1/pacientes/{pacienteId} → Validar paciente existe
- POST /api/v1/pagos → Crear pago

VALIDACIÓN: Validar que pacienteId exista antes de crear pago

FLUJO:
Start → Validar Paciente (GET) → Gateway → Crear Pago (POST) → End
```

---

## ✅ VENTAJAS DE USAR ESTE PROMPT:

1. ✅ Genera instrucciones paso a paso detalladas
2. ✅ Incluye configuración exacta de conectores REST
3. ✅ Mantiene consistencia entre procesos
4. ✅ Evita complejidad innecesaria (sin errores/notificaciones)
5. ✅ Respeta nombres exactos de campos del DTO
6. ✅ Incluye casos de prueba

---

## 🎯 DTOs PENDIENTES QUE PUEDES CREAR:

Usa este template para:

- [ ] HorarioDTO
- [ ] PagoDTO
- [ ] TerapeutaDTO (validar disciplinaId existe)
- [ ] PacienteDTO (crear directo, sin validación previa)

---

## 💡 TIPS:

1. **Copia el prompt completo** incluyendo "Actúa como experto..."
2. **Reemplaza TODOS los placeholders** [NOMBRE_ENTIDAD], etc.
3. **Pega en ChatGPT, Claude, o cualquier IA**
4. **Sigue las instrucciones generadas** paso a paso en Bonita
5. **Prueba con casos exitosos y de error**

---

¡Listo! Con este template puedes generar procesos Bonita para TODOS tus DTOs de forma consistente.
