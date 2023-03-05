-- Generar una esquema de la base de datos para la aplicación.
-- Se utiliza SQlite como motor de la base de datos.

drop table if exists curso;
drop table if exists inscripcion;
drop table if exists inscripcioncancelada;
drop table if exists alumno;
drop table if exists docente;
drop table if exists docencia;
drop table if exists clase;
drop table if exists pago;

create table curso (
    id integer primary key autoincrement,
    nombre text not null,
    descripcion text,
    coste float not null,
    start_inscr date not null,
    end_inscr date not null,
    plazas integer not null,
    start date not null,
    end date not null,
    localizacion text not null,
    docente_id integer not null,
    foreign key (docente_id) references docente(id)
);

create table inscripcion (
    id integer primary key autoincrement,
    fecha date not null,
    curso_id integer not null,
    alumno_id integer not null,
    foreign key (curso_id) references curso(id),
    foreign key (alumno_id) references alumno(id)
);

create table inscripcioncancelada (
	id integer primary key autoincrement,
	importedevuelto integer not null,
	fechacancelacion date not null,
	inscripcion_id integer not null,
	foreign key (inscripcion_id) references inscripcion(id)
);

create table alumno (
    id integer primary key autoincrement,
    nombre text not null,
    apellidos text not null,
    email text not null,
    telefono integer
);

create table docente (
    id integer primary key autoincrement,
    nombre text not null,
    apellidos text not null,
    dni text not null,
    email text not null,
    telefono text not null,
    direccion text not null
);

create table docencia (
    id integer primary key autoincrement,
    remuneracion integer not null,
    curso_id integer not null,
    docente_id integer not null,
    foreign key (curso_id) references curso(id),
    foreign key (docente_id) references docente(id)
);

create table pago (
    id integer primary key autoincrement,
    importe integer not null,
    fecha date not null,
    inscripcion_id integer not null,
    foreign key (inscripcion_id) references inscripcion(id)
);
