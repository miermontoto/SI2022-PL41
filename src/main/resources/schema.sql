-- Generar una esquema de la base de datos para la aplicación.
-- Se utiliza SQlite como motor de la base de datos.

drop table if exists curso;
drop table if exists inscripcion;
drop table if exists alumno;
drop table if exists docente;
drop table if exists docencia;
drop table if exists fecha;
drop table if exists pago;

create table fecha (
    fecha date not null,
    location text not null,
    foreign key (curso_id) references curso(id),
    primary key (fecha, curso_id)
)

create table curso (
    id integer primary key autoincrement,
    nombre text not null,
    descripcion text,
    estado text not null,
    start_inscr date,
    end_inscr date,
    plazas integer,
    ingresos_estimados integer,
    gastos_estimados integer,
    foreign key (docente_id) references docente(id)
);

create table inscripcion (
    id integer primary key autoincrement,
    fecha date not null,
    coste integer not null,
    estado text not null,
    foreign key (curso_id) references curso(id),
    foreign key (alumno_id) references alumno(id)
);

create table alumno (
    id integer primary key autoincrement,
    nombre text not null,
    apellido text not null,
    dni text not null,
    email text not null,
    telefono text not null,
    direccion text not null,
    birth date not null,
);

create table docente (
    id integer primary key autoincrement,
    nombre text not null,
    apellido text not null,
    dni text not null,
    email text not null,
    telefono text not null,
    direccion text not null,
);

create table docencia (
    id integer primary key autoincrement,
    remuneracion integer not null,
    foreign key (curso_id) references curso(id),
    foreign key (docente_id) references docente(id),
);

create table pago (
    id integer primary key autoincrement,
    importe integer not null,
    fecha date not null,
    foreign key (inscripcion_id) references inscripcion(id)
);
