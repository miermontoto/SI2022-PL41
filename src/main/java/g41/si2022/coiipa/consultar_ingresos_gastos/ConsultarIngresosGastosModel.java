package g41.si2022.coiipa.consultar_ingresos_gastos;

import g41.si2022.util.Database;
import g41.si2022.coiipa.dto.CursoDTO;

public class ConsultarIngresosGastosModel {

	private Database db = new Database();
	
	public java.util.List<CursoDTO> getCursosList () {
		String sql = 
				"SELECT * FROM curso ORDER BY curso.start_inscr";
		return db.executeQueryPojo(CursoDTO.class, sql);
	}
	
	public java.util.List<CursoDTO> getCursosBalance () {
		String sql = 
				"SELECT docencia.remuneracion as gastos, "
				+ " SUM(pago.importe) as ingresos "
				+ " ,docencia.remuneracion - SUM(pago.importe) as balance "
				+ " ,curso.nombre "
				+ " ,curso.start_inscr "
				+ " ,curso.end_inscr "
				+ " ,curso.start "
				+ " ,curso.end "
				+ " FROM curso "
				+ " LEFT JOIN docencia ON curso.id = docencia.curso_id " // Change for INNER JOIN. To do this, INSERT INTO docencia when creating a new course
				+ " LEFT JOIN inscripcion ON inscripcion.curso_id = curso.id "
				+ " LEFT JOIN pago ON pago.inscripcion_id = inscripcion.id "
				+ " GROUP BY (curso.id)";
		return db.executeQueryPojo(CursoDTO.class, sql);
	}

}
