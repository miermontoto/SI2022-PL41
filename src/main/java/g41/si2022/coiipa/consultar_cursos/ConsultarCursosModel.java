package g41.si2022.coiipa.consultar_cursos;

import java.util.List;


import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.coiipa.dto.InscripcionDTO;
import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.util.Database;

public class ConsultarCursosModel {

	private Database db = new Database();

	public List<CursoDTO> getListaCursos() {
		String sql = "SELECT * FROM curso "
				   + "INNER JOIN inscripcion on inscripcion.id = curso.id ";
				   
		return db.executeQueryPojo(CursoDTO.class, sql);
	}

	public List<CursoDTO> getListaCursosInscr() {
		String sql = "SELECT curso.nombre, coste, plazas, start, end, inscripcion.fecha as inscripcion_fecha, start_inscr, end_inscr, "
				   + "alumno.nombre as inscripcion_alumno "
				   + "FROM curso INNER JOIN inscripcion "
				   + "ON curso.id = inscripcion.curso_id "
				   + "INNER JOIN alumno ON inscripcion.id = alumno.id";

		return db.executeQueryPojo(CursoDTO.class, sql);
	}

	// Modificar query para que devuelva los valores necesarios de inscripción de tipo CursoDTO
	public List<InscripcionDTO> getListaInscr(String idCurso) {
		String sql = "SELECT i.id, i.fecha, a.nombre as alumno_nombre, "
		+ " a.apellidos as alumno_apellidos"
		+ " FROM inscripcion as i"
		+ " INNER JOIN alumno as a ON i.alumno_id = a.id"
		+ " WHERE curso_id = ?";

		return db.executeQueryPojo(InscripcionDTO.class, sql, idCurso);
	}

	public String getGastos(String idCurso) {
		String sql = "SELECT remuneracion FROM docencia WHERE curso_id = ?";

		try {
			return String.valueOf((int) db.executeQueryArray(sql, idCurso).get(0)[0]);
		} catch (IndexOutOfBoundsException ioob) {return "-";}
	}

	public String getCosteCurso(String idCurso) {
		String sql = "SELECT coste FROM curso where id = ?";
		try {
			return String.valueOf((double) db.executeQueryArray(sql, idCurso).get(0)[0]);
		} catch (IndexOutOfBoundsException ioob) {return "0";}
	}

	public String getIngresosEstimados(String idCurso) {
		String sql = "SELECT coste * plazas as ingrEst from curso WHERE id = ?";
		try {
			return String.valueOf((double) db.executeQueryArray(sql, idCurso).get(0)[0]);
		} catch (IndexOutOfBoundsException ioob) { return "0"; }
	}

	public List<PagoDTO> getListaPagos(String idCurso) {
		String sql = "SELECT * FROM pago "
					 + "INNER JOIN inscripcion ON pago.inscripcion_id = inscripcion.id "
					 + "WHERE inscripcion.id = ?" ;

		return db.executeQueryPojo(PagoDTO.class, sql, idCurso);
	}

	public String getImportePagos(String idInscripcion) {
		String sql = "SELECT sum(importe) FROM pago as p "
					 + "INNER JOIN inscripcion as i ON p.inscripcion_id = i.id "
			 		 + "WHERE i.id = ?" ;

		try {
			return String.valueOf((double) db.executeQueryArray(sql, idInscripcion).get(0)[0]);
		} catch (NullPointerException npe) { return "0.0"; }
	}
}
