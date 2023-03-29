-- Generar una esquema de la base de datos para la aplicación.
-- Se utiliza SQlite como motor de la base de datos.

drop table if exists curso;
drop table if exists inscripcion;
drop table if exists inscripcioncancelada;
drop table if exists alumno;
drop table if exists docente;
drop table if exists docencia;
drop table if exists pago;
drop table if exists factura;
drop table if exists evento;

create table curso (
    id integer primary key autoincrement,
    nombre text not null,
    descripcion text,
    coste float not null,
    start_inscr date not null,
    end_inscr date not null,
    plazas integer not null,
    start date not null,
    end date not null
);

create table inscripcion (
    id integer primary key autoincrement,
    fecha date not null,
    cancelada boolean not null default false,
    curso_id integer not null,
    alumno_id integer not null,
    foreign key (curso_id) references curso(id),
    foreign key (alumno_id) references alumno(id)
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
    importe float not null,
    fecha date not null,
    inscripcion_id integer,
    factura_id integer,
    foreign key (inscripcion_id) references inscripcion(id),
    foreign key (factura_id) references factura(id)
);

create table factura (
    id integer primary key autoincrement,
    fecha_introd date not null,
    fecha_pago date,
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
