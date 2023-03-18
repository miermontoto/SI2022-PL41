package g41.si2022.util.state;

/**
 * A <code>InscripcionState</code> is an <code>Enum</code> that contains all the possible states of an
 * inscripción.<br>
 * 
 * $s = \sum_{i=0}^{inscripciones} inscripciones_i$
 * 
 * <ul>
 * <li> PENDIENTE. An inscripcion whose s is lower than the total cost.
 * <li> EXCESO. An inscripcion whose s is higher than the total cost.
 * <li> PAGADA. An inscripcion whose s is equal to the total cost.
 * <li> CANCELADA. An inscripcion that was cancelled.
 * </ul>
 * 
 * @author Alex // UO281827
 *
 */
public enum InscripcionState {
	/**
	 * An inscripcion whose s is lower than the total cost.
	 */
	PENDIENTE,
	/**
	 * An inscripcion whose s is higher than the total cost.
	 */
	EXCESO,
	/**
	 * An inscripcion whose s is equal to the total cost.
	 */
	PAGADA,
	/**
	 * An inscripcion that was cancelled.
	 */
	CANCELADA;
}
