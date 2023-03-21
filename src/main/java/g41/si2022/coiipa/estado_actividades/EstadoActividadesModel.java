package g41.si2022.coiipa.estado_actividades;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.dto.PagoDTO;

public class EstadoActividadesModel extends g41.si2022.mvc.Model {
	// messi
	public List<CursoDTO> getListaCursos() {
		String sql = "select id, nombre, coste, plazas, start, end, start_inscr, end_inscr from curso";
		return getDatabase().executeQueryPojo(CursoDTO.class, sql);
	}

	// Modificar query para que devuelva los valores necesarios de inscripción de tipo CursoDTO
	public List<InscripcionDTO> getListaInscr(String idCurso) {
		String sql = "SELECT i.id, i.fecha, a.nombre as alumno_nombre, "
		+ " a.apellidos as alumno_apellidos, c.coste as curso_coste"
		+ " FROM inscripcion as i"
		+ " INNER JOIN alumno as a ON i.alumno_id = a.id"
		+ " INNER JOIN curso as c ON i.curso_id = c.id"
		+ " WHERE curso_id = ?";

		return getDatabase().executeQueryPojo(InscripcionDTO.class, sql, idCurso);
	}

	public String getGastos(String idCurso) {
		String sql = "SELECT sum(remuneracion) FROM docencia WHERE curso_id = ?";

		try {
			return String.valueOf((int) getDatabase().executeQuerySingle(sql, idCurso));
		} catch (Exception ex) {return "-";}
	}

	public String getCosteCurso(String idCurso) {
		String sql = "SELECT coste FROM curso where id = ?";
		try {
			return String.valueOf((double) getDatabase().executeQuerySingle(sql, idCurso));
		} catch (IndexOutOfBoundsException ioob) {return "0";}
	}

	public String getIngresosEstimados(String idCurso) {
		String sql = "select coste * count(*) from curso "
				   + "inner join inscripcion on curso.id = inscripcion.curso_id "
				   + "where curso.id = ?";
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
