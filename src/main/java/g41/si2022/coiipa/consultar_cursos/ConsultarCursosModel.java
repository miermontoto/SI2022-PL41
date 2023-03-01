package g41.si2022.coiipa.consultar_cursos;

import java.util.List;

import g41.si2022.util.Database;

public class ConsultarCursosModel {
	
	private Database db = new Database();
	
	public List<ConsultarCursosDTO> getListaCursos() {
		String sql =
				"SELECT nombre, estado, plazas, start, end"
				+ "FROM curso";
		return db.executeQueryPojo(ConsultarCursosDTO.class, sql);
	}
}
