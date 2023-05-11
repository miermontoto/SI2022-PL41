package g41.si2022.util.state;

/**
 *
 * A <code>CursoState</code> is an <code>Enum</code> that contains the different states a Curso could
 * be in.
 * <br>
 * <ul>
 * <li> CUALQUIERA. Used in filters to check for any <code>CursoState</code>.
 * <li> INSCRIPCION_SIN_ABRIR. A curso whose inscription start has not began yet.
 * <li> INSCRIPCION_ABIERTA. A curso whose inscripcion start has passed but its inscripcion end has not passed.
 * <li> INSCRIPCION_CERRADA. A curso whose inscription end has passed.
 * <li> EN_CURSO. A curso whose start date has passed.
 * <li> FINALIZADO. A curso whose end date has passed but its teachers have not been paid.
 * <li> CERRADO. A curso whose end date has passed and all its teachers have been paid.
 * <li> CANCELADO. A cancelled curso.
 * <li> ABIERTO. A curso both in inscription and in course.
 * </ul>
 *
 * @author Alex // UO281827
 *
 */
public enum CursoState {
	CUALQUIERA,
	PLANEADO,
	EN_INSCRIPCION,
	INSCRIPCION_CERRADA,
	EN_CURSO,
	FINALIZADO,
	CERRADO,
	CANCELADO,
	ABIERTO;
}
