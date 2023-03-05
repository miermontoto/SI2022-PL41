package g41.si2022.coiipa.registrar_curso;

import g41.si2022.coiipa.dto.ProfesorDTO;
import g41.si2022.util.Database;

public class RegistrarCursoModel {

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
			String plazas, String localizacion, String docenteId
			) {
		String sql =
				"INSERT INTO curso (nombre, descripcion, coste, start_inscr, end_inscr, plazas, start, end, localizacion, docente_id) "
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		db.executeUpdate(sql,
				nombre, descripcion, coste,
				inscrStart, inscrEnd, plazas, start, end, localizacion, docenteId
				);
	}
}
