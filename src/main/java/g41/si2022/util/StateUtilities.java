package g41.si2022.util;

import java.time.LocalDate;

public class StateUtilities {

	private StateUtilities () {
		throw new UnexpectedException("StateUtilities");
	}
	
	/**
	 * Returns the current state of a course for a given date.
	 * 
	 * @param curso Course to be checked.
	 * @param today Reference date to be used.
	 * @return State of the course for the given date.
	 */
	public static CursoState getCursoState (g41.si2022.coiipa.dto.CursoDTO curso, LocalDate today) {
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
	
}
