insert into docente (nombre, apellidos, dni, email, telefono, direccion) values
    ('Juan', 'Pérez', '12345678A', 'juanperez@mail.com', '666555442', 'C/ Mayor 1'),
    ('María', 'García', '87654321B', 'mariagarcia@mail.com', '666324897', 'C/ Menor 2'),
    ('Pedro', 'Martínez', '34567890C', 'pedromartinez@mail.com', '654189723', 'C/ Central 3');

insert into curso (nombre, descripcion, estado, coste, start_inscr, end_inscr, plazas, start, end, docente_id) values
    ('Desarrollo de aplicaciones móviles con React Native', 'Curso sobre el desarrollo de aplicaciones móviles con React Native', 'Inscripción abierta', 50.0, '2023-04-01', '2023-04-15', 30, '2023-04-20', '2023-05-20', 10),
    ('Big Data y análisis de datos con Hadoop', 'Curso avanzado sobre Big Data y análisis de datos con Hadoop', 'En desarrollo', 100.0, '2023-05-01', '2023-05-15', 25, '2023-05-20', '2023-06-20', 11),
    ('Blockchain y criptomonedas', 'Curso sobre la tecnología blockchain y criptomonedas', 'Planificado', 80.0, '2023-06-01', '2023-06-15', 20, '2023-06-20', '2023-07-20', 12),
    ('Diseño gráfico con Adobe Illustrator', 'Curso sobre diseño gráfico con Adobe Illustrator', 'Finalizado', 75.0, '2022-11-01', '2022-11-15', 15, '2022-11-20', '2022-11-27', 13),
    ('Introducción a la ciberseguridad', 'Curso de introducción a la ciberseguridad', 'Planificado', 40.0, '2023-08-01', '2023-08-15', 20, '2023-08-20', '2023-09-20', 14),
    ('Desarrollo de aplicaciones web con Angular', 'Curso sobre el desarrollo de aplicaciones web con Angular', 'Inscripción abierta', 60.0, '2023-09-01', '2023-09-15', 30, '2023-09-20', '2023-10-20', 15),
    ('Inteligencia artificial con Python', 'Curso sobre inteligencia artificial con Python', 'En desarrollo', 90.0, '2023-10-01', '2023-10-15', 25, '2023-10-20', '2023-11-20', 16),
    ('Diseño de interiores', 'Curso sobre diseño de interiores', 'Planificado', 50.0, '2023-11-01', '2023-11-15', 15, '2023-11-20', '2023-12-20', 17);

insert into alumno (nombre, apellidos, email, telefono) values
    ('Juan', 'García', 'juan.garcia@gmail.com', '666555444'),
    ('María', 'Martínez', 'maria.martinez@gmail.com', '666555333'),
    ('Luis', 'Sánchez', 'luis.sanchez@gmail.com', ''),
    ('Laura', 'Gómez', 'laura.gomez@gmail.com', '666555111'),
    ('Pablo', 'Rodríguez', 'pablo.rodriguez@gmail.com', '666555000'),
    ('Ana', 'Fernández', 'ana.fernandez@gmail.com', '666555999'),
    ('Juan', 'Mier', 'mier@mier.info', '644358905'),
    ('Test', 'Test', 'test@test.com', '');

insert into inscripcion (fecha, estado, curso_id, alumno_id) values
    ('2022-05-15', 'Pagado', 1, 1),
    ('2022-05-15', 'Pendiente', 1, 2),
    ('2022-05-15', 'Pendiente', 1, 3),
    ('2022-05-15', 'Pagado', 2, 4),
    ('2022-05-15', 'Pendiente', 2, 5),
    ('2022-05-15', 'Pendiente', 2, 6),
    ('2022-06-01', 'Pagado', 3, 1),
    ('2022-06-01', 'Pagado', 3, 3),
    ('2022-06-01', 'Pagado', 3, 6),
    ('2022-06-01', 'Pagado', 4, 2),
    ('2022-06-01', 'Pendiente', 4, 4),
    ('2022-06-01', 'Pendiente', 4, 5),
    ('2022-06-01', 'Pendiente', 5, 1),
    ('2022-06-01', 'Pagado', 5, 2),
    ('2022-06-01', 'Pendiente', 5, 3);

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
