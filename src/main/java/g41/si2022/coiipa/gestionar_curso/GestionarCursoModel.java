package g41.si2022.coiipa.gestionar_curso;

import g41.si2022.dto.CursoDTO;

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

}
