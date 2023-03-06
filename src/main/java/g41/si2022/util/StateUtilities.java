package g41.si2022.util;

import java.time.LocalDate;
import g41.si2022.coiipa.dto.PagoDTO;

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
	
	/**
	 * Returns the InscripcionState for a given Course and student.
	 * 
	 * @param curso Course that is being paid.
	 * @param pagos Payments of this student and this course.
	 * @return State of the inscription
	 */
	public static InscripcionState getInscripcionState (Double coste, java.util.List<PagoDTO> pagos) {
		double paid = 0;
		double devuelto = 0;
		for (PagoDTO p : pagos) {
			paid += Double.parseDouble(p.getImporte());
			
			if(!(p.getImportedevuelto() == null)) {
				
				devuelto += Double.parseDouble(p.getImportedevuelto());
				
			}
		}
		if (devuelto > 0) return InscripcionState.CANCELADA;
		if (paid > coste) return InscripcionState.EXCESO;
		if (paid < coste) return InscripcionState.PENDIENTE;
		
		return InscripcionState.PAGADA;
	}

}
