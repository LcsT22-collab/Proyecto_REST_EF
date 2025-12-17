-- Insertar datos de prueba para Pacientes
INSERT INTO pacientes (nombre, fecha_nacimiento, nombre_tutor, telefono, correo, direccion, fecha_registro, activo)
VALUES 
('Juan Pérez Gómez', '2010-05-15', 'María Gómez', '555-0101', 'maria.gomez@email.com', 'Av. Principal 123', CURRENT_DATE, true),
('Ana María López', '2012-08-22', 'Carlos López', '555-0102', 'carlos.lopez@email.com', 'Calle Secundaria 456', CURRENT_DATE, true),
('Pedro Rodríguez', '2008-03-10', 'Laura Rodríguez', '555-0103', 'laura.rodriguez@email.com', 'Plaza Central 789', CURRENT_DATE, true),
('Sofia Martínez', '2015-11-30', 'Roberto Martínez', '555-0104', 'roberto.martinez@email.com', 'Av. Norte 321', CURRENT_DATE, true);

-- Insertar datos de prueba para Disciplinas
INSERT INTO disciplina (nombre, descripcion, estado_disciplina, fecha_registro)
VALUES 
('Fisioterapia', 'Tratamiento de lesiones y rehabilitación física', 'ACTIVA', CURRENT_DATE),
('Terapia Ocupacional', 'Desarrollo de habilidades para actividades cotidianas', 'ACTIVA', CURRENT_DATE),
('Psicología', 'Atención psicológica y emocional', 'ACTIVA', CURRENT_DATE),
('Logopedia', 'Tratamiento de trastornos del habla y lenguaje', 'ACTIVA', CURRENT_DATE);

-- Insertar datos de prueba para Terapeutas
INSERT INTO terapeuta (nombre, disciplina_id, telefono, correo, disponibilidad, fecha_registro, activo)
VALUES 
('Dra. María López', 1, '555-0201', 'maria.lopez@clinica.com', 'DISPONIBLE', CURRENT_DATE, true),
('Dr. José García', 2, '555-0202', 'jose.garcia@clinica.com', 'DISPONIBLE', CURRENT_DATE, true),
('Dra. Carmen Ruiz', 3, '555-0203', 'carmen.ruiz@clinica.com', 'DISPONIBLE', CURRENT_DATE, true),
('Dr. Antonio Sánchez', 4, '555-0204', 'antonio.sanchez@clinica.com', 'DISPONIBLE', CURRENT_DATE, true);

-- Insertar datos de prueba para Horarios
INSERT INTO horario (terapeuta_id, dia_semana, hora_inicio, hora_fin, activo)
VALUES 
(1, 'LUNES', '08:00:00', '14:00:00', true),
(1, 'MIERCOLES', '08:00:00', '14:00:00', true),
(2, 'MARTES', '09:00:00', '15:00:00', true),
(2, 'JUEVES', '09:00:00', '15:00:00', true),
(3, 'LUNES', '10:00:00', '16:00:00', true),
(3, 'VIERNES', '10:00:00', '16:00:00', true),
(4, 'MIERCOLES', '08:00:00', '12:00:00', true),
(4, 'VIERNES', '08:00:00', '12:00:00', true);
