-- Inserción de nuevos docentes
insert into docente (nombre, apellido, dni, email, telefono, direccion) values
    ('Juan', 'Pérez', '12345678A', 'juanperez@mail.com', '111222333', 'C/ Mayor 1'),
    ('María', 'García', '87654321B', 'mariagarcia@mail.com', '444555666', 'C/ Menor 2'),
    ('Pedro', 'Martínez', '34567890C', 'pedromartinez@mail.com', '777888999', 'C/ Central 3');


-- Inserción de nuevos cursos
insert into curso (nombre, descripcion, estado, start_inscr, end_inscr, plazas, ingresos_estimados, gastos_estimados, docente_id) values
    ('Introducción a la programación en Python', 'Curso de iniciación a la programación en Python', 'En curso', '2023-03-01', '2023-03-31', 20, 500, 200, 1),
    ('Programación avanzada en Python', 'Curso avanzado de programación en Python', 'En fase de inscripción', '2023-04-01', '2023-04-30', 15, 750, 250, 2),
    ('Bases de datos con SQL', 'Curso sobre bases de datos con SQL', 'Cerrado', '2023-05-01', '2023-05-31', 10, 300, 150, 3);


-- Inserción de nuevos alumnos
insert into alumno (nombre, apellido, dni, email, telefono, direccion, birth) values
    ('Juan', 'García', '12345678A', 'juan.garcia@gmail.com', '666555444', 'C/ Mayor, 1', '1990-02-12'),
    ('María', 'Martínez', '87654321B', 'maria.martinez@gmail.com', '666555333', 'C/ Real, 2', '1992-06-25'),
    ('Luis', 'Sánchez', '45678912C', 'luis.sanchez@gmail.com', '666555222', 'C/ Nueva, 3', '1995-09-03'),
    ('Laura', 'Gómez', '12348765D', 'laura.gomez@gmail.com', '666555111', 'C/ La Paz, 4', '1987-11-18'),
    ('Pablo', 'Rodríguez', '54321678E', 'pablo.rodriguez@gmail.com', '666555000', 'C/ del Carmen, 5', '1999-03-30'),
    ('Ana', 'Fernández', '87651234F', 'ana.fernandez@gmail.com', '666555999', 'C/ Naranjos, 6', '2001-07-12');

-- Inscripción de alumnos a cursos
insert into inscripcion (fecha, coste, estado, curso_id, alumno_id) values
    ('2022-05-15', 5000, 'Pagado', 1, 1),
    ('2022-05-15', 5000, 'Pendiente', 1, 2),
    ('2022-05-15', 5000, 'Pendiente', 1, 3),
    ('2022-05-15', 5000, 'Pagado', 2, 4),
    ('2022-05-15', 5000, 'Pendiente', 2, 5),
    ('2022-05-15', 5000, 'Pendiente', 2, 6),
    ('2022-06-01', 2000, 'Pagado', 3, 1),
    ('2022-06-01', 2000, 'Pagado', 3, 3),
    ('2022-06-01', 2000, 'Pagado', 3, 6),
    ('2022-06-01', 2000, 'Pagado', 4, 2),
    ('2022-06-01', 2000, 'Pendiente', 4, 4),
    ('2022-06-01', 2000, 'Pendiente', 4, 5),
    ('2022-06-01', 2000, 'Pendiente', 5, 1),
    ('2022-06-01', 2000, 'Pagado', 5, 2),
    ('2022-06-01', 2000, 'Pendiente', 5, 3);

-- Asignación de docentes a cursos
INSERT INTO docencia (remuneracion, curso_id, docente_id) VALUES
(120, 1, 1), (90, 4, 4), (70, 5, 5),
(110, 3, 6), (100, 2, 5), (80, 1, 4);

-- Asignación de fechas y ubicaciones a cursos
INSERT INTO fecha (fecha, location, curso_id) VALUES
('2022-05-10', 'Aula 2', 1), ('2022-05-17', 'Aula 3', 1),
('2022-06-10', 'Aula 1', 4), ('2022-06-17', 'Aula 1', 4),
('2022-07-01', 'Aula 4', 5), ('2022-07-08', 'Aula 4', 5),
('2022-07-15', 'Aula 2', 3), ('2022-07-22', 'Aula 2', 3),
('2022-08-08', 'Aula 3', 3), ('2022-08-15', 'Aula 1', 3),
('2022-08-22', 'Aula 1', 3), ('2022-08-29', 'Aula 2', 3),
('2022-09-01', 'Aula 1', 1), ('2022-09-08', 'Aula 1', 1),
('2022-09-15', 'Aula 2', 2), ('2022-09-22', 'Aula 2', 2),
('2022-09-29', 'Aula 3', 2);

-- Registro de pagos
INSERT INTO pago (importe, fecha, inscripcion_id) VALUES
(80, '2022-06-05', 1), (80, '2022-07-10', 1),
(90, '2022-08-15', 2), (90, '2022-08-15', 3),
(120, '2022-09-01', 4), (120, '2022-09-01', 5),
(70, '2022-09-15', 6), (70, '2022-09-15', 7),
(70, '2022-09-15', 8), (70, '2022-09-15', 9),
(80, '2022-09-15', 10), (80, '2022-09-15', 11),
(90, '2022-09-15', 12), (90, '2022-09-15', 13),
(100, '2022-09-15', 14), (100, '2022-09-15', 15);
