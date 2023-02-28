package g41.si2022.coiipa.consultar_cursos;

import g41.si2022.util.Database;

public class ConsultarCursosModel {
	
	private Database db = new Database();
	
	public java.util.List<ConsultarCursosDTO> getListaCursos() {
		String sql =
				"SELECT nombre, estado, plazas, start_inscr, end_inscr"
				+ "FROM curso";
		return db.executeQueryPojo(ConsultarCursosDTO.class, sql);
	}
}
