package g41.SI2022.coiipa.registrarCurso;

import g41.SI2022.util.Database;

public class RegistrarCursoModel {

	private Database db = new Database();
	
	public java.util.List<ProfesorDisplayDTO> getListaProfesores () {
		String sql =
				"SELECT nombre, apellido, email, direccion "
				+ " FROM docente ORDER BY nombre";
		return db.executeQueryPojo(ProfesorDisplayDTO.class, sql);
	}

}
