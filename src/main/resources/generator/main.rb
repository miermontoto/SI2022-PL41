# Script que genera nuevos datos de prueba
# para la base de datos. Depende de la
# fecha de ejecución.

require_relative 'classes.rb'
require_relative 'util.rb'
require_relative 'generate.rb'
require 'faker'
Faker::Config.locale = 'es'

def obtain_data(ratio)
    # Generar alumnos
    ## Sin mayor relevancia para casos específicos,
    ## simplemente se necesita generar un número
    ## arbitrario de alumnos para introducir en
    ## cursos.
    alumnos = generate_alumnos((248 * ratio).to_i)

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

    # Generar entidades
    ## Sin mayor relevancia para casos específicos.
    entidades = generate_entidades((30 * ratio).to_i)

    cursos.push(Curso.new('[G] Curso ya finalizado', 'Curso generado que ya ha finalizado',
        start_two_months_ago, end_two_months_ago, rand(1..alumnos.length),
        start_last_month, end_last_month))
    cursos.push(Curso.new('[G] Curso en progreso', 'Curso generado que está en progreso',
        start_last_month, end_last_month, rand(1..alumnos.length),
        start_this_month, end_this_month))
    cursos.push(Curso.new('[G] Curso con inscripción cerrada', 'Curso generado que ya ha cerrado la inscripción pero no ha empezado',
        start_last_month, end_last_month, rand(1..alumnos.length),
        start_next_month, end_next_month))
    cursos.push(Curso.new('[G] Curso con inscripción abierta', 'Curso generado que está abierto a inscripciones',
        start_this_month, end_this_month, rand(1..alumnos.length),
        start_next_month, end_next_month))
    cursos.push(Curso.new('[G] Curso esperando a inscripción', 'Curso generado que está esperando a que se abra la inscripción',
        start_next_month, end_next_month, rand(1..alumnos.length),
        start_two_months_ago, end_two_months_ago))

    # cursos += generate_cursos((10 * ratio).to_i, entidades)

    # Generar colectivos
    ## Se necesitan colectivos preestablecidos.
    ## De momento, tan solo se almacena el nombre de
    ## los colegiados.
    nombres = ['Estudiantes', 'Colegiados', 'Otros']
    colectivos = []
    nombres.each do |n|
        colectivos.push(Colectivo.new(n))
    end

    # Generar costes
    ## Se genera un coste para cada combinación de
    ## curso y colectivo.
    costes = generate_costes(cursos, colectivos)

    ### Se genera un curso con una sola plaza después de generar inscripciones para que
    ### siempre tenga una plaza libre.
    cursos.push(Curso.new('[G] Curso con una plaza', 'Curso generado que tiene una plaza',
        start_this_month, end_this_month, 1,
        start_next_month, end_next_month))

    # Generar docentes
    ## No se necesitan casos específicos.
    docentes = generate_docentes((75 * ratio).to_i, entidades)

    # Generar cursos aleatorios
    ## Estos cursos pueden (o no) ser cursos externos
    ## contratados a empresas.
    cursos += generate_cursos((10 * ratio).to_i, entidades)

    ### Se genera un curso con una sola plaza después de generar inscripciones para que
    ### siempre tenga una plaza libre.
    cursos.push(Curso.new('[G] Curso con una plaza', 'Curso generado que tiene una plaza',
        start_this_month, end_this_month, 1,
        start_next_month, end_next_month))

    # Generar docencias
    ## Se necesita al menos una docencia por curso.
    ## No importa qué docentes imparten qué cursos.
    ## Las remuneraciones se generan aleatoriamente.
    ## No se pueden generar docencias de cursos externos.
    docencias = []

    cursos.each.with_index do |c, i|
        if c.entidad_id == nil
            docencias += generate_docencias(rand(1..(6 * ratio).to_i), docentes, i)
        end
    end

    # Generar docentes
    ## No se necesitan casos específicos.
    docentes = generate_docentes((50 * ratio).to_i, entidades)

    # Generar sesiones para los cursos
    ## Sin mayor relevancia para casos específicos.
    ## Se genera como mínimo un sesión por curso.
    aulas = ['AN-B3', 'AN-B6', 'AN-B1', 'AN-E', 'AN-B', 'AS-1', 'AN-P3', 'DO-1.S.31', 'DO-9', 'AN-S6', 'AN-C']
    sesiones = []

    cursos.each.with_index do |c, i|
        sesiones += generate_sesiones(rand(1..(5 * ratio).to_i), aulas, c, i)
    end

    # Generar facturas
    ## Solo se generan facturas de cursos completados.
    facturas = []
    cursos.each.with_index do |c, i|
        if c.end > Date.today
            next
        end
        if c.entidad_id != nil && rand < 0.75
            facturas.push(generate_factura_empresa(c, i))
        else
            num_docencias = docencias.filter { |d| d.curso_id == i }.length
            facturas += generate_facturas_docencias(rand(num_docencias/2..num_docencias), docencias, c, i)
        end
    end

    ## Generar un curso con MUCHOS sesiones, docencias e inscripciones.
    cursos.push(Curso.new('[G] Curso grande', 'Curso generado con muchos sesiones, inscripciones y docencias',
        start_last_month, end_last_month, alumnos.length, start_this_month, end_this_month))
    sesiones += generate_sesiones(rand((200 * ratio).to_i..(500 * ratio).to_i), aulas, cursos.last, cursos.length - 1)
    docencias += generate_docencias(rand(docentes.length/2..docentes.length), docentes, cursos.length - 1)

    alumnos.push(Alumno.new('Juan', 'Mier', 'mier@mier.info', ''))
    alumnos.push(Alumno.new('Test', 'Test', 'test@test.com', ''))

    # Generar inscripciones
    ## Se necesita una inscripción cancelada por curso.
    ## Como el estado de las inscripciones depende de los pagos,
    ## se generarán pagos para obtener varios estados de inscripción.
    inscripciones = []

    cursos.each.with_index do |c, i|
        inscripciones += generate_inscripciones(rand(1..c.plazas), alumnos, c, i, entidades, costes)
    end

    # Generar pagos
    ## Se generan una porción de pagos para todas las inscripciones, de forma
    ## que algunas estarán pagadas, otras sin pagar y otras con pagos demasiado grandes.
    pagos = generate_pagos(inscripciones.length/2, inscripciones, cursos, "PAGADO", costes)
    pagos += generate_pagos(inscripciones.length/10, inscripciones, cursos, "EXCESO", costes)

    # Generar archivo SQL a partir de los datos generados
    tables = {
        'alumno' => alumnos,
        'curso' => cursos,
        'docente' => docentes,
        'docencia' => docencias,
        'sesion' => sesiones,
        'inscripcion' => inscripciones,
        'factura' => facturas,
        'pago' => pagos,
        'coste' => costes,
        'entidad' => entidades,
        'colectivo' => colectivos
    }

    tables
end

ratio = ARGV.length == 1 ? ARGV[0].to_f : 1.0
if ratio < 0.0
    puts "el ratio debe de ser mayor o igual que 0."
    exit(1)
elsif ratio > 25
    puts "WARNING! ratio elevado. you may be here a while!"
end

# variables globales
filename = 'src/main/resources/data.sql'
print "Generando datos... [  ]"
time = Time.now
sql = "-- generated by main.rb on #{time}\n-- DO NOT EDIT: changes will be replaced.\n\n"

# funcionamiento del script
data = obtain_data(ratio) # Generar todos los datos
data.each do |t|; sql += add_data_to_sql(t[1], t[0]); end # Añadir los datos de cada tabla a la cadena SQL
File.open(filename, 'w') do |f|; f.write(sql); end # Guardar la cadena SQL en el archivo especificado

# salida de resultados
puts "\rGenerando datos... [OK] (#{(Time.now - time).round(3)}s)"
length = File.open(filename, 'r').readlines.length
puts "#{length} líneas generadas en #{filename}"
if length > 50000
    puts "WARNING! presumiblemente, el archivo generado es demasiado grande para sqlite."
end
