package g41.si2022.coiipa.gestionar_inscripciones;

import java.time.LocalDate;
import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.dto.PagoDTO;
import g41.si2022.ui.util.Dialog;
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

	/**
	 * Método implementado para tests. Obtiene una inscripción dado su id
	 * 
	 * @param idInscr id de la inscripción
	 * @return inscripción correspondiente a la id pasada por parámetro
	 */
	public InscripcionDTO getInscrById(String idInscr) {
		String sql = "SELECT * FROM inscripcion WHERE id = ?";

		return this.getDatabase().executeQueryPojo(InscripcionDTO.class, sql, idInscr).get(0);
	}

	/**
	 * Método implementado para tests
	 * @param inscripcion inscripción a cancelar
	 * @return {@code true} si la inscripción se puede cancelar
	 * 		   {@code false} si la inscripción no se puede cancelar 
	 */
	public boolean cancelarInscripcionTest(InscripcionDTO inscripcion) {
		switch(inscripcion.getEstado()) {
			case PENDIENTE:
				Dialog.show("Inscripción con estado PENDIENTE. Se puede cancelar");
				return true;

			case EXCESO:
				Dialog.show("Inscripción con estado EXCESO. Se puede cancelar");
				return true;

			case PAGADA:
				Dialog.show("Inscripción con estado PAGADA. Se puede cancelar");
				return true;

			case RETRASADA:
				Dialog.show("Inscripción con estado RETRASADA. Se puede cancelar");
				return true;

			case RETRASADA_EXCESO:
				Dialog.show("Inscripción con estado RETRASADA_EXCESO. Se puede cancelar");
				return true;

			case EN_ESPERA:
				Dialog.show("Inscripción con estado EN_ESPERA. Se puede cancelar");
				return true;

			case CANCELADA:
				Dialog.showError("Inscripción ya cancelada");
				return false;

			default:
				// Nunca se debe llegar aquí
				return false;
		}
	}

	/**
	 * Obtener un curso mediante su id
	 * @param idCurso id del curso
	 * @return curso asociado a dicha id
	 */
	public CursoDTO getCursoFromInscr(String idCurso) {
		String sql = "SELECT * FROM curso WHERE id = ?";

		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql, idCurso).get(0);
	}

	public boolean cancelarInscripcionEstadoCursoTest(CursoDTO curso) {
		switch (curso.getEstado()) {
			case EN_INSCRIPCION:
				Dialog.show("Estado EN_INSCRIPCION. La inscrpción se puede cancelar");
				return true;

			case PLANEADO:
				Dialog.showError("Estado PLANEADO. No tiene inscripciones");
				return false;

			case EN_CURSO:
				Dialog.showError("Curso con estado EN_CURSO. La inscripción no se puede cancelar");
				return false;

			case FINALIZADO:
				return false;

			default:
				return false;
		}
	}
}
