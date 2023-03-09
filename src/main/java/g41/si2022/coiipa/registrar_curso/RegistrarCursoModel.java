package g41.si2022.coiipa.registrar_curso;

import g41.si2022.dto.ProfesorDTO;
import g41.si2022.util.db.Database;

public class RegistrarCursoModel extends g41.si2022.mvc.Model {

	private Database db = new Database();

	public java.util.List<ProfesorDTO> getListaProfesores () {
		String sql =
				"SELECT * "
						+ " FROM docente ORDER BY nombre";
		return db.executeQueryPojo(ProfesorDTO.class, sql);
	}

	public void insertCurso (
			String nombre, String descripcion, String coste,
			String inscrStart, String inscrEnd, String start, String end,
			String plazas, String localizacion, String docenteId, String remuneracion
			) {
		String sql =
				"INSERT INTO curso (nombre, descripcion, coste, start_inscr, end_inscr, plazas, start, end, localizacion, docente_id) "
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		db.executeUpdate(sql,
				nombre, descripcion, coste,
				inscrStart, inscrEnd, plazas, start, end, localizacion, docenteId
				);
		sql =
				"SELECT curso.id FROM curso ORDER BY curso.id DESC LIMIT(1)";
		this.insertDocencia(remuneracion, docenteId,
				db.executeQueryPojo(g41.si2022.dto.CursoDTO.class, sql).get(0).getId());
	}

	public void insertDocencia (
			String remuneracion, String profesor_id, String curso_id
			) {
		String sql =
				"INSERT INTO docencia (remuneracion, docente_id, curso_id) "
						+ " VALUES (?, ?, ?)";
		db.executeUpdate(sql,
				remuneracion, profesor_id, curso_id);
	}
}
