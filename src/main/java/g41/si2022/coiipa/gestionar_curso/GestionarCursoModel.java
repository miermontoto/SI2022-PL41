package g41.si2022.coiipa.gestionar_curso;

import g41.si2022.dto.CursoDTO;

public class GestionarCursoModel extends g41.si2022.mvc.Model{
	
	public java.util.List<CursoDTO> getCursos () {
		String sql =
				"SELECT curso.id "
				+ " ,curso.nombre "
				+ " ,curso.start_inscr "
				+ " ,curso.end_inscr "
				+ " ,curso.start "
				+ " ,curso.end"
				+ " ,curso.plazas"
				+ " ,curso.plazas - count(inscripcion.id) as plazas_libres"
				+ " FROM curso "
				+ " LEFT JOIN docencia ON curso.id = docencia.curso_id " // Change for INNER JOIN. To do this, INSERT INTO docencia when creating a new course
				+ " LEFT JOIN inscripcion ON inscripcion.curso_id = curso.id "
				+ " LEFT JOIN pago ON pago.inscripcion_id = inscripcion.id "
				+ " GROUP BY (curso.id)";
		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql);
	}
	
	/*public void retrasarFechaCurso(String fecha, String idCurso) {
		
		String sql = "	UPDATE curso SET start = ? WHERE id= ?";
		this.getDatabase().executeUpdate(sql, fecha, idCurso);
		
	}*/
	
	public void updateFechas(int idCurso, String fechaCurso, String fechaFinCurso, String fechaInscripciones, String fechaFinInscripciones) {
		
		String sql = "	UPDATE curso SET start = ?, end = ? , start_inscr = ?, end_inscr = ? WHERE id= ?";
		
		this.getDatabase().executeUpdate(sql, fechaCurso, fechaFinCurso, fechaInscripciones, fechaFinInscripciones, idCurso);
		
		
	}

}
