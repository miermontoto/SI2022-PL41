package g41.si2022.coiipa.gestionar_inscripciones;

import java.time.LocalDate;
import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.dto.PagoDTO;
import g41.si2022.util.state.InscripcionState;

public class GestionarInscripcionesModel extends g41.si2022.mvc.Model {

	public List<InscripcionDTO> getInscripciones(boolean all, LocalDate today) {
		String sql = "select i.id,"
			+ " i.alumno_id as alumno_id,"
			+ " a.nombre as alumno_nombre,"
			+ " a.apellidos as alumno_apellidos, "
			+ " coste.coste as curso_coste,"
			+ " CASE WHEN sum(pa.importe) IS NOT NULL THEN sum(pa.importe) ELSE 0 END as pagado," // no borrar :)
			+ " c.nombre as curso_nombre,"
			+ " i.id as inscripcion_id, "
			+ " i.curso_id as curso_id,"
			+ " i.fecha as fecha,"
			+ " i.cancelada as cancelada,"
			+ " case when le.id is not null then true else false end as en_espera"
			+ " from inscripcion as i inner join alumno as a ON i.alumno_id = a.id"
			+ " inner join coste ON coste.id = i.coste_id"
			+ " inner join curso as c on c.id = i.curso_id"
			+ " left join pago as pa on pa.inscripcion_id = i.id"
			+ " left join lista_espera as le on le.inscripcion_id = i.id"
			+ " group by i.id order by i.fecha asc";
		List<InscripcionDTO> lista = this.getDatabase().executeQueryPojo(InscripcionDTO.class, sql);
		lista.forEach(i -> i.updateEstado(today)); // Actualizar todos los estados de las inscripciones.
		if (!all) lista.removeIf(i -> (!( // Filtrar las inscripciones oportunas.
			i.getEstado() == InscripcionState.PENDIENTE ||
			i.getEstado().toString().contains("EXCESO")
		)));
		return lista;
	}

	public CursoDTO getCurso(String id) {
		String sql = "SELECT * FROM curso WHERE curso.id = ?";
		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql, id).get(0);
	}

	public List<PagoDTO> getPagos() {
		String sql = "SELECT * FROM pago"
				+ " INNER JOIN inscripcion ON inscripcion.id = pago.inscripcion_id ";
		return this.getDatabase().executeQueryPojo(PagoDTO.class, sql);
	}

	public List<PagoDTO> getPagos(String alumnoId, String cursoId) {
		String sql = "SELECT * FROM pago as p"
				+ " INNER JOIN inscripcion as i ON i.id = p.inscripcion_id "
				+ " WHERE i.alumno_id = ? AND i.curso_id = ?";
		return this.getDatabase().executeQueryPojo(PagoDTO.class, sql, alumnoId, cursoId);
	}

	public void registrarPago(String importe, String fecha, String idInscripcion) {
		String sql = "INSERT INTO pago (importe, fecha, inscripcion_id) VALUES(?,?,?)";
		this.getDatabase().executeUpdate(sql, importe, fecha, idInscripcion);
	}

	public String getEmailAlumno(String idAlumno) {
		String sql = "select email from alumno where id=?";
		return (String) this.getDatabase().executeQuerySingle(sql, idAlumno);
	}

	public void cancelarInscripcion(String idInscripcion) {
		String sql = "UPDATE inscripcion SET cancelada = TRUE WHERE id = ?";
		this.getDatabase().executeUpdate(sql, idInscripcion);
	}

	public String getFechaCurso(String idCurso) {
		String sql = "select start from curso where id=?";
		return getDatabase().executeQuerySingle(sql, idCurso).toString();
	}
}
