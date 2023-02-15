- Repo del equipo (github)
	- SI2022-PLXY
	- Compartir con prof.

V. Sem. --> Modelo

# Proyecto
Proyecto para el COIIPA, ordenado por el vicedecano. El software apoya la realización de los cursos de formación.
Actualmente, surgen ideas sobre un curso de formación organizados por miembros del curso denominados responsables de formacion.
Se especifica una temática del curso y unos objetivos. Se especifican unos formadores/as que llegan a un acuerdo para decidir quien imparte en que horario.
Cursos se realizan Viernes de tarde y sabados de mañana [Puede haber otros días?] en un sitio genérico (Gijón, Avilés).
Cada curso tiene un número de plazas.
Cada curso tiene un período de inscripción.
Cada profesor tiene una remuneración por el curso completo.
Un miembro de la secretaría administrativa se encarga de 'reservar' los sitios específicos (Edificio, Aula).

Una vez creada, se le da difusión a la actividad para que otras personas se subscriban.
Las personas rellenan un formulario de inscripcion.
	- Nombre
	- Apellidos
	- e-mail
Una persona se puede subscribir a un curso sii quedan plazas libres.
La inscripción require que la persona abone el pago del curso.
	- Pagos por transf.
Una vez inscrito, hay un plazo de un par de dias para realizar el pago.
	Un miembro de la secretaría administrativa revisa los pagos recibidos y marca las inscripciones que han sido abonados.
	En caso de que no haya sido abonado, se le realiza un aviso (no por el programa).
	Si la cantidad de un pago no coincide con la cantidad correspondiente del curso, el programa avisa al usuario.
	
Presentar un listado de los cursos con 
	- Nombre
	- Profesor
	- Descripción
	- Estado
	- Período inscripción
	- Fecha
	- Plazas
	- Plazas libres
	- BTN: Inscripciones
		- Listado de personas inscritas
			- Nombre
			- Apellidos
			- e-mail
			- Estado
				- Codificado por colores: { OK, SIN_PAGAR, PAGO_ERRONEO }
	- Ingresos totales
	- Costes totales
	- Ganancias totales
	
Miembros inscritos pueden querer cancelar una inscripcion
	- Cancelación con 7 dias naturales de antelacion del inicio --> 100% retornado
	- Cancelación con 6 días naturales de antelación del inicio --> 50 % retornado
	- Otherwise													--> 0  % retornado

El tesorero es quien realiza todos los pagos y se puede equivocar. Permitir cancelaciones.

El profesor emite una factura tras la finalizacion del curso con lo acordado inicialmente.
Secretaria administrativa anota en el sistema.
El tesorero realiza el pago.
Secretaria administrativa anota la realizacion del pago.

El sistema debe ofrecer un resumen de los ingresos y pagos de los cursos entre dos fechas específicas.
	Un movimiento que no haya sido realizado en el intervalo de tiempo pero pertenezca a un curso que si pertenezca al intervalo se muestra
	- Totales
	- Finalizado o en curso

Ingresos/Gastos realizados
|Curso|Dato|Dato|
|---|---|---|
|A|B|C|

Ingresos/Gastos en curso (por realizar / realizados posteriores al fin del intervalo)
|Curso|Dato|Dato|
|---|---|---|
|A|B|C|

# Restricciones
Planificar actividades que tengan un unico profesor.
Planificar actividades que se realizan en una única fecha.
El formulario solo se rellena por un miembro de la secretaría administrativa.
El unico coste del curso es aquel de la remuneración del profesor.

# Mod. Dom.
Curso (1) --- (*) Inscripcion
