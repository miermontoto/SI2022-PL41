package g41.si2022.coiipa.estado_actividades;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.dto.PagoDTO;

public class EstadoActividadesModel extends g41.si2022.mvc.Model {

	public List<CursoDTO> getListaCursos() {
		String sql = "select id, nombre, plazas, start, end, start_inscr, end_inscr, importe from curso";
		return getDatabase().executeQueryPojo(CursoDTO.class, sql);
	}

	// Modificar query para que devuelva los valores necesarios de inscripción de tipo CursoDTO
	public List<InscripcionDTO> getListaInscr(String idCurso) {
		String sql = "SELECT i.id, i.fecha, a.nombre as alumno_nombre,"
		+ " a.apellidos as alumno_apellidos, cos.coste as curso_coste,"
		+ " i.cancelada as cancelada, e.nombre as entidad_nombre,"
		+ " CASE WHEN sum(p.importe) is null THEN 0 ELSE sum(p.importe) END as pagado"
		+ " FROM inscripcion as i"
		+ " INNER JOIN alumno as a ON i.alumno_id = a.id"
		+ " INNER JOIN curso as cur ON i.curso_id = cur.id"
		+ " INNER JOIN coste AS cos ON cos.id = i.coste_id"
		+ " LEFT JOIN entidad AS e ON e.id = i.entidad_id"
		+ " LEFT JOIN pago AS p ON p.inscripcion_id = i.id"
		+ " WHERE cur.id = ? group by i.id";

		return getDatabase().executeQueryPojo(InscripcionDTO.class, sql, idCurso);
	}

	public String getGastos(String idCurso) {
		String sql = "SELECT sum(remuneracion) FROM docencia WHERE curso_id = ?";

		try {
			return getDatabase().executeQuerySingle(sql, idCurso).toString();
		} catch (Exception ex) {return "-";}
	}

	/**
	 * This method will return the total earnings for a given curso
	 * supposing that all alumnos that have signed up will pay.<br>
	 * Note that this method does not take into account costs.
	 *
	 * @param idCurso ID of the curso to be checked
	 * @return Amount that should be earned by this curso.
	 */
	public String getIngresosEstimados(String idCurso) {
		String sql = "select sum(cur.coste) from inscripcion as i"
			+ " inner join coste as cur on i.coste_id = cur.id"
			+ " where i.curso_id = ?";

		try {
			return String.valueOf((double) getDatabase().executeQuerySingle(sql, idCurso));
		} catch (NullPointerException ex) { return "0"; }
	}

	public List<PagoDTO> getListaPagos(String idCurso) {
		String sql = "SELECT * FROM pago"
			+ " INNER JOIN inscripcion ON pago.inscripcion_id = inscripcion.id"
			+ " WHERE inscripcion.id = ?";
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
