package g41.si2022.coiipa.registrar_curso;

import g41.si2022.dto.ProfesorDTO;

public class RegistrarCursoModel extends g41.si2022.mvc.Model {

	public java.util.List<ProfesorDTO> getListaProfesores() {
		String sql = "SELECT * FROM docente ORDER BY nombre";
		return this.getDatabase().executeQueryPojo(ProfesorDTO.class, sql);
	}

	/**
	 * Inserta un curso en la base de datos.
	 * @param nombre Nombre del curso
	 * @param descripcion Descripcion del curso
	 * @param coste Coste de la inscripción del curso
	 * @param inscrStart Fecha de inicio de inscripción
	 * @param inscrEnd Fecha de fin de inscripción
	 * @param start Fecha de inicio del curso
	 * @param end Fecha de fin del curso
	 * @param plazas Cantidad de plazas disponibles
	 * @param localizacion Localización del curso
	 * @return El ID del curso insertado
	 */
	public String insertCurso(
			String nombre, String descripcion, String coste,
			String inscrStart, String inscrEnd, String start, String end,
			String plazas) {
		String sql = "INSERT INTO curso (nombre, descripcion, coste, start_inscr, end_inscr, plazas, start, end) "
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		this.getDatabase().executeUpdate(sql,
				nombre, descripcion, coste,
				inscrStart, inscrEnd, plazas, start, end);
		//return getDatabase().getLastInsertedId();
		return String.valueOf(this.getDatabase().executeQuerySingle("select id from curso"
		 + " where nombre = ? and start_inscr = ? and start = ? and coste = ? and plazas = ?",
		  nombre, inscrStart, start, coste, plazas));
	}

	public void insertDocencia (String remuneracion, String profesor_id, String curso_id) {
		String sql = "INSERT INTO docencia (remuneracion, docente_id, curso_id) VALUES (?, ?, ?)";
		this.getDatabase().executeUpdate(sql, remuneracion, profesor_id, curso_id);
	}
}
