# Script que genera nuevos datos de prueba
# para la base de datos. Depende de la
# fecha de ejecución.

require_relative 'classes.rb'
require_relative 'util.rb'
require_relative 'generate.rb'
require 'faker'
Faker::Config.locale = 'es'
rng = Random.new

puts "Generando datos con fecha #{Time.now}"

# Generar alumnos
## Sin mayor relevancia para casos específicos,
## simplemente se necesita generar un número
## arbitrario de alumnos para introducir en
## cursos.
alumnos = generate_alumnos(248)

# Generar cursos
## Se necesitan los siguientes cursos:
### Curso ya finalizado
### Curso en progreso
### Curso con inscripción cerrada
### Curso con inscripción abierta
### Curso esperando a inscripción
### Curso con una plaza
## El estado de los cursos depende de la
## fecha, por lo que se generan dependiendo
## de la fecha de ejecución del script.
cursos = []
date = Time.now.to_date
year = date.year
month = date.month

start_this_month = Date.new(year, month, 1)
end_this_month = Date.new(year, month, -1)
start_next_month = Date.new(year, month + 1, 1)
end_next_month = Date.new(year, month + 1, -1)
start_last_month = Date.new(year, month - 1, 1)
end_last_month = Date.new(year, month - 1, -1)
start_two_months_ago = Date.new(year, month - 2, 1)
end_two_months_ago = Date.new(year, month - 2, -1)


cursos.push(Curso.new('[G] Curso ya finalizado', 'Curso generado que ya ha finalizado',
    rng.rand(1..500), start_two_months_ago, end_two_months_ago, rng.rand(1..alumnos.length),
    start_last_month, end_last_month))
cursos.push(Curso.new('[G] Curso en progreso', 'Curso generado que está en progreso',
    rng.rand(1..500), start_last_month, end_last_month, rng.rand(1..alumnos.length),
    start_this_month, end_this_month))
cursos.push(Curso.new('[G] Curso con inscripción cerrada', 'Curso generado que ya ha cerrado la inscripción pero no ha empezado',
    rng.rand(1..500), start_last_month, end_last_month, rng.rand(1..alumnos.length),
    start_next_month, end_next_month))
cursos.push(Curso.new('[G] Curso con inscripción abierta', 'Curso generado que está abierto a inscripciones',
    rng.rand(1..500), start_this_month, end_this_month, rng.rand(1..alumnos.length),
    start_next_month, end_next_month))
cursos.push(Curso.new('[G] Curso esperando a inscripción', 'Curso generado que está esperando a que se abra la inscripción',
    rng.rand(1..500), start_next_month, end_next_month, rng.rand(1..alumnos.length),
    start_two_months_ago, end_two_months_ago))
cursos.push(Curso.new('[G] Curso con una plaza', 'Curso generado que tiene una plaza',
    rng.rand(1..500), start_this_month, end_this_month, 1,
    start_next_month, end_next_month))


# Generar docentes
## No se necesitan casos específicos.
docentes = generate_docentes(50)

# Generar eventos para los cursos
## Sin mayor relevancia para casos específicos.
## Se genera como mínimo un evento por curso.
aulas = ['AN-B3', 'AN-B6', 'AN-B1', 'AN-E', 'AN-B', 'AS-1', 'AN-P3', 'DO-1.S.31', 'DO-9', 'AN-S6', 'AN-C']
eventos = []

cursos.each.with_index do |c, i|
    eventos += generate_eventos(rng.rand(1..5), aulas, c, i)
end

# Generar docencias
## Se necesita al menos una docencia por curso.
## No importa qué docentes imparten qué cursos.
## Las remuneraciones se generan aleatoriamente.
docencias = []

cursos.length.times do |i|
    docencias += generate_docencias(rng.rand(1..6), docentes, i)
end

# Generar inscripciones
## Se necesita una inscripción cancelada por curso.
## Como el estado de las inscripciones depende de los pagos,
## se generarán pagos para obtener varios estados de inscripción.
inscripciones = []

cursos.each.with_index do |c, i|
    inscripciones += generate_inscripciones(rng.rand(1..c.plazas), alumnos, c, i)
end

# Generar facturas
## SOLO DEL CURSO COMPLETADO se generan facturas
## DE TODOS los docentes.
facturas = []
cursos.filter { |c| c.end < Time.now.to_date }.each.with_index do |c, i|
    num_docencias = docencias.filter { |d| d.curso_id == i }.length
    facturas += generate_facturas_docencias(rng.rand(num_docencias/2..num_docencias), docencias, c, i)
end

## Generar un curso con MUCHOS eventos, docencias e inscripciones.
cursos.push(Curso.new('[G] Curso grande', 'Curso generado con muchos eventos, inscripciones y docencias',
    rng.rand(1..500), start_last_month, end_last_month, alumnos.length, start_this_month, end_this_month))
eventos += generate_eventos(rng.rand(200..500), aulas, cursos.last, cursos.length - 1)
docencias += generate_docencias(rng.rand(docentes.length/2..docentes.length), docentes, cursos.length - 1)
inscripciones += generate_inscripciones(rng.rand(2*cursos.last.plazas/3..cursos.last.plazas), alumnos, cursos.last, cursos.length - 1)

alumnos.push(Alumno.new('Juan', 'Mier', 'mier@mier.info', ''))
alumnos.push(Alumno.new('Test', 'Test', 'test@mier.info', ''))

# Generar pagos
## Se generan una porción de pagos para todas las inscripciones, de forma
## que algunas estarán pagadas, otras sin pagar y otras con pagos demasiado grandes.
pagos = generate_pagos(inscripciones.length/2, inscripciones, cursos, "PAGADO")
pagos += generate_pagos(inscripciones.length/10, inscripciones, cursos, "EXCESO")

# Generar archivo SQL a partir de los datos generados
sql = "-- generated by main.rb on #{Time.now}\n-- DO NOT EDIT: changes will be replaced.\n\n"
tables = {
    'alumno' => alumnos,
    'curso' => cursos,
    'docente' => docentes,
    'docencia' => docencias,
    'evento' => eventos,
    'inscripcion' => inscripciones,
    'factura' => facturas,
    'pago' => pagos
}
tables.each do |t|
    sql += add_data_to_sql(t[1], t[0])
end

# Guardar el archivo SQL a 'data.sql'
File.open('src/main/resources/data.sql', 'w') do |f|
    f.write(sql)
end
