package g41.si2022.util.state;

import java.time.LocalDate;
import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.dto.PagoDTO;
import g41.si2022.util.db.Database;
import g41.si2022.util.exception.UnexpectedException;

public class StateUtilities {

	public static final byte INSCRIPCION_DELAY_TIME = 2;

	private StateUtilities () {
		throw new UnexpectedException("StateUtilities");
	}

	/* --- CURSO STATE --- */

	/**
	 * Returns the current state ({@link CursoState}) of a course for a given date.
	 * @param idCurso id of the course to be checked.
	 * @param today Reference date to be used.
	 * @return {@link CursoState} of the course for the given date.
	 */
	public static CursoState getCursoState(String idCurso, LocalDate today) {
		String sql = "select * from curso where id = ?";
		return getCursoState(new Database().executeQueryPojo(CursoDTO.class, sql, idCurso).get(0), today);
	}

	/**
	 * Returns the current state ({@link CursoState}) of a course for a given date.
	 * NOTE: Calling this function will execute a query.
	 *
	 * @param curso {@link CursoDTO} to be checked.
	 * @param today Reference date to be used.
	 * @return {@link CursoState} of the course for the given date.
	 *
	 * @see CursoDTO
	 * @see CursoState
	 */
	public static CursoState getCursoState(CursoDTO curso, LocalDate today) {
		return getCursoState(curso, today, false);
	}

	/**
	 * Returns the current state ({@link CursoState}) of a course for a given date.
	 * NOTE: Calling this function will execute a query
	 * if the parameter <code>canBeCerrado</code> is set to <code>true</code>.
	 *
	 * @param curso {@link CursoDTO} to be checked.
	 * @param today Reference date to be used.
	 * @param canBeCerrado true if the curso can be cerrado, false if not.
	 * @return {@link CursoState} of the course for the given date.
	 */
	public static CursoState getCursoState (CursoDTO curso, LocalDate today, boolean canBeCerrado) {
		if (canBeCerrado && getCursoDTOWithState(curso.getId(), today).get(0).getEstado() != null) {
			return CursoState.CERRADO;
		}
		if (curso.getStart_inscr().compareTo(today.toString()) > 0) { // if inscription start is before today
			return CursoState.PLANEADO;
		} else if (curso.getEnd_inscr().compareTo(today.toString()) > 0) { // if inscription end is before today
			return CursoState.EN_INSCRIPCION;
		} else if (curso.getStart().compareTo(today.toString()) > 0) { // if course start is before today
			return CursoState.INSCRIPCION_CERRADA;
		} else if (curso.getEnd().compareTo(today.toString()) > 0) { // if course end is before today
			return CursoState.EN_CURSO;
		} else { // if course end is after today
			return CursoState.FINALIZADO;
		}
	}

	/**
	 * Returns the cursos with the states.
	 * This function will return the CERRADO state in particular, which is not returned by <code>getCursoState</code>.
	 *
	 * @return Cursos with states.
	 */
	public static List<CursoDTO> getCursoDTOWithState(String cursoId, LocalDate today) {
		String sql =
				"SELECT *, CASE WHEN fecha_pago IS NOT NULL THEN 'CERRADO' ELSE NULL END AS cursoEstado\r\n "
				+ "FROM curso "
				+ "LEFT JOIN docencia ON curso.id = docencia.curso_id "
				+ "LEFT JOIN factura ON factura.docencia_id = docencia.id "
				+ "WHERE curso.id = ?";
		List<CursoDTO> lc = new Database().executeQueryPojo(CursoDTO.class, sql, cursoId);
		lc.forEach(x -> x.updateEstado(today));
		return lc;
	}

	/* --- INSCRIPCION STATES --- */

	/**
	 * Returns the current state ({@link InscripcionState}) of an inscription for a given id.
	 * @param idInscripcion
	 * @return {@link InscripcionState} of the inscription.
	 * @see InscripcionState
	 */
	public static InscripcionState getInscripcionState(String idInscripcion, LocalDate today) {
		String sql = "SELECT *, c.coste as curso_coste FROM inscripcion"
			+ " LEFT JOIN curso c ON c.id = inscripcion.curso_id"
			+ " WHERE inscripcion.id = ?";
		InscripcionDTO inscr = new Database().executeQueryPojo(InscripcionDTO.class, sql, idInscripcion).get(0);
		return getInscripcionState(inscr, today);
	}

	/**
	 * Returns the current state ({@link InscripcionState}) of an inscription.
	 * @param inscr {@link InscripcionDTO} to be checked.
	 * @return {@link InscripcionState} of the inscription.
	 */
	public static InscripcionState getInscripcionState(InscripcionDTO inscr, LocalDate today) {
		return getInscripcionState(inscr,
			new Database().executeQueryPojo(PagoDTO.class,
				"SELECT * FROM pago WHERE inscripcion_id = ?", inscr.getId()), today);
	}

	/**
	 * Returns the current state ({@link InscripcionState}) of an inscription.
	 * @param inscr {@link InscripcionDTO} to be checked.
	 * @param pagos List of {@link PagoDTO} that contains at least every payment related to the inscription.
	 * @return {@link InscripcionState} of the inscription.
	 * @see InscripcionState.
	 */
	public static InscripcionState getInscripcionState(InscripcionDTO inscr, List<PagoDTO> pagos, LocalDate today) {
		if(Integer.parseInt(inscr.getCancelada()) == 1) return InscripcionState.CANCELADA;
		InscripcionState state = getInscripcionState(Double.parseDouble(inscr.getCurso_coste()), pagos);
		if (isDelayed(inscr, today)) {
			if (state == InscripcionState.PENDIENTE) return InscripcionState.RETRASADA;
			if (state == InscripcionState.EXCESO) return InscripcionState.RETRASADA_EXCESO;
		}
		return state;
	}

	/**
	 * Returns the InscripcionState for a given Course and student.
	 *
	 * @param coste Cost of the course.
	 * @param pagos Payments of this student and this course.
	 * @return State of the inscription
	 */
	public static InscripcionState getInscripcionState(Double coste, List<PagoDTO> pagos) {
		double paid = 0;
		for (PagoDTO p : pagos) paid += Double.parseDouble(p.getImporte());
		return getInscripcionState(coste, paid);
	}

	/**
	 * Returns the InscripcionState for a given course and student.
	 *
	 * @param coste Cost of the course.
	 * @param pagado Amount paid by the student.
	 * @return State of the inscription
	 */
	public static InscripcionState getInscripcionState(Double coste, Double pagado) {
		if (pagado > coste) return InscripcionState.EXCESO;
		if (pagado < coste) return InscripcionState.PENDIENTE;
		return InscripcionState.PAGADA;
	}

	/**
	 * Returns true if the inscription is delayed.
	 * @param inscr {@link InscripcionDTO} to be checked.
	 * @param today Reference date to be used.
	 * @return {@code true} if the inscription is delayed, {@code false} if not.
	 */
	public static boolean isDelayed(InscripcionDTO inscr, LocalDate today) {
		LocalDate inscrDate = LocalDate.parse(inscr.getFecha());
		return inscrDate.plusDays(INSCRIPCION_DELAY_TIME).compareTo(today) < 0;
	}

}
