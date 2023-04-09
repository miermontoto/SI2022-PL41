require_relative 'util.rb'

def generate_alumnos(num)
    alumnos = []

    num.times do
        a = Alumno.new

        a.nombre = "[G] " + Faker::Name.first_name
        a.apellidos = Faker::Name.last_name
        a.email = Faker::Internet.email
        a.telefono = phonify(Faker::PhoneNumber.cell_phone)

        alumnos.push(a)
    end

    alumnos
end

def generate_docentes(num)
    docentes = []

    num.times do
        d = Docente.new

        d.nombre = "[G] " + Faker::Name.first_name
        d.apellidos = Faker::Name.last_name
        d.email = Faker::Internet.email
        d.telefono = phonify(Faker::PhoneNumber.cell_phone)
        d.direccion = Faker::Address.street_address
        d.dni = Faker::IDNumber.spanish_citizen_number

        docentes.push(d)
    end

    docentes
end

def generate_eventos(num, aulas, curso, curso_id)
    eventos = []

    num.times do
        e = Evento.new

        e.loc = aulas.sample
        e.fecha = curso.start + rand(0..(curso.end - curso.start))
        e.hora_ini = OnlyTime.new(rand(8..18))
        e.hora_fin = OnlyTime.new(e.hora_ini.hours + rand(1..3))
        e.curso_id = curso_id + 1
        e.observaciones = 'Evento generado automáticamente'

        eventos.push(e)
    end

    eventos
end

def generate_docencias(num, docentes, curso_id)
    docencias = []

    num.times do
        d = Docencia.new

        d.curso_id = curso_id + 1
        d.docente_id = rand(0..docentes.length - 1)
        d.remuneracion = rand(100..500)

        docencias.push(d)
    end

    docencias
end

def generate_inscripciones(num, alumnos, curso, curso_id, entidades, costes)
    inscripciones = []

    num.times do
        i = Inscripcion.new

        i.curso_id = curso_id + 1
        i.alumno_id = alumnos.sample
        i.fecha = curso.start + rand(0..(curso.end - curso.start))
        i.cancelada = 0
        i.entidad_id = rand(1..entidades.length - 1) if rand > 0.8
        i.coste_id = costes.index(costes.filter { |coste| coste.curso_id == curso_id + 1 }.sample).to_i + 1

        inscripciones.push(i)
    end
    inscripciones.sample.cancelada = 1

    inscripciones
end

def generate_facturas_docencias(num, docencias, curso, curso_id)
    facturas = []

    num.times do
        f = Factura.new

        f.docencia_id = (1..docencias.length).to_a.sample
        while facturas.any? { |fact| fact.docencia_id == f.docencia_id }
            f.docencia_id = (1..docencias.length).to_a.sample
        end
        f.fecha = curso.end + rand(0..(Date.today - curso.end))

        facturas.push(f)
    end

    facturas
end

def generate_pagos(num, inscripciones, cursos, tipo, costes)
    pagos = []

    num.times do
        p = Pago.new

        i = inscripciones.sample
        p.inscripcion_id = inscripciones.index(i) + 1
        while pagos.any? { |pag| pag.inscripcion_id == p.inscripcion_id }
            i = inscripciones.sample
            p.inscripcion_id = inscripciones.index(i) + 1
        end
        p.fecha = i.fecha + rand(0..7)

        coste = costes.fetch(i.coste_id - 1).coste
        if tipo == "EXCESO"
            p.importe = coste + rand(1..100)
        elsif tipo == "PAGADO"
            p.importe = coste
        end

        pagos.push(p)
    end

    pagos
end

def generate_entidades(num)
    entidades = []

    num.times do
        e = Entidad.new

        e.nombre = "[G] " + (rand > 0.5 ? Faker::University.name : Faker::Company.name)
        e.email = Faker::Internet.email
        e.telefono = rand > 0.75 ? phonify(Faker::PhoneNumber.phone_number) : nil

        entidades.push(e)
    end

    entidades
end

def generate_costes(cursos, colectivos)
    costes = []

    cursos.length.times do |cu|
        colectivos.length.times do |co|
            c = Coste.new

            c.curso_id = cu
            c.colectivo_id = co
            c.coste = rand(10..250)

            costes.push(c)
        end
    end

    costes
end
