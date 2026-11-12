-- =====================================================================
-- DATOS DE PRUEBA – Punto Marcial (Fase 8)
-- Se ejecuta en cada arranque del backend (spring.sql.init.mode=always),
-- después de que Hibernate crea las tablas.
-- INSERT IGNORE: si el dato ya existe no se duplica ni da error.
-- Los ids de prueba empiezan en 1001 para no chocar con datos registrados a mano.
-- El administrador NO está aquí: lo crea DataInitializer (admin@puntomarcial.com / Admin123).
-- Contraseñas: clientes -> Cliente123, instructores -> Instructor123
-- Para volver al estado inicial: borrar la base academia_punto_marcial y arrancar de nuevo.
-- =====================================================================

-- ---------- CLIENTES (herencia JOINED: usuario + cliente) ----------
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1001, 'María', 'Flores', 'DNI', '45781236', 'maria.flores@example.com', '987654321', '$2a$10$evM96JR19vKhZlEovBF0ZuCWqeewyItpz1xPz8Cq17nTk0GQybHPO', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1002, 'Carlos', 'Mendoza', 'DNI', '71234589', 'carlos.mendoza@example.com', '912345678', '$2a$10$evM96JR19vKhZlEovBF0ZuCWqeewyItpz1xPz8Cq17nTk0GQybHPO', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1003, 'Lucía', 'Huamán', 'DNI', '70456812', 'lucia.huaman@example.com', '956781234', '$2a$10$evM96JR19vKhZlEovBF0ZuCWqeewyItpz1xPz8Cq17nTk0GQybHPO', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1004, 'Jorge', 'Castillo', 'DNI', '46892317', 'jorge.castillo@example.com', '934567812', '$2a$10$evM96JR19vKhZlEovBF0ZuCWqeewyItpz1xPz8Cq17nTk0GQybHPO', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1005, 'Valeria', 'Rojas', 'DNI', '72589134', 'valeria.rojas@example.com', '945612378', '$2a$10$evM96JR19vKhZlEovBF0ZuCWqeewyItpz1xPz8Cq17nTk0GQybHPO', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1006, 'Renzo', 'Gutiérrez', 'DNI', '73145826', 'renzo.gutierrez@example.com', '967812345', '$2a$10$evM96JR19vKhZlEovBF0ZuCWqeewyItpz1xPz8Cq17nTk0GQybHPO', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1007, 'Camila', 'Chávez', 'DNI', '74321568', 'camila.chavez@example.com', '978123456', '$2a$10$evM96JR19vKhZlEovBF0ZuCWqeewyItpz1xPz8Cq17nTk0GQybHPO', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1008, 'Andrés', 'Salinas', 'CE', '001478523', 'andres.salinas@example.com', '923456781', '$2a$10$evM96JR19vKhZlEovBF0ZuCWqeewyItpz1xPz8Cq17nTk0GQybHPO', 'ACTIVO');
INSERT IGNORE INTO cliente (id_usuario, estado_membresia, fecha_vencimiento) VALUES (1001, 'VIGENTE', CURDATE() + INTERVAL 120 DAY);
INSERT IGNORE INTO cliente (id_usuario, estado_membresia, fecha_vencimiento) VALUES (1002, 'VIGENTE', CURDATE() + INTERVAL 60 DAY);
INSERT IGNORE INTO cliente (id_usuario, estado_membresia, fecha_vencimiento) VALUES (1003, 'VIGENTE', CURDATE() + INTERVAL 90 DAY);
INSERT IGNORE INTO cliente (id_usuario, estado_membresia, fecha_vencimiento) VALUES (1004, 'VIGENTE', CURDATE() + INTERVAL 30 DAY);
INSERT IGNORE INTO cliente (id_usuario, estado_membresia, fecha_vencimiento) VALUES (1005, 'VIGENTE', CURDATE() + INTERVAL 200 DAY);
INSERT IGNORE INTO cliente (id_usuario, estado_membresia, fecha_vencimiento) VALUES (1006, 'VIGENTE', CURDATE() + INTERVAL 45 DAY);
INSERT IGNORE INTO cliente (id_usuario, estado_membresia, fecha_vencimiento) VALUES (1007, 'VENCIDA', CURDATE() + INTERVAL -15 DAY);
INSERT IGNORE INTO cliente (id_usuario, estado_membresia, fecha_vencimiento) VALUES (1008, 'VIGENTE', CURDATE() + INTERVAL 75 DAY);

-- ---------- INSTRUCTORES (2 por disciplina; herencia JOINED: usuario + instructor) ----------
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1101, 'Diego', 'Ramírez', 'DNI', '41235678', 'diego.ramirez@puntomarcial.com', '911223344', '$2a$10$.XQKcuy1BGVNpRewyW/6nuopH90Ngg6lb0XxnKJZMxlcYWJy8Bq0K', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1102, 'Carla', 'Vega', 'DNI', '42345671', 'carla.vega@puntomarcial.com', '922334455', '$2a$10$.XQKcuy1BGVNpRewyW/6nuopH90Ngg6lb0XxnKJZMxlcYWJy8Bq0K', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1103, 'Luis', 'Paredes', 'DNI', '43456712', 'luis.paredes@puntomarcial.com', '933445566', '$2a$10$.XQKcuy1BGVNpRewyW/6nuopH90Ngg6lb0XxnKJZMxlcYWJy8Bq0K', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1104, 'Andrea', 'Salazar', 'DNI', '44567123', 'andrea.salazar@puntomarcial.com', '944556677', '$2a$10$.XQKcuy1BGVNpRewyW/6nuopH90Ngg6lb0XxnKJZMxlcYWJy8Bq0K', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1105, 'Marco', 'Quispe', 'DNI', '45671234', 'marco.quispe@puntomarcial.com', '955667788', '$2a$10$.XQKcuy1BGVNpRewyW/6nuopH90Ngg6lb0XxnKJZMxlcYWJy8Bq0K', 'ACTIVO');
INSERT IGNORE INTO usuario (id_usuario, nombre, apellido, tipo_doc, num_doc, correo, telefono, password, estado) VALUES (1106, 'Sofía', 'Torres', 'DNI', '46712345', 'sofia.torres@puntomarcial.com', '966778899', '$2a$10$.XQKcuy1BGVNpRewyW/6nuopH90Ngg6lb0XxnKJZMxlcYWJy8Bq0K', 'ACTIVO');
INSERT IGNORE INTO instructor (id_usuario, disciplina) VALUES (1101, 'MMA');
INSERT IGNORE INTO instructor (id_usuario, disciplina) VALUES (1102, 'MMA');
INSERT IGNORE INTO instructor (id_usuario, disciplina) VALUES (1103, 'MUAY_THAI');
INSERT IGNORE INTO instructor (id_usuario, disciplina) VALUES (1104, 'MUAY_THAI');
INSERT IGNORE INTO instructor (id_usuario, disciplina) VALUES (1105, 'BOX');
INSERT IGNORE INTO instructor (id_usuario, disciplina) VALUES (1106, 'BOX');

-- ---------- CLASES (salas A-D) ----------
-- Los horarios se calculan desde la fecha de hoy, así siempre son clases de esta semana.
-- 1001: Empieza en 1 hora: la cancelación se RECHAZA (menos de 2 h)
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1001, 'BOX', 1105, 'D', DATE_FORMAT(NOW() + INTERVAL 1 HOUR, '%Y-%m-%d %H:%i:00'), DATE_FORMAT(NOW() + INTERVAL 1 HOUR, '%Y-%m-%d %H:%i:00') + INTERVAL 60 MINUTE, 10, 8);
-- 1002: Mañana: la cancelación se ACEPTA (más de 2 h)
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1002, 'MUAY_THAI', 1103, 'A', TIMESTAMP(CURDATE() + INTERVAL 1 DAY, '19:00:00'), TIMESTAMP(CURDATE() + INTERVAL 1 DAY, '19:00:00') + INTERVAL 60 MINUTE, 12, 9);
-- 1003: Aforo 1: un solo cupo
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1003, 'MMA', 1101, 'B', TIMESTAMP(CURDATE() + INTERVAL 1 DAY, '20:00:00'), TIMESTAMP(CURDATE() + INTERVAL 1 DAY, '20:00:00') + INTERVAL 60 MINUTE, 1, 1);
-- 1004: Aforo lleno: sin cupos
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1004, 'BOX', 1106, 'C', TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '18:00:00'), TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '18:00:00') + INTERVAL 60 MINUTE, 3, 0);
-- 1005: Normal
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1005, 'MMA', 1102, 'A', TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '07:00:00'), TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '07:00:00') + INTERVAL 60 MINUTE, 15, 14);
-- 1006: Normal
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1006, 'MUAY_THAI', 1104, 'B', TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '19:00:00'), TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '19:00:00') + INTERVAL 60 MINUTE, 20, 18);
-- 1007: Normal
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1007, 'BOX', 1105, 'A', TIMESTAMP(CURDATE() + INTERVAL 3 DAY, '18:00:00'), TIMESTAMP(CURDATE() + INTERVAL 3 DAY, '18:00:00') + INTERVAL 60 MINUTE, 12, 11);
-- 1008: Normal
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1008, 'MMA', 1101, 'C', TIMESTAMP(CURDATE() + INTERVAL 3 DAY, '20:00:00'), TIMESTAMP(CURDATE() + INTERVAL 3 DAY, '20:00:00') + INTERVAL 60 MINUTE, 10, 10);
-- 1009: Normal
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1009, 'MUAY_THAI', 1103, 'A', TIMESTAMP(CURDATE() + INTERVAL 4 DAY, '07:00:00'), TIMESTAMP(CURDATE() + INTERVAL 4 DAY, '07:00:00') + INTERVAL 60 MINUTE, 15, 15);
-- 1010: Normal
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1010, 'BOX', 1106, 'B', TIMESTAMP(CURDATE() + INTERVAL 4 DAY, '19:00:00'), TIMESTAMP(CURDATE() + INTERVAL 4 DAY, '19:00:00') + INTERVAL 60 MINUTE, 12, 11);
-- 1011: Normal
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1011, 'MMA', 1102, 'D', TIMESTAMP(CURDATE() + INTERVAL 5 DAY, '10:00:00'), TIMESTAMP(CURDATE() + INTERVAL 5 DAY, '10:00:00') + INTERVAL 60 MINUTE, 20, 20);
-- 1012: Normal (90 min)
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1012, 'MUAY_THAI', 1104, 'C', TIMESTAMP(CURDATE() + INTERVAL 5 DAY, '11:30:00'), TIMESTAMP(CURDATE() + INTERVAL 5 DAY, '11:30:00') + INTERVAL 90 MINUTE, 25, 23);
-- 1013: Aforo máximo permitido (30)
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1013, 'BOX', 1105, 'A', TIMESTAMP(CURDATE() + INTERVAL 6 DAY, '09:00:00'), TIMESTAMP(CURDATE() + INTERVAL 6 DAY, '09:00:00') + INTERVAL 60 MINUTE, 30, 30);
-- 1014: Normal
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1014, 'MMA', 1101, 'B', TIMESTAMP(CURDATE() + INTERVAL 6 DAY, '17:00:00'), TIMESTAMP(CURDATE() + INTERVAL 6 DAY, '17:00:00') + INTERVAL 60 MINUTE, 8, 7);
-- 1015: EN CURSO (empezó hace 30 min): el instructor puede tomar asistencia; el cliente ya no puede reservar
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1015, 'MMA', 1101, 'C', DATE_FORMAT(NOW() - INTERVAL 30 MINUTE, '%Y-%m-%d %H:%i:00'), DATE_FORMAT(NOW() - INTERVAL 30 MINUTE, '%Y-%m-%d %H:%i:00') + INTERVAL 60 MINUTE, 12, 9);
-- 1016: Pasada: asistencia tomada
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1016, 'MMA', 1101, 'B', TIMESTAMP(CURDATE() + INTERVAL -1 DAY, '20:00:00'), TIMESTAMP(CURDATE() + INTERVAL -1 DAY, '20:00:00') + INTERVAL 60 MINUTE, 10, 7);
-- 1017: Pasada: asistencia tomada
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1017, 'MUAY_THAI', 1103, 'A', TIMESTAMP(CURDATE() + INTERVAL -2 DAY, '19:00:00'), TIMESTAMP(CURDATE() + INTERVAL -2 DAY, '19:00:00') + INTERVAL 60 MINUTE, 12, 10);
-- 1018: Pasada: asistencia tomada
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1018, 'BOX', 1105, 'A', TIMESTAMP(CURDATE() + INTERVAL -3 DAY, '18:00:00'), TIMESTAMP(CURDATE() + INTERVAL -3 DAY, '18:00:00') + INTERVAL 60 MINUTE, 12, 9);
-- 1019: Pasada: asistencia tomada
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1019, 'MMA', 1102, 'D', TIMESTAMP(CURDATE() + INTERVAL -4 DAY, '10:00:00'), TIMESTAMP(CURDATE() + INTERVAL -4 DAY, '10:00:00') + INTERVAL 60 MINUTE, 20, 19);
-- 1020: Pasada: asistencia tomada
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1020, 'MUAY_THAI', 1104, 'C', TIMESTAMP(CURDATE() + INTERVAL -5 DAY, '11:30:00'), TIMESTAMP(CURDATE() + INTERVAL -5 DAY, '11:30:00') + INTERVAL 90 MINUTE, 25, 23);
-- 1021: Pasada: asistencia tomada
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1021, 'BOX', 1106, 'B', TIMESTAMP(CURDATE() + INTERVAL -6 DAY, '19:00:00'), TIMESTAMP(CURDATE() + INTERVAL -6 DAY, '19:00:00') + INTERVAL 60 MINUTE, 12, 11);
-- 1022: Pasada: asistencia tomada
INSERT IGNORE INTO clase (id_clase, disciplina, id_instructor, sala, horario_inicio, horario_fin, aforo_maximo, aforo_disponible) VALUES (1022, 'MMA', 1101, 'C', TIMESTAMP(CURDATE() + INTERVAL -7 DAY, '20:00:00'), TIMESTAMP(CURDATE() + INTERVAL -7 DAY, '20:00:00') + INTERVAL 60 MINUTE, 10, 8);

-- En cada arranque se vuelven a calcular los horarios de las clases de prueba
-- (si no, la clase "en menos de 2 horas" quedaría en el pasado al día siguiente).
UPDATE clase SET horario_inicio = DATE_FORMAT(NOW() + INTERVAL 1 HOUR, '%Y-%m-%d %H:%i:00'), horario_fin = DATE_FORMAT(NOW() + INTERVAL 1 HOUR, '%Y-%m-%d %H:%i:00') + INTERVAL 60 MINUTE WHERE id_clase = 1001;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 1 DAY, '19:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 1 DAY, '19:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1002;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 1 DAY, '20:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 1 DAY, '20:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1003;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '18:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '18:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1004;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '07:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '07:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1005;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '19:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 2 DAY, '19:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1006;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 3 DAY, '18:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 3 DAY, '18:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1007;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 3 DAY, '20:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 3 DAY, '20:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1008;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 4 DAY, '07:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 4 DAY, '07:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1009;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 4 DAY, '19:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 4 DAY, '19:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1010;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 5 DAY, '10:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 5 DAY, '10:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1011;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 5 DAY, '11:30:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 5 DAY, '11:30:00') + INTERVAL 90 MINUTE WHERE id_clase = 1012;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 6 DAY, '09:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 6 DAY, '09:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1013;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL 6 DAY, '17:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL 6 DAY, '17:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1014;
UPDATE clase SET horario_inicio = DATE_FORMAT(NOW() - INTERVAL 30 MINUTE, '%Y-%m-%d %H:%i:00'), horario_fin = DATE_FORMAT(NOW() - INTERVAL 30 MINUTE, '%Y-%m-%d %H:%i:00') + INTERVAL 60 MINUTE WHERE id_clase = 1015;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL -1 DAY, '20:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL -1 DAY, '20:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1016;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL -2 DAY, '19:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL -2 DAY, '19:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1017;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL -3 DAY, '18:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL -3 DAY, '18:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1018;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL -4 DAY, '10:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL -4 DAY, '10:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1019;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL -5 DAY, '11:30:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL -5 DAY, '11:30:00') + INTERVAL 90 MINUTE WHERE id_clase = 1020;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL -6 DAY, '19:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL -6 DAY, '19:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1021;
UPDATE clase SET horario_inicio = TIMESTAMP(CURDATE() + INTERVAL -7 DAY, '20:00:00'), horario_fin = TIMESTAMP(CURDATE() + INTERVAL -7 DAY, '20:00:00') + INTERVAL 60 MINUTE WHERE id_clase = 1022;

-- ---------- RESERVAS (coinciden con el aforo disponible de cada clase) ----------
-- asistencia: PENDIENTE, ASISTIO o NO_ASISTIO (la toma el instructor)
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1001, 1001, 1001, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1002, 1002, 1001, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1003, 1001, 1002, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1004, 1004, 1002, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1005, 1005, 1002, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1006, 1002, 1004, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1007, 1003, 1004, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1008, 1004, 1004, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1009, 1006, 1005, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1010, 1005, 1006, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1011, 1007, 1006, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1012, 1001, 1007, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1013, 1008, 1010, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1014, 1003, 1012, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1015, 1006, 1012, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1016, 1002, 1014, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1017, 1001, 1015, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1018, 1005, 1015, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1019, 1006, 1015, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'PENDIENTE');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1020, 1002, 1016, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1021, 1003, 1016, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'NO_ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1022, 1004, 1016, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1023, 1001, 1017, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1024, 1005, 1017, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1025, 1002, 1018, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1026, 1008, 1018, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'NO_ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1027, 1006, 1018, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1028, 1007, 1019, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1029, 1003, 1020, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1030, 1001, 1020, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1031, 1004, 1021, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'NO_ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1032, 1001, 1022, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'ASISTIO');
INSERT IGNORE INTO reserva (id_reserva, id_cliente, id_clase, fecha_reserva, estado, asistencia) VALUES (1033, 1002, 1022, DATE_FORMAT(NOW(), '%Y-%m-%d'), 'ACTIVA', 'ASISTIO');

-- ---------- PRODUCTOS (stock normal, igual al mínimo, bajo el mínimo y 0) ----------
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1001, 'Guantes de MMA', 'EQUIPAMIENTO', 'MMA', '4 oz', 129.90, 15, 4, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1002, 'Rashguard manga larga', 'ROPA', 'MMA', 'M', 89.90, 10, 3, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1003, 'Rashguard manga larga', 'ROPA', 'MMA', 'L', 89.90, 3, 3, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1004, 'Short de MMA', 'ROPA', 'MMA', 'M', 79.90, 0, 2, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1005, 'Guantes de Muay Thai', 'EQUIPAMIENTO', 'MUAY_THAI', '12 oz', 189.90, 8, 3, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1006, 'Guantes de Muay Thai', 'EQUIPAMIENTO', 'MUAY_THAI', '16 oz', 199.90, 2, 3, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1007, 'Espinilleras', 'PROTECCION', 'MUAY_THAI', 'M', 159.90, 6, 2, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1008, 'Short de Muay Thai', 'ROPA', 'MUAY_THAI', 'L', 69.90, 12, 3, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1009, 'Guantes de box', 'EQUIPAMIENTO', 'BOX', '12 oz', 179.90, 10, 3, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1010, 'Guantes de box', 'EQUIPAMIENTO', 'BOX', '16 oz', 189.90, 4, 4, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1011, 'Casco de box', 'PROTECCION', 'BOX', 'Única', 149.90, 5, 2, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1012, 'Vendas de algodón', 'ACCESORIOS', 'GENERAL', '4.5 m', 19.90, 40, 10, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1013, 'Protector bucal', 'PROTECCION', 'GENERAL', 'Única', 24.90, 25, 5, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1014, 'Cuerda para saltar', 'ACCESORIOS', 'GENERAL', 'Única', 34.90, 0, 3, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1015, 'Proteína whey 2 lb', 'SUPLEMENTOS', 'GENERAL', '2 lb', 159.00, 7, 2, 'ACTIVO');
INSERT IGNORE INTO producto (id_producto, nombre, categoria, disciplina, talla, precio, stock, stock_minimo, estado) VALUES (1016, 'Botella deportiva', 'ACCESORIOS', 'GENERAL', '750 ml', 29.90, 18, 4, 'ACTIVO');

-- ---------- VENTAS (pagadas y rechazadas) Y SU DETALLE ----------
INSERT IGNORE INTO venta (id_venta, id_cliente, fecha, total, estado, ultimos4) VALUES (1001, 1001, TIMESTAMP(CURDATE() - INTERVAL 30 DAY, '18:30:00'), 219.70, 'PAGADA', '4242');
INSERT IGNORE INTO venta (id_venta, id_cliente, fecha, total, estado, ultimos4) VALUES (1002, 1001, TIMESTAMP(CURDATE() - INTERVAL 21 DAY, '18:30:00'), 89.90, 'PAGADA', '4242');
INSERT IGNORE INTO venta (id_venta, id_cliente, fecha, total, estado, ultimos4) VALUES (1003, 1001, TIMESTAMP(CURDATE() - INTERVAL 14 DAY, '18:30:00'), 159.00, 'RECHAZADA', NULL);
INSERT IGNORE INTO venta (id_venta, id_cliente, fecha, total, estado, ultimos4) VALUES (1004, 1001, TIMESTAMP(CURDATE() - INTERVAL 7 DAY, '18:30:00'), 54.80, 'PAGADA', '1881');
INSERT IGNORE INTO venta (id_venta, id_cliente, fecha, total, estado, ultimos4) VALUES (1005, 1001, TIMESTAMP(CURDATE() - INTERVAL 2 DAY, '18:30:00'), 349.80, 'PAGADA', '4242');
INSERT IGNORE INTO venta (id_venta, id_cliente, fecha, total, estado, ultimos4) VALUES (1006, 1002, TIMESTAMP(CURDATE() - INTERVAL 10 DAY, '18:30:00'), 209.80, 'PAGADA', '0005');
INSERT IGNORE INTO venta (id_venta, id_cliente, fecha, total, estado, ultimos4) VALUES (1007, 1002, TIMESTAMP(CURDATE() - INTERVAL 3 DAY, '18:30:00'), 149.90, 'RECHAZADA', NULL);
INSERT IGNORE INTO venta (id_venta, id_cliente, fecha, total, estado, ultimos4) VALUES (1008, 1003, TIMESTAMP(CURDATE() - INTERVAL 5 DAY, '18:30:00'), 159.70, 'PAGADA', '3333');
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1001, 1001, 1009, 1, 179.90, 179.90);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1002, 1001, 1012, 2, 19.90, 39.80);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1003, 1002, 1002, 1, 89.90, 89.90);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1004, 1003, 1015, 1, 159.00, 159.00);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1005, 1004, 1013, 1, 24.90, 24.90);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1006, 1004, 1016, 1, 29.90, 29.90);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1007, 1005, 1005, 1, 189.90, 189.90);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1008, 1005, 1007, 1, 159.90, 159.90);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1009, 1006, 1001, 1, 129.90, 129.90);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1010, 1006, 1004, 1, 79.90, 79.90);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1011, 1007, 1011, 1, 149.90, 149.90);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1012, 1008, 1008, 2, 69.90, 139.80);
INSERT IGNORE INTO detalle_venta (id_detalle, id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1013, 1008, 1012, 1, 19.90, 19.90);
