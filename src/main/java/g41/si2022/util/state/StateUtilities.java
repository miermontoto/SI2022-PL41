package g41.si2022.util.state;

import java.time.LocalDate;
import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.PagoDTO;
import g41.si2022.util.db.Database;
import g41.si2022.util.exception.UnexpectedException;

public class StateUtilities {

	private StateUtilities () {
		throw new UnexpectedException("StateUtilities");
	}

	/**
	 * Returns the current state of a course for a given date.
	 * NOTE: Calling this function will execute a query.
	 *
	 * @param curso Course to be checked.
	 * @param today Reference date to be used.
	 * @return State of the course for the given date.
	 */
	public static CursoState getCursoState (CursoDTO curso, LocalDate today) {
		return getCursoState(curso, today, false);
	}

	/**
	 * Returns the current state of a course for a given date.
	 * NOTE: Calling this function will execute a query
	 * if the parameter <code>canBeCerrado</code> is set to <code>true</code>.
	 *
	 * @param curso Course to be checked.
	 * @param today Reference date to be used.
	 * @param canBeCerrado true if the curso can be cerrado, false if not.
	 * @return State of the course for the given date.
	 */
	public static CursoState getCursoState (CursoDTO curso, LocalDate today, boolean canBeCerrado) {
		if (canBeCerrado && getCursosWithStates(curso.getId(), today).get(0).getCursoEstado() != null) {
			return CursoState.CERRADO;
		}
		if (curso.getStart_inscr().compareTo(today.toString()) > 0) { // if inscription start is before today
			return CursoState.INSCRIPCION_SIN_ABRIR;
		} else if (curso.getEnd_inscr().compareTo(today.toString()) > 0) { // if inscription end is before today
			return CursoState.INSCRIPCION_ABIERTA;
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
	public static List<CursoDTO> getCursosWithStates (LocalDate today) {
		String sql =
				"SELECT *, CASE WHEN fecha_pago IS NOT NULL THEN 'CERRADO' ELSE NULL END AS cursoEstado\r\n "
				+ "FROM curso "
				+ "LEFT JOIN docencia ON curso.id = docencia.curso_id "
				+ "LEFT JOIN factura ON factura.docencia_id = docencia.id";
		List<CursoDTO> lc = new Database().executeQueryPojo(CursoDTO.class, sql);
		lc.forEach(x -> x.setCursoEstado(x.getCursoEstado() == null ? getCursoState(x, today, false).toString() : x.getCursoEstado()));
		return lc;
	}

	/**
	 * Returns the cursos with the states.
	 * This function will return the CERRADO state in particular, which is not returned by <code>getCursoState</code>.
	 *
	 * @return Cursos with states.
	 */
	public static List<CursoDTO> getCursosWithStates (String cursoId, LocalDate today) {
		String sql =
				"SELECT *, CASE WHEN fecha_pago IS NOT NULL THEN 'CERRADO' ELSE NULL END AS cursoEstado\r\n "
				+ "FROM curso "
				+ "LEFT JOIN docencia ON curso.id = docencia.curso_id "
				+ "LEFT JOIN factura ON factura.docencia_id = docencia.id "
				+ "WHERE curso.id = ?";
		List<CursoDTO> lc = new Database().executeQueryPojo(CursoDTO.class, sql);
		lc.forEach(x -> x.setCursoEstado(x.getCursoEstado() == null ? getCursoState(x, today, false).toString() : x.getCursoEstado()));
		return lc;
	}

	/**
	 * Returns the InscripcionState for a given Course and student.
	 *
	 * @param curso Course that is being paid.
	 * @param pagos Payments of this student and this course.
	 * @return State of the inscription
	 */
	public static InscripcionState getInscripcionState (Double coste, java.util.List<PagoDTO> pagos) {
		double paid = 0;
		//double devuelto = 0;
		for (PagoDTO p : pagos) {
			paid += Double.parseDouble(p.getImporte());
			/*if(p.getImportedevuelto() != null) {
				devuelto += Double.parseDouble(p.getImportedevuelto());
			}*/
		}

		if (paid > coste) return InscripcionState.EXCESO;
		if (paid < coste) return InscripcionState.PENDIENTE;
		return InscripcionState.PAGADA;
	}

	/**
	 * Returns the InscripcionState for a given Course and student.
	 *
	 * @param curso Course that is being paid.
	 * @param pagos Payments of this student and this course.
	 * @return State of the inscription
	 */
	public static InscripcionState getInscripcionState (Double coste, Double pagado) {

		if (pagado > coste) return InscripcionState.EXCESO;
		if (pagado < coste) return InscripcionState.PENDIENTE;
		return InscripcionState.PAGADA;
	}


}
