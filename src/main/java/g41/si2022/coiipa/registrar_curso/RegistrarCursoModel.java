package g41.si2022.coiipa.registrar_curso;

import g41.si2022.util.Database;

public class RegistrarCursoModel {

	private Database db = new Database();

	public java.util.List<ProfesorDisplayDTO> getListaProfesores () {
		String sql =
				"SELECT nombre, apellidos, email, direccion "
				+ " FROM docente ORDER BY nombre";
		return db.executeQueryPojo(ProfesorDisplayDTO.class, sql);
	}

}
