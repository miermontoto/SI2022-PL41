-- Inserción de nuevos docentes
INSERT INTO docente (nombre, apellido) VALUES
('Ana', 'Martinez'), ('Pablo', 'Garcia'), ('Maria', 'Lopez');

-- Inserción de nuevos cursos
INSERT INTO curso (nombre, estado, start_inscr, end_inscr, plazas, docente_id) VALUES
('Marketing Digital', 'Activo', '2022-05-01', '2022-05-31', 15, 4),
('Diseño Gráfico', 'Cancelado', '2022-06-01', '2022-06-30', 10, 5),
('Programación Java', 'Activo', '2022-07-01', '2022-07-31', 20, 6);

-- Inserción de nuevos alumnos
INSERT INTO alumno (nombre, apellido, dni, email, telefono, direccion, birth) VALUES
('Elena', 'Fernandez', '45678901D', 'elena@gmail.com', '666777888', 'Calle Mayor 5', '1997-12-15'),
('Miguel', 'Sanchez', '56789012E', 'miguel@hotmail.com', '612345789', 'Calle Ancha 2', '1993-08-30'),
('Sofia', 'Gomez', '67890123F', 'sofia@yahoo.com', '600123456', 'Calle Estrecha 3', '1995-05-12'),
('Carlos', 'Rodriguez', '78901234G', 'carlos@outlook.com', '687654321', 'Calle Larga 4', '1991-11-02');

-- Inscripción de alumnos a cursos
INSERT INTO inscripcion (curso_id, alumno_id) VALUES
(1, 3), (1, 4), (2, 4), (4, 1), (5, 2),
(3, 3), (3, 4), (3, 1), (3, 2), (1, 1),
(2, 2), (2, 3), (2, 1), (1, 2), (1, 4);

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
