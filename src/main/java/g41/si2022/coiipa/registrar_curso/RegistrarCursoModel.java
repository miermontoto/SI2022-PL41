package g41.si2022.coiipa.registrar_curso;

import g41.si2022.coiipa.dto.ProfesorDTO;
import g41.si2022.util.Database;

import java.util.Date;

public class RegistrarCursoModel {

	private Database db = new Database();

	public java.util.List<ProfesorDTO> getListaProfesores () {
		String sql =
				"SELECT * "
				+ " FROM docente ORDER BY nombre";
		return db.executeQueryPojo(ProfesorDTO.class, sql);
	}
	
	public void insertCurso (
			String nombre, String descripcion,
			String inscrStart, String inscrEnd, String start, String end,
			int plazas, int docenteId
	) {
		System.out.println("Executing INSERT CURSO");
		String sql =
				"INSERT INTO curso (nombre, descripcion, start_inscr, end_inscr, start, end, plazas, docente_id, estado) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'INSCR_SIN_ABRIR');"; // INSCR_SIN_ABRIR may change later on
		db.executeUpdate(sql, 
			nombre, descripcion,
			inscrStart, inscrEnd, start, end,
			plazas, docenteId
		);
	}

}
