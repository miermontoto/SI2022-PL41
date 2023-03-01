insert into docente (nombre, apellidos, dni, email, telefono, direccion) values
    ('Juan', 'Pérez', '12345678A', 'juanperez@mail.com', '111222333', 'C/ Mayor 1'),
    ('María', 'García', '87654321B', 'mariagarcia@mail.com', '444555666', 'C/ Menor 2'),
    ('Pedro', 'Martínez', '34567890C', 'pedromartinez@mail.com', '777888999', 'C/ Central 3');

insert into curso (nombre, descripcion, estado, start_inscr, end_inscr, plazas, start, end, docente_id) values
    ('Introducción a la programación en Python', 'Curso de iniciación a la programación en Python', 'Finalizado', '2022-07-01', '2022-07-15', 20, '2022-07-20', '2022-07-25', 1),
    ('Programación avanzada en Python', 'Curso avanzado de programación en Python', 'Finalizado', '2022-08-01', '2022-08-15', 15, '2022-08-20', '2022-08-27', 2),
    ('Bases de datos con SQL', 'Curso sobre bases de datos con SQL', 'Finalizado', '2022-09-01', '2022-09-15', 10, '2022-09-20', '2022-09-23', 3),
    ('Desarrollo web con Django', 'Curso sobre desarrollo web con Django', 'Finalizado', '2021-10-01', '2021-10-15', 25, '2021-10-20', '2021-10-27', 4),
    ('Introducción al machine learning', 'Curso de iniciación al machine learning', 'Finalizado', '2022-03-01', '2022-03-15', 12, '2022-03-20', '2022-03-23', 5),
    ('Programación orientada a objetos', 'Curso avanzado de programación orientada a objetos en Java', 'Finalizado', '2022-06-01', '2022-06-15', 20, '2022-06-20', '2022-06-27', 6),
    ('Ciberseguridad', 'Curso sobre ciberseguridad y hacking ético', 'Finalizado', '2022-02-01', '2022-02-15', 15, '2022-02-20', '2022-02-23', 7),
    ('Desarrollo de videojuegos con Unity', 'Curso sobre desarrollo de videojuegos con Unity', 'Finalizado', '2022-01-01', '2022-01-15', 8, '2022-01-20', '2022-01-23', 9);

insert into alumno (nombre, apellidos, email, telefono) values
    ('Juan', 'García', 'juan.garcia@gmail.com', '666555444'),
    ('María', 'Martínez', 'maria.martinez@gmail.com', '666555333'),
    ('Luis', 'Sánchez', 'luis.sanchez@gmail.com', ''),
    ('Laura', 'Gómez', 'laura.gomez@gmail.com', '666555111'),
    ('Pablo', 'Rodríguez', 'pablo.rodriguez@gmail.com', '666555000'),
    ('Ana', 'Fernández', 'ana.fernandez@gmail.com', '666555999');

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

insert into docencia (remuneracion, curso_id, docente_id) values
(120, 1, 1), (90, 4, 4), (70, 5, 5),
(110, 3, 6), (100, 2, 5), (80, 1, 4);

insert into pago (importe, fecha, inscripcion_id) values
(80, '2022-06-05', 1), (80, '2022-07-10', 1),
(90, '2022-08-15', 2), (90, '2022-08-15', 3),
(120, '2022-09-01', 4), (120, '2022-09-01', 5),
(70, '2022-09-15', 6), (70, '2022-09-15', 7),
(70, '2022-09-15', 8), (70, '2022-09-15', 9),
(80, '2022-09-15', 10), (80, '2022-09-15', 11),
(90, '2022-09-15', 12), (90, '2022-09-15', 13),
(100, '2022-09-15', 14), (100, '2022-09-15', 15);
