-- Generar una esquema de la base de datos para la aplicación.
-- Se utiliza SQlite como motor de la base de datos.

drop table if exists curso;
drop table if exists inscripcion;
drop table if exists alumno;
drop table if exists docente;
drop table if exists docencia;
drop table if exists pago;
drop table if exists factura;
drop table if exists evento;
drop table if exists entidad;
drop table if exists coste;
drop table if exists colectivo;
drop table if exists lista_espera;

create table coste (
	id integer primary key autoincrement,
	coste float not null,
	curso_id integer not null,
    colectivo_id integer not null,
	foreign key (curso_id) references curso(id),
    foreign key (colectivo_id) references colectivo(id)
);

create table colectivo (
    id integer primary key autoincrement,
    nombre text not null
);

create table entidad (
	id integer primary key autoincrement,
    nombre text not null,
    email text,
    telefono text
);

create table curso (
    id integer primary key autoincrement,
    nombre text not null,
    descripcion text,
    start_inscr date not null,
    end_inscr date not null,
    plazas integer not null,
    start date not null,
    end date not null,
    estado text,
    entidad_id integer,
    foreign key (entidad_id) references entidad(id)
);

create table inscripcion (
    id integer primary key autoincrement,
    fecha date not null,
    cancelada boolean not null default false,
    curso_id integer not null,
    alumno_id integer not null,
    coste_id integer not null,
    entidad_id integer,
    foreign key (coste_id) references coste(id),
    foreign key (curso_id) references curso(id),
    foreign key (alumno_id) references alumno(id),
    foreign key (entidad_id) references entidad(id)
);

create table alumno (
    id integer primary key autoincrement,
    nombre text not null,
    apellidos text not null,
    email text not null,
    telefono integer
);

create table lista_espera (
	id integer not null primary key autoincrement,
	inscripcion_id integer not null,
	fecha_entrada date not null,
	en_cola boolean not null default true,
	foreign key (inscripcion_id) references inscripcion(id)
);

create table docente (
    id integer primary key autoincrement,
    nombre text not null,
    apellidos text not null,
    dni text not null,
    email text not null,
    telefono text not null,
    direccion text not null,
    entidad_id integer,
    foreign key (entidad_id) references entidad(id)
);

create table docencia (
    id integer primary key autoincrement,
    remuneracion float not null,
    curso_id integer not null,
    docente_id integer not null,
    foreign key (curso_id) references curso(id),
    foreign key (docente_id) references docente(id)
);


create table pago (
    id integer primary key autoincrement,
    importe float not null,
    fecha date not null,
    inscripcion_id integer,
    factura_id integer,
    foreign key (inscripcion_id) references inscripcion(id),
    foreign key (factura_id) references factura(id)
);

create table factura (
    id integer primary key autoincrement,
    fecha date not null,
    docencia_id integer not null,
    foreign key (docencia_id) references docencia(id)
);

create table evento (
    id integer primary key autoincrement,
    fecha date not null,
    hora_ini time not null,
    hora_fin time not null,
    loc text not null,
    observaciones text,
    curso_id integer not null,
    foreign key (curso_id) references curso(id)
);
