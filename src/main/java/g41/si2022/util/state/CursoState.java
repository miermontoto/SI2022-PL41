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
 * </ul>
 *
 * @author Alex // UO281827
 *
 */
public enum CursoState {
	/**
	 * Any <code>CursoState</code>.
	 */
	CUALQUIERA,
	/**
	 * A curso whose inscription start has not began yet.
	 */
	INSCRIPCION_SIN_ABRIR,
	/**
	 * A curso whose inscripcion start has passed but its inscripcion end has not passed.
	 */
	INSCRIPCION_ABIERTA,
	/**
	 * A curso whose inscription end has passed.
	 */
	INSCRIPCION_CERRADA,
	/**
	 * A curso whose start date has passed.
	 */
	EN_CURSO,
	/**
	 * A curso whose end date has passed but its teachers have not been paid.
	 */
	FINALIZADO,
	/**
	 * A curso whose end date has passed and all its teachers have been paid.
	 */
	CERRADO,
	/**
	 * A cancelled curso.
	 */
	CANCELADO;
}
