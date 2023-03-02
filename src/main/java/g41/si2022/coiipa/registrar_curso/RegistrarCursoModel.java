package g41.si2022.coiipa.registrar_curso;

import g41.si2022.coiipa.dto.ProfesorDTO;
import g41.si2022.util.Database;

import java.util.Date;

public class RegistrarCursoModel {

	private Database db = new Database();

	public java.util.List<ProfesorDTO> getListaProfesores () {
		String sql =
				"SELECT nombre, apellidos, email, direccion "
				+ " FROM docente ORDER BY nombre";
		return db.executeQueryPojo(ProfesorDTO.class, sql);
	}
	
	public void insertCurso (
			String nombre, String descripcion,
			Date inscrStart, Date inscrEnd, Date start, Date end,
			int plazas, int docenteId
	) {
		String sql =
				"INSERT INTO curso (nombre, descripcion, inscr_start, inscr_end, start, end, plazas, docente_id) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		db.executeUpdate(sql, 
			nombre, descripcion,
			inscrStart, inscrEnd, start, end,
			plazas, docenteId
		);
	}

}
