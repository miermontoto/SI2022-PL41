# Proyecto COIIPA (SI2022-PL41)
\[[proyecto base original](https://github.com/javiertuya/samples-test-dev)\] |
\[[redkanban](https://in2test.lsi.uniovi.es/redkanban/)\] |
\[[teams doc](https://unioviedo.sharepoint.com/sites/CV22_SistemasdeInformacinGradoenIngenieraInformticaenTecnolo-SI2022-PL41/Documentos%20compartidos/Forms/AllItems.aspx?id=%2Fsites%2FCV22%5FSistemasdeInformacinGradoenIngenieraInformticaenTecnolo%2DSI2022%2DPL41%2FDocumentos%20compartidos%2FSI2022%2DPL41&p=true&ga=1)\] |
\[[modelo de dominio](src/main/resources/mod_dominio.md)\]

 
## instalar (temporal)
1. `Maven > Update Project...`
2. ???
3. Profit

Solo funciona en JRE 1.8 (Java 8)

---

# sprint 1
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
- **cursos** (en planificación, en periodo de inscripción, en curso, cancelada, finalizada)
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
			- el tesorero (se puede equivocar)
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
		- el tesorero realiza la transferencia (se puede equivocar)
		- tras la transferencia, se anota otra vez en el sistema
- **economía**
	- fundamental ingresos y gastos en un determinado periodo sobre los cursos de formación

## restricciones
- solo planificar actividades que tengan un unico profesor.
- solo planificar actividades que se realizan en una única fecha.
- el formulario solo se rellena por un miembro de la secretaría administrativa.
- el único coste del curso es aquel de la remuneración del profesor.
- el único ingreso del curso es el de las inscripciones.
- se asume que todas las transferencias se realizan con el importe correcto.
- se asume que el tesorero no se puede equivocar a la hora de realizar transferencias.

# sprint2

## objetivos
- (deshacer) actividades de más de una fecha.
- (deshacer) las transferencias pueden tener un importe incorrecto.
- (deshacer) actividades con más de un profesor
- contratar cursos a empresas (docencias)
- formalizar el cierre de un curso (cerrar el sistema comprobando incidencias, devoluciones, impagos...)
- diferentes precios para colectivos diferentes
- retrasar curso cuando no hay suficientes inscripciones (fechas nuevas, acuerdo con docencia, se avisa a los inscritos)
- posibilidad de cancelar actividades (se reflejan devoluciones)
- poder gestionar pagos y cobros por caja (en lugar de transferencias, tener en cuenta límite legal)
- gestionar inscripciones múltiples (realizadas por entidades)

