package g41.si2022.util.state;

import java.time.LocalDate;
import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.FacturaDTO;
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
	public static CursoState getCursoState(CursoDTO curso, LocalDate today, boolean canBeCerrado) {
		LocalDate startInscr = LocalDate.parse(curso.getStart_inscr());
		LocalDate endInscr = LocalDate.parse(curso.getEnd_inscr());
		LocalDate start = LocalDate.parse(curso.getStart());
		LocalDate end = LocalDate.parse(curso.getEnd());

		if (canBeCerrado && getCursoDTOWithState(curso.getId(), today).get(0).getEstado() != null) return CursoState.CERRADO;
    	if (getCursoStateDB(String.valueOf(curso.getId())).equals("CANCELADO")) return CursoState.CANCELADO;
		if (startInscr.isAfter(today)) return CursoState.PLANEADO;
		if (endInscr.isAfter(today) || endInscr.equals(today)) return CursoState.EN_INSCRIPCION;
		if (start.isAfter(today)) return CursoState.INSCRIPCION_CERRADA;
		if (end.isAfter(today) || end.isEqual(today)) return CursoState.EN_CURSO;
		return CursoState.FINALIZADO;
	}

	/**
	 * Gets the value of the status attribute of the course table. By default this attribute is 'null'.
	 * In the class GestionarCursoController.java this attribute is modified to 'CANCELADO' in order to
	 * perform course cancellations.
	 *
	 * @param idCurso Id of the course to get its status stored in the database.
	 * @return The value of the specified course status attribute.
	 */
	public static String getCursoStateDB(String idCurso) {
		String sql = "SELECT estado FROM curso WHERE id = ?";

		return String.valueOf(new Database().executeQuerySingle(sql, idCurso));
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

	/* --- CURSO TYPES --- */
	public static CursoType getCursoType(String idCurso) {
		String sql = "SELECT id, entidad_id FROM curso WHERE id = ?";
		return getCursoType(new Database().executeQueryPojo(CursoDTO.class, sql, idCurso).get(0));
	}

	public static CursoType getCursoType(CursoDTO curso) {
		return curso.getEntidad_id().equals("") ? CursoType.INTERNO : CursoType.EXTERNO;
	}


	/* --- INSCRIPCION STATES --- */

	/**
	 * Returns the current state ({@link InscripcionState}) of an inscription for a given id.
	 * @param idInscripcion
	 * @return {@link InscripcionState} of the inscription.
	 * @see InscripcionState
	 */
	public static InscripcionState getInscripcionState(String idInscripcion, LocalDate today) {
		String sql = "SELECT *, cos.coste as curso_coste FROM inscripcion as i"
			+ " LEFT JOIN curso c ON c.id = i.curso_id"
			+ " INNER JOIN coste as cos on cos.id = i.coste_id"
			+ " WHERE i.id = ?";
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
		if(Integer.parseInt(inscr.getEn_espera()) == 1) return InscripcionState.EN_ESPERA;
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

	/* ----- FACTURA STATES ----- */

	public static FacturaState getFacturaState(FacturaDTO factura) {
		return getFacturaState(Double.parseDouble(factura.getPagado()), Double.parseDouble(factura.getRemuneracion()));
	}

	public static FacturaState getFacturaState(double paid, double cost) {
		if (paid > cost) return FacturaState.EXCESO;
		if (paid < cost) return FacturaState.PENDIENTE;
		return FacturaState.PAGADA;
	}
}
