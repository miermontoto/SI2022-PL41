require_relative 'util.rb'
require 'faker'
Faker::Config.locale = 'es'

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

def generate_docentes(num, entidades)
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

def generate_sesiones(num, aulas, curso, curso_id)
    sesiones = []

    num.times do
        e = Sesion.new

        e.loc = aulas.sample
        e.fecha = curso.start + rand(0..(curso.end - curso.start))
        e.hora_ini = OnlyTime.new(rand(8..18))
        e.hora_fin = OnlyTime.new(e.hora_ini.hours + rand(1..3))
        e.curso_id = curso_id + 1
        e.observaciones = 'Sesion generado automáticamente'

        sesiones.push(e)
    end

    sesiones
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
        i.alumno_id = alumnos.index(alumnos.sample)
        i.fecha = curso.start + rand(0..(curso.end - curso.start))
        i.cancelada = 0
        i.entidad_id = rand > 0.8 ? rand(1..entidades.length - 1) : nil
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
        f.fecha = curso.end + rand(0..(Date.today - curso.end)).to_i
        f.curso_id = nil

        facturas.push(f)
    end

    facturas
end

def generate_factura_empresa(curso, curso_id)
    f = Factura.new

    f.docencia_id = nil
    f.curso_id = curso_id + 1
    f.docencia_id = nil
    f.fecha = curso.end + rand(0..(Date.today - curso.end)).to_i

    f
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

            c.curso_id = cu + 1
            c.colectivo_id = co
            c.coste = rand(10..250)

            costes.push(c)
        end
    end

    costes
end

def generate_cursos(num, entidades)
    cursos = []

    num.times do
        c = Curso.new

        c.nombre = "[G] " + Faker::Educator.course_name
        c.descripcion = Faker::Lorem.paragraph
        c.start_inscr = Date.today + rand(-365..365)
        c.end_inscr = c.start_inscr + rand(1..30)
        c.plazas = rand(10..200)
        c.start = c.end_inscr + rand(1..30)
        c.end = c.start + rand(1..30)
        c.entidad_id = rand > 0.5 ? rand(1..entidades.length - 1) : nil
        c.importe = c.entidad_id != nil ? rand(10..250) : nil
        if c.entidad_id != nil
            c.nombre += " [E]"
        end
        cursos.push(c)
    end

    cursos
end
