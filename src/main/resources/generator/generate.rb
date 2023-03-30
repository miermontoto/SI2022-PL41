def generate_alumnos(num)
    alumnos = []
    num.times do
        a = Alumno.new
        a.nombre = "[G] " + Faker::Name.first_name
        a.apellidos = Faker::Name.last_name
        a.email = Faker::Internet.email
        a.telefono = Faker::PhoneNumber.phone_number
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
        d.telefono = Faker::PhoneNumber.phone_number
        d.direccion = Faker::Address.street_address
        d.dni = Faker::IDNumber.spanish_citizen_number
        docentes.push(d)
    end
    docentes
end

def generate_eventos(num, aulas, curso, curso_id)
    rng = Random.new
    eventos = []
    num.times do
        e = Evento.new
        e.loc = aulas.sample
        e.fecha = curso.start + rng.rand(0..(curso.end - curso.start))
        e.hora_ini = OnlyTime.new(rng.rand(8..18))
        e.hora_fin = OnlyTime.new(e.hora_ini.hours + rng.rand(1..3))
        e.curso_id = curso_id + 1
        e.observaciones = 'Evento generado automáticamente'
        eventos.push(e)
    end
    eventos
end

def generate_docencias(num, docentes, curso_id)
    rng = Random.new
    docencias = []
    num.times do
        d = Docencia.new
        d.curso_id = curso_id + 1
        d.docente_id = rng.rand(0..docentes.length - 1)
        d.remuneracion = rng.rand(100..500)
        docencias.push(d)
    end
    docencias
end

def generate_inscripciones(num, alumnos, curso, curso_id)
    rng = Random.new
    inscripciones = []
    num.times do
        i = Inscripcion.new
        i.curso_id = curso_id + 1
        i.alumno_id = rng.rand(1..alumnos.length)
        while inscripciones.any? { |insc| insc.alumno_id == i.alumno_id }
            i.alumno_id = rng.rand(1..alumnos.length)
        end
        i.fecha = curso.start + rng.rand(0..(curso.end - curso.start))
        i.cancelada = 0
        inscripciones.push(i)
    end
    inscripciones.sample.cancelada = 1
    inscripciones
end

def generate_facturas_docencias(num, docencias, curso, curso_id)
    rng = Random.new
    facturas = []
    num.times do
        f = Factura.new
        f.docencia_id = (1..docencias.length).to_a.sample
        while facturas.any? { |fact| fact.docencia_id == f.docencia_id }
            f.docencia_id = (1..docencias.length).to_a.sample
        end
        f.fecha = curso.end + rng.rand(0..(Date.today - curso.end))
        facturas.push(f)
    end
    facturas
end

def generate_pagos(num, inscripciones, cursos, tipo)
    rng = Random.new
    pagos = []
    num.times do
        p = Pago.new
        i = inscripciones.sample
        p.inscripcion_id = inscripciones.index(i) + 1
        while pagos.any? { |pag| pag.inscripcion_id == p.inscripcion_id }
            i = inscripciones.sample
            p.inscripcion_id = inscripciones.index(i) + 1
        end
        p.fecha = i.fecha + rng.rand(0..7)
        if tipo == "EXCESO"
            p.importe = cursos.fetch(i.curso_id - 1).coste + rng.rand(1..100)
        elsif tipo == "PAGADO"
            p.importe = cursos.fetch(i.curso_id - 1).coste
        end

        pagos.push(p)
    end
    pagos
end
