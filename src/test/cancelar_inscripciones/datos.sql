INSERT INTO inscripcion (fecha, cancelada, curso_id, alumno_id, coste_id) VALUES
('2023-05-08', '0', '1', '1', '1'),
('2023-04-22', '1', '2', '2', '2'),
('2023-04-23', '0', '2', '3', '2');

INSERT INTO curso (nombre, descripcion, start_inscr, end_inscr, plazas, start, end, estado, entidad_id, importe) VALUES
('Curso con estado EN_INSCRIPCION', 'Test', '2023-05-07', '2023-05-13', '5', '2023-05-15', '2023-05-20', '', '', ''),
('Curso con estado EN_CURSO', 'Test', '2023-04-20', '2023-04-25', '5', '2023-05-08', '2023-05-15', '', '', '');

INSERT INTO alumno (nombre, apellidos, email, telefono) VALUES
('Pedro', 'Benito', 'pedrobenito@gmail.com', '123456789'),
('María', 'Magdalena', 'mariamagdalena@gmail.com', '987654321'),
('Ahmad', 'Chacur', 'ahmadchacur@gmail.com', '789456123');

INSERT INTO coste (coste, curso_id, colectivo_id) VALUES
('80', '1', '1'),
('75', '3', '2');

INSERT INTO colectivo (nombre) VALUES
('Por defecto 1'),
('Por defecto 2');