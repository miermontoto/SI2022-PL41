insert into docente (nombre, apellidos, dni, email, telefono, direccion) values
    ('Elena', 'García Gómez', '12345678A', 'elena.garcia@gmail.com', '666555442', 'C/ Alcalá 21'),
    ('Roberto', 'Fernández Sánchez', '87654321B', 'roberto.fernandez@hotmail.com', '666324897', 'C/ Gran Vía 7'),
    ('Lucía', 'Martín Pérez', '34567890C', 'lucia.martin@outlook.com', '654189723', 'C/ Princesa 5'),
    ('Sergio', 'González Ruiz', '98765432D', 'sergio.gonzalez@gmail.com', '628483937', 'C/ Velázquez 12'),
    ('Isabel', 'Rodríguez Torres', '56789012E', 'isabel.rodriguez@hotmail.com', '675849234', 'C/ Goya 9'),
    ('Antonio', 'Hernández Castro', '23456789F', 'antonio.hernandez@gmail.com', '672846391', 'C/ Santa Engracia 14'),
    ('Carmen', 'Díaz Navarro', '34567890G', 'carmen.diaz@yahoo.es', '687245639', 'C/ Atocha 37'),
    ('Pablo', 'Sánchez Romero', '45678901H', 'pablo.sanchez@gmail.com', '673845639', 'C/ Toledo 26'),
    ('María', 'Jiménez García', '56789012I', 'maria.jimenez@hotmail.com', '678492736', 'C/ Hortaleza 8'),
    ('David', 'Gutiérrez Sánchez', '67890123J', 'david.gutierrez@yahoo.com', '675849234', 'C/ Mayor 25');

insert into curso (nombre, descripcion, coste, start_inscr, end_inscr, plazas, start, end, localizacion, docente_id) values
    ('Desarrollo de aplicaciones móviles con React Native', 'Curso sobre el desarrollo de aplicaciones móviles con React Native', 50.0, '2023-04-01', '2023-04-15', 30, '2023-04-20', '2023-05-20', 'Aula 1', 10),
    ('Big Data y análisis de datos con Hadoop', 'Curso avanzado sobre Big Data y análisis de datos con Hadoop', 100.0, '2023-05-01', '2023-05-15', 25, '2023-05-20', '2023-06-20', 'Aula 2', 11),
    ('Blockchain y criptomonedas', 'Curso sobre la tecnología blockchain y criptomonedas', 80.0, '2023-06-01', '2023-06-15', 20, '2023-06-20', '2023-07-20', 'Aula 3', 12),
    ('Diseño gráfico con Adobe Illustrator', 'Curso sobre diseño gráfico con Adobe Illustrator', 75.0, '2022-11-01', '2022-11-15', 15, '2022-11-20', '2022-11-27', 'Aula 4', 13),
    ('Introducción a la ciberseguridad', 'Curso de introducción a la ciberseguridad', 40.0, '2023-08-01', '2023-08-15', 20, '2023-08-20', '2023-09-20', 'Aula 5', 14),
    ('Desarrollo de aplicaciones web con Angular', 'Curso sobre el desarrollo de aplicaciones web con Angular', 60.0, '2023-09-01', '2023-09-15', 30, '2023-09-20', '2023-10-20', 'Aula 6', 15),
    ('Inteligencia artificial con Python', 'Curso sobre inteligencia artificial con Python', 90.0, '2023-10-01', '2023-10-15', 25, '2023-10-20', '2023-11-20', 'Aula 7', 16),
    ('Diseño de interiores', 'Curso sobre diseño de interiores', 50.0, '2023-11-01', '2023-11-15', 15, '2023-11-20', '2023-12-20', 'Aula 8', 17),
    ('Marketing digital para emprendedores', 'Curso sobre marketing digital para emprendedores', 70.0, '2023-06-01', '2023-06-15', 20, '2023-06-20', '2023-07-20', 'Aula 1', 18),
    ('Inglés para negocios', 'Curso de inglés enfocado en el mundo empresarial', 65.0, '2023-07-01', '2023-07-15', 30, '2023-07-20', '2023-08-20', 'Aula 2', 19),
    ('Desarrollo de videojuegos con Unity', 'Curso sobre desarrollo de videojuegos con Unity', 90.0, '2023-08-01', '2023-08-15', 15, '2023-08-20', '2023-09-20', 'Aula 3', 20),
    ('Fotografía digital', 'Curso de fotografía digital', 60.0, '2023-09-01', '2023-09-15', 25, '2023-09-20', '2023-10-20', 'Aula 4', 21),
    ('Introducción a la programación en Python', 'Curso introductorio a la programación en Python', 50.0, '2023-10-01', '2023-10-15', 20, '2023-10-20', '2023-11-20', 'Aula 5', 22),
    ('Diseño de moda', 'Curso sobre diseño de moda', 80.0, '2023-11-01', '2023-11-15', 15, '2023-11-20', '2023-12-20', 'Aula 6', 23),
    ('Marketing en redes sociales', 'Curso sobre marketing en redes sociales', 75.0, '2024-01-01', '2024-01-15', 25, '2024-01-20', '2024-02-20', 'Aula 7', 24),
    ('Gestión de proyectos', 'Curso sobre gestión de proyectos', 100.0, '2024-02-01', '2024-02-15', 20, '2024-02-20', '2024-03-20', 'Aula 8', 25);


insert into alumno (nombre, apellidos, email, telefono) values
    ('Juan', 'García', 'juan.garcia@gmail.com', '666555444'),
    ('María', 'Martínez', 'maria.martinez@gmail.com', '666555333'),
    ('Luis', 'Sánchez', 'luis.sanchez@gmail.com', ''),
    ('Laura', 'Gómez', 'laura.gomez@gmail.com', '666555111'),
    ('Pablo', 'Rodríguez', 'pablo.rodriguez@gmail.com', '666555000'),
    ('Ana', 'Fernández', 'ana.fernandez@gmail.com', '666555999'),
    ('Juan', 'Mier', 'mier@mier.info', '644358985'),
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
