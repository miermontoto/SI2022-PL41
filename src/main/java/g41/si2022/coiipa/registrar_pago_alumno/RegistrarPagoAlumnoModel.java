package g41.si2022.coiipa.registrar_pago_alumno;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.dto.PagoDTO;

public class RegistrarPagoAlumnoModel extends g41.si2022.mvc.Model {

	public List<InscripcionDTO> getInscripciones(String date) {
		String sql =
				"select i.id,"
				+ " i.alumno_id as alumno_id,"
				+ " a.nombre as alumno_nombre,"
				+ " c.coste as curso_coste,"
				+ " CASE WHEN sum(pa.importe) IS NOT NULL THEN sum(pa.importe) ELSE 0 END as pagado,"
				+ " c.nombre as curso_nombre,"
				+ " i.curso_id as curso_id,"
				+ " i.fecha as fecha"
				+ " from inscripcion as i inner join alumno as a ON i.alumno_id = a.id"
				+ " inner join curso as c on c.id = i.curso_id"
				+ " left join pago as pa on pa.inscripcion_id = i.id"
				+ " where i.fecha<=? and c.start >=? group by i.id order by i.fecha asc";
		return this.getDatabase().executeQueryPojo(InscripcionDTO.class, sql, date, date);
	}

	public CursoDTO getCurso (String id) {
		String sql =
				" SELECT * "
				+ " FROM curso "
				+ " WHERE curso.id = ?";
		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql, id).get(0);
	}

	public List<PagoDTO> getPagos () {
		String sql =
				"SELECT * "
				+ " FROM pago "
				+ " INNER JOIN inscripcion ON inscripcion.id = pago.inscripcion_id ";

		return this.getDatabase().executeQueryPojo(PagoDTO.class, sql);
	}

	public List<PagoDTO> getPagos (String alumnoId, String cursoId) {
		String sql =
				"SELECT * "
				+ " FROM pago "
				+ " INNER JOIN inscripcion ON inscripcion.id = pago.inscripcion_id "
				+ " left join inscripcioncancelada on inscripcioncancelada.inscripcion_id = inscripcion.id"
				+ " WHERE inscripcion.alumno_id = ? "
				+ " AND inscripcion.curso_id = ?";
		return this.getDatabase().executeQueryPojo(PagoDTO.class, sql, alumnoId, cursoId);
	}

	public void registrarPago(String importe, String fecha, String idInscripcion) {
		String sql = "INSERT INTO pago (importe, fecha, inscripcion_id) VALUES(?,?,?)";
		this.getDatabase().executeUpdate(sql, importe, fecha, idInscripcion);
	}

	public String getEmailAlumno(String idAlumno) {
		String sql = "select email from alumno where id=?";
		return (String) this.getDatabase().executeQueryArray(sql, idAlumno).get(0)[0];
	}

	public boolean isCancelled(String idInscripcion) {
		String sql = "select count(id) from inscripcioncancelada where inscripcion_id=?";
		int test = (int) this.getDatabase().executeQueryArray(sql, idInscripcion).get(0)[0];

		return test > 0;
	}
}
