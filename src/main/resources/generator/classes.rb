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

class Docente < Persona
    attr_accessor :dni, :direccion
end

class Inscripcion # grupo_id is optional
    attr_accessor :fecha, :cancelada
    attr_accessor :curso_id, :alumno_id, :coste_id, :entidad_id
end

class Curso
    attr_accessor :nombre, :descripcion
    attr_accessor :start_inscr, :end_inscr, :plazas
    attr_accessor :start, :end

    def initialize(nombre, descripcion, startInscr, endInscr, plazas, startCourse, endCourse)
        @nombre = nombre
        @descripcion = descripcion
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

class Sesion
    attr_accessor :fecha, :hora_ini, :hora_fin, :loc, :curso_id, :observaciones
end

class Entidad
    attr_accessor :nombre, :email, :telefono
end

class Colectivo
    attr_accessor :nombre

    def initialize(nombre)
        @nombre = nombre
    end
end

## Tablas de unión

class Docencia
    attr_accessor :remuneracion, :curso_id, :docente_id
end

class Coste
    attr_accessor :coste, :colectivo_id, :curso_id
end
