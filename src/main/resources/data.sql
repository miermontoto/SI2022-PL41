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

insert into curso (nombre, descripcion, coste, start_inscr, end_inscr, plazas, start, end) values
    ('Desarrollo de aplicaciones móviles con React Native', 'Curso sobre el desarrollo de aplicaciones móviles con React Native', 50.0, '2023-04-01', '2023-04-15', 30, '2023-04-20', '2023-05-20'),
    ('Big Data y análisis de datos con Hadoop', 'Curso avanzado sobre Big Data y análisis de datos con Hadoop', 100.0, '2023-05-01', '2023-05-15', 25, '2023-05-20', '2023-06-20'),
    ('Blockchain y criptomonedas', 'Curso sobre la tecnología blockchain y criptomonedas', 80.0, '2023-01-01', '2023-12-01', 20, '2023-06-20', '2023-07-20'),
    ('Diseño gráfico con Adobe Illustrator', 'Curso sobre diseño gráfico con Adobe Illustrator', 75.0, '2022-11-01', '2022-11-15', 15, '2022-11-20', '2022-11-27'),
    ('Introducción a la ciberseguridad', 'Curso de introducción a la ciberseguridad', 40.0, '2023-08-01', '2023-08-15', 20, '2023-08-20', '2023-09-20'),
    ('Desarrollo de aplicaciones web con Angular', 'Curso sobre el desarrollo de aplicaciones web con Angular', 60.0, '2023-09-01', '2023-09-15', 30, '2023-09-20', '2023-10-20'),
    ('Inteligencia artificial con Python', 'Curso sobre inteligencia artificial con Python', 90.0, '2023-10-01', '2023-10-15', 25, '2023-10-20', '2023-11-20'),
    ('Diseño de interiores', 'Curso sobre diseño de interiores', 50.0, '2023-11-01', '2023-11-15', 15, '2023-11-20', '2023-12-20'),
    ('Marketing digital para emprendedores', 'Curso sobre marketing digital para emprendedores', 70.0, '2023-06-01', '2023-06-15', 20, '2023-06-20', '2023-07-20'),
    ('Inglés para negocios', 'Curso de inglés enfocado en el mundo empresarial', 65.0, '2023-07-01', '2023-07-15', 30, '2023-07-20', '2023-08-20'),
    ('Desarrollo de videojuegos con Unity', 'Curso sobre desarrollo de videojuegos con Unity', 90.0, '2023-08-01', '2023-08-15', 15, '2023-08-20', '2023-09-20'),
    ('Fotografía digital', 'Curso de fotografía digital', 60.0, '2023-09-01', '2023-09-15', 25, '2023-09-20', '2023-10-20'),
    ('Introducción a la programación en Python', 'Curso introductorio a la programación en Python', 50.0, '2023-10-01', '2023-10-15', 20, '2023-10-20', '2023-11-20'),
    ('Diseño de moda', 'Curso sobre diseño de moda', 80.0, '2023-11-01', '2023-11-15', 15, '2023-11-20', '2023-12-20'),
    ('Marketing en redes sociales', 'Curso sobre marketing en redes sociales', 75.0, '2024-01-01', '2024-01-15', 25, '2024-01-20', '2024-02-20'),
    ('Gestión de proyectos', 'Curso sobre gestión de proyectos', 100.0, '2024-02-01', '2024-02-15', 20, '2024-02-20', '2024-03-20');

insert into alumno (nombre, apellidos, email, telefono) values
    ('Juan', 'García Pérez', 'juan.garcia_92@gmail.com', '634527893'),
    ('María', 'Martínez López', 'maria.martinez_87@gmail.com', '672190438'),
    ('Luis', 'Sánchez González', 'luis.sanchez_95@gmail.com', ''),
    ('Laura', 'Gómez García', 'laura.gomez_93@gmail.com', '639012345'),
    ('Pablo', 'Rodríguez Pérez', 'pablo_rodriguez_90@gmail.com', ''),
    ('Ana', 'Fernández García', 'ana_fernandez_91@gmail.com', '689301245'),
    ('Carlos', 'Ruiz Martínez', 'carlos.ruiz_88@gmail.com', '671524963'),
    ('Sofía', 'Hernández Gutiérrez', 'sofia_hernandez_94@gmail.com', '622748910'),
    ('Pedro', 'Gutiérrez Sánchez', 'pedro.gutierrez_89@gmail.com', ''),
    ('Lucía', 'Díaz Martín', 'lucia.diaz_96@gmail.com', '675149236'),
    ('Javier', 'López García', 'javier.lopez_97@gmail.com', '654713289'),
    ('Carmen', 'Santos Rodríguez', 'carmen_santos_98@gmail.com', '610395627'),
    ('José', 'Martínez Gómez', 'jose.martinez_85@gmail.com', '689215743'),
    ('María', 'Fernández Sánchez', 'maria.fernandez_90@gmail.com', '655719872'),
    ('Juan', 'García Pérez', 'juan.garcia_83@gmail.com', '634527893'),
    ('Lucía', 'Rodríguez López', 'lucia.rodriguez_82@gmail.com', ''),
    ('Manuel', 'Santos Martín', 'manuel.santos_87@gmail.com', '610394127'),
    ('Sara', 'González Ruiz', 'sara.gonzalez_88@gmail.com', ''),
    ('Javier', 'Hernández García', 'javier.hernandez_89@gmail.com', '654712398'),
    ('Ana', 'López Rodríguez', 'ana.lopez_84@gmail.com', '689301245'),
    ('Marina', 'Díaz Gutiérrez', 'marina.diaz_86@gmail.com', ''),
    ('Juan', 'Ruiz Martínez', 'juan.ruiz_81@gmail.com', '671524963'),
    ('Sofía', 'García Gutiérrez', 'sofia.garcia_80@gmail.com', '622748910'),
    ('Diego', 'Pérez Sánchez', 'diego.perez_91@gmail.com', '675149236'),
    ('Juan', 'Mier', 'mier@mier.info', '644358985'),
    ('Test', 'Test', 'test@test.com', '');

insert into inscripcion (fecha, curso_id, alumno_id) values
    ('2022-05-15', 7, 1),
    ('2022-05-15', 9, 2),
    ('2022-05-15', 14, 3),
    ('2022-05-15', 6, 4),
    ('2022-05-15', 2, 5),
    ('2022-05-15', 2, 6),
    ('2022-06-01', 3, 9),
    ('2022-06-01', 8, 3),
    ('2022-06-01', 3, 6),
    ('2022-06-01', 4, 2),
    ('2022-06-01', 12, 4),
    ('2022-06-01', 4, 5),
    ('2022-06-01', 7, 7),
    ('2022-06-01', 5, 2),
    ('2022-06-01', 5, 3),
    ('2023-03-04', 13, 1),
    ('2023-03-04', 1, 2),
    ('2023-03-04', 10, 3),
    ('2023-03-04', 2, 4),
    ('2023-03-04', 2, 5),
    ('2023-03-04', 2, 6),
    ('2023-03-05', 7, 7),
    ('2023-03-05', 13, 8),
    ('2023-03-05', 3, 6),
    ('2023-03-06', 11, 9),
    ('2023-03-06', 4, 14),
    ('2023-03-06', 9, 15),
    ('2023-03-06', 5, 11),
    ('2023-03-06', 8, 12),
    ('2023-03-06', 5, 13);

insert into pago (importe, fecha, inscripcion_id) values
    (80, '2022-06-05', 1), (80, '2022-07-10', 1),
    (90, '2022-08-15', 2), (90, '2022-08-15', 3),
    (120, '2022-09-01', 4), (120, '2022-09-01', 5),
    (70, '2022-09-15', 6), (70, '2022-09-15', 7),
    (70, '2022-09-15', 8), (70, '2022-09-15', 9),
    (80, '2022-09-15', 10), (80, '2022-09-15', 11),
    (90, '2022-09-15', 12), (90, '2022-09-15', 13),
    (100, '2022-09-15', 14), (100, '2022-09-15', 15);

insert into docencia (remuneracion, curso_id, docente_id) values
    (120, 1, 1), (90, 4, 4), (70, 5, 5),
    (110, 3, 6), (100, 2, 5), (80, 1, 4);

insert into factura (fecha_introd, fecha_pago, docencia_id) values
    ('2022-09-15', '', 1), ('2022-09-15', '', 2),
    ('2022-09-15', '2022-09-15', 3), ('2022-09-15', '2022-09-15', 4),
    ('2022-09-15', '', 5), ('2022-09-15', '', 6);

insert into evento (fecha, hora_ini, hora_fin, loc, curso_id) values
    ('2023-06-21', '18:00:00', '19:00:00', 'Aula AN-B6', '3'),
    ('2023-06-25', '10:00:00', '11:00:00', 'Aula AN-S4', '3');
