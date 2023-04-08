package g41.si2022.coiipa.estado_actividades;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.dto.PagoDTO;

public class EstadoActividadesModel extends g41.si2022.mvc.Model {

	public List<CursoDTO> getListaCursos() {
		String sql = "select id, nombre, plazas, start, end, start_inscr, end_inscr from curso";
		return getDatabase().executeQueryPojo(CursoDTO.class, sql);
	}

	// Modificar query para que devuelva los valores necesarios de inscripción de tipo CursoDTO
	public List<InscripcionDTO> getListaInscr(String idCurso) {
		String sql = "SELECT i.id, i.fecha, a.nombre as alumno_nombre,"
		+ " a.apellidos as alumno_apellidos, cc.coste as curso_coste,"
		+ " i.cancelada as cancelada"
		+ " FROM inscripcion as i"
		+ " INNER JOIN alumno as a ON i.alumno_id = a.id"
		+ " INNER JOIN curso as c ON i.curso_id = c.id"
		+ " INNER JOIN costecolectivo AS cc ON i.costecolectivo_id = cc.id"
		+ " WHERE curso_id = ?";

		return getDatabase().executeQueryPojo(InscripcionDTO.class, sql, idCurso);
	}

	public String getGastos(String idCurso) {
		String sql = "SELECT sum(remuneracion) FROM docencia WHERE curso_id = ?";

		try {
			return String.valueOf((int) getDatabase().executeQuerySingle(sql, idCurso));
		} catch (Exception ex) {return "-";}
	}

	/**
	 * getIngresosEstimados.
	 * This method will return the total earnings for a given curso 
	 * supposing that all alumnos that have signed up will pay.<br>
	 * Note that this method does not take into account costs.
	 * 
	 * @param idCurso ID of the curso to be checked
	 * @return Amount that should be earned by this curso.
	 */
	public String getIngresosEstimados(String idCurso) {
		String sql = "SELECT SUM(cc.coste * ("
				+ "	SELECT COUNT(id) "
				+ " FROM inscripcion AS i"
				+ " WHERE i.costecolectivo_id = cc.id"
				+ " GROUP BY i.id"
				+ ")"
				+ ")"
				+ " FROM costecolectivo AS cc"
				+ " WHERE cc.curso_id = ?"
				+ " GROUP BY cc.id";
		try {
			return String.valueOf((double) getDatabase().executeQuerySingle(sql, idCurso));
		} catch (Exception ex) { return "0"; }
	}

	public List<PagoDTO> getListaPagos(String idCurso) {
		String sql = "SELECT * FROM pago "
					 + "INNER JOIN inscripcion ON pago.inscripcion_id = inscripcion.id "
					 + "WHERE inscripcion.id = ?" ;

		return getDatabase().executeQueryPojo(PagoDTO.class, sql, idCurso);
	}

	public String getImportePagosFromInscripcion(String idInscripcion) {
		String sql = "SELECT sum(importe) FROM pago as p "
					 + "INNER JOIN inscripcion as i ON p.inscripcion_id = i.id "
			 		 + "WHERE i.id = ?" ;

		try {
			return String.valueOf((double) getDatabase().executeQuerySingle(sql, idInscripcion));
		} catch (NullPointerException npe) { return "0.0"; }
	}

	public String getImportePagosFromCurso(String idCurso) {
		String sql = "SELECT sum(importe) FROM pago as p "
					 + "INNER JOIN inscripcion as i ON p.inscripcion_id = i.id "
			 		 + "WHERE i.curso_id = ?" ;

		try {
			return String.valueOf((double) getDatabase().executeQuerySingle(sql, idCurso));
		} catch (NullPointerException npe) { return "0.0"; }
	}
}
