# Proyecto SI
## info
- Proyecto sobre COIPA, petición por el vicedecano
- sw para apoyar cursos de formación


## SI actual
- surgen ideas de cursos → planificación de temática → definición del curso → acuerdo con profesores
- los cursos tienen fechas tras los acuerdos con profesores
- las sesiones que se ofrecen: viernes tarde y sábado mañana
- situación típica: cursos de 16h (4h viernes y 4h sábado → dos semanas de curso)
- remuneración acordada con los profesores
- hay que reservar los espacios (aulas)
	- responsables de organización hablan con secretaria administrativa que reserva los espacios

## tareas
- identificar historias de usuario(funcionalidades) que sean factibles en un determinado sprint.
	- se plantean restricciones para delimitar funcionalidades al sprint.
	- restricción "regalada": planificar actividades que tengan únicamente un profesor y que se realicen en una única fecha.

## requisitos
- **cursos** (en planificación, en periodo de inscripción, en curso...)
	- una lista de los cursos (nombre, estado, periodo de inscripcion, total plazas, plazas libres, fechas)
		- al seleccionar la lista, poder incluir más información (descripción, profesores...)
		- información económica sobre cursos (balance)
	- poder acceder a una lista de inscripciones al hacer click en un curso
		- sobre una persona: fecha de inscripción, lista de pagos...
- **alumnos**
	- *<u>inscripciones de alumnos</u>* (form)
    	- nombre, apellidos, email
    	- estudiar núm. plazas según curso y aulas a la hora de aceptar inscripciones.
    	- requiere que la persona pague.
    	- *restricción:* solo necesario proporcionar interfaces para que la secr. adm. inscriba a gente
	- *<u>cancelar inscripciones</u>*
		- política
			- si la cancelación es 7d antes o más, 100% devuelto
			- si la cancelación es 3d antes o más, 50% devuelto
			- si la cancelación es 0d antes o más, 0% devuelto
		- devolución por transferencia por parte del tesorero
			- el tesorero
	- *<u>pagos</u>*
    	- a partir del momento que te inscribes, se da un plazo de tiempo para realizar la transferencia. (2d)
    	- si tras ese plazo no ha realizado la transferencia, se le avisa a la persona.
    	- lo que tiene que suceder en el sw: la secr. adm. tiene que poder anotar cuándo la transferencia llega.
    	- en el caso de que la transferencia tenga un importe diferente al precio del curso, el sw tiene que avisar.
    	- no se reserva la plaza de manera definitiva hasta que se realice el pago.
- **profesores**
	- *<u>pagos a profesores</u>*
    	- al concluir el curso
    	- se registran facturas en el sistema
    	- el tesorero realiza la transferencia
    	- tras la transferencia, se anota otra vez en el sistema
- **economía**
	- fundamental ingresos y gastos en un determinado periodo sobre los cursos de formación
