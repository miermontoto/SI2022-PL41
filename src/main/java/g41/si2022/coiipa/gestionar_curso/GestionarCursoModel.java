package g41.si2022.coiipa.gestionar_curso;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;

import java.util.List;


public class GestionarCursoModel extends g41.si2022.mvc.Model {

	public java.util.List<CursoDTO> getCursos () {
		String sql = "SELECT c.id,"
			+ " c.nombre,"
			+ " c.start_inscr,"
			+ " c.end_inscr,"
			+ " c.start,"
			+ " c.end,"
			+ " c.plazas,"
			+ " (c.plazas -"
			+ " (select count(*) from inscripcion as i where i.curso_id = c.id and i.cancelada = 0)"
			+ " ) as plazas_libres"
			+ " FROM curso as c"
			+ " LEFT JOIN docencia as da ON c.id = da.curso_id "
			+ " LEFT JOIN inscripcion as i ON i.curso_id = c.id "
			+ " LEFT JOIN pago as p ON p.inscripcion_id = i.id "
			+ " GROUP BY (c.id)";
		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql);
	}

	public void updateFechas(int idCurso, String fechaCurso, String fechaFinCurso, String fechaInscripciones, String fechaFinInscripciones) {
		String sql = "UPDATE curso SET start = ?, end = ?, start_inscr = ?, end_inscr = ? WHERE id = ?";
		this.getDatabase().executeUpdate(sql, fechaCurso, fechaFinCurso, fechaInscripciones, fechaFinInscripciones, idCurso);
	}

	public void updateDetalles(int idCurso, String plazas) {
		String sql = "UPDATE curso SET plazas = ? WHERE id = ?";
		this.getDatabase().executeUpdate(sql, plazas, idCurso);
	}

	/**
	 * Gets all the attributes of a course stored in the database.
	 * 
	 * @param idCurso Id of the course to get all its attributes
	 * @return All the attributes of the specified course.
	 */
	public CursoDTO getCurso(String idCurso) {

		String sql = "SELECT * FROM curso where id = ?";

		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql, idCurso).get(0);
	}

    /** 
	 * Gets the value of the status attribute of the course table. By default this attribute is 'null'.
	 * In the class GestionarCursoController.java this attribute is modified to 'CANCELADO' in order to 
	 * perform course cancellations.
	 * 
	 * @param idCurso Id of the course to get its status stored in the database.
	 * @return The value of the specified course status attribute.
	 */
	public String getDBcursoState(String idCurso) {
		String sql = "SELECT estado FROM curso WHERE id = ?";

		return String.valueOf(this.getDatabase().executeQuerySingle(sql, idCurso));
	}

	/**
	 * Updates the status attribute of a course.
	 * 
	 * @param estado New final status of a course to be stored in the database. By default, CANCELADO.
	 * @param idCurso Course id to modify its status.
	 */
	public void updateCursoStateToCancelled(String estado, String idCurso) {
		String sql = "UPDATE curso SET estado = ? WHERE id = ?";

		this.getDatabase().executeUpdate(sql, estado, idCurso);
	}

	/**
	 * Gets the e-mails of the students enrolled in a course.
	 * 
	 * @param idCurso Id of the course to get e-mails.
	 * @return E-mails of the students registered in the course.
	 */
	public List<String> getAlumnosEmail(String idCurso) {
		String sql = "SELECT al.email FROM inscripcion as i " + 
					 "INNER JOIN alumno as al " +
					 "WHERE curso_id = ?";

		return this.getDatabase().executeQueryPojo(String.class, sql, idCurso);
	}
}
