## Clases abstractas

class Persona
    attr_accessor :nombre, :apellidos, :email, :telefono
end

## Tablas de objetos

class Alumno < Persona # telefono is optional
    def initialize(nombre = nil, apellidos = nil, email = nil, telefono = nil)
        @nombre = nombre unless nombre.nil?
        @apellidos = apellidos unless apellidos.nil?
        @email = email unless email.nil?
        @telefono = telefono unless telefono.nil?
    end
end

class Docente < Persona # telefono is not optional
    attr_accessor :dni, :direccion
end

class Inscripcion
    attr_accessor :fecha, :curso_id, :alumno_id, :cancelada
end

class Curso
    attr_accessor :nombre, :descripcion, :coste
    attr_accessor :start_inscr, :end_inscr, :plazas
    attr_accessor :start, :end

    def initialize(nombre, descripcion, coste, startInscr, endInscr, plazas, startCourse, endCourse)
        @nombre = nombre
        @descripcion = descripcion
        @coste = coste
        @start_inscr = startInscr
        @end_inscr = endInscr
        @plazas = plazas
        @start = startCourse
        @end = endCourse
    end
end

class Factura
    attr_accessor :fecha, :docencia_id
end

class Pago
    attr_accessor :importe, :fecha, :inscripcion_id, :factura_id
end

class Evento
    attr_accessor :fecha, :hora_ini, :hora_fin, :loc, :curso_id, :observaciones
end

## Tablas de unión

class Docencia
    attr_accessor :remuneracion, :curso_id, :docente_id
end
