package g41.si2022.coiipa.consultar_ingresos_gastos;

import java.util.List;

import g41.si2022.dto.CursoDTO;

public class ConsultarIngresosGastosModel extends g41.si2022.mvc.Model {

	/**
	 * Returns a big data set, containing:
	 * - Gastos (docencia.remuneracion) // TODO: This will be later changed with some sort of SUM()
	 * - Ingresos (SUM(pago.importe))
	 * - Ingresos - Gastos
	 * - curso.nombre
	 * - curso.start_inscr
	 * - curso.end_inscr
	 * - curso.start
	 * - curso.end
	 *
	 * @return List <html>&gt;</html>CursoDTO&<html>&gt;</html> containing all the data needed to display the JTable
	 */
	public java.util.List<CursoDTO> getCursosBalance() {
		String sql =
				"SELECT docencia.remuneracion as gastos, "
				+ " SUM(pago.importe) as ingresos "
				+ " ,docencia.remuneracion - SUM(pago.importe) as balance "
				+ " ,curso.nombre "
				+ " ,curso.start_inscr "
				+ " ,curso.end_inscr "
				+ " ,curso.start "
				+ " ,curso.end"
				+ " ,MAX(pago.fecha) as pagoHighestFecha "
				+ " ,MIN(pago.fecha) as pagoLowestFecha "
				+ " FROM curso "
				+ " LEFT JOIN docencia ON curso.id = docencia.curso_id " // Change for INNER JOIN. To do this, INSERT INTO docencia when creating a new course
				+ " LEFT JOIN inscripcion ON inscripcion.curso_id = curso.id "
				+ " LEFT JOIN pago ON pago.inscripcion_id = inscripcion.id "
				+ " GROUP BY (curso.id)";
		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql);
	}

	public List<CursoDTO> getCursos() {
		String sql = "select c.*, sum(dca.remuneracion) as gastos, "
			+ " sum(p.importe) as ingresos, "
			+ " sum(p.importe) - sum(dca.remuneracion) as balance, "
			+ " max(p.fecha) as pagoHighestFecha, "
			+ " min(p.fecha) as pagoLowestFecha "
			+ " from curso as c "
			+ " left join docencia as dca on dca.curso_id = c.id "
			+ " left join inscripcion as i on i.curso_id = c.id "
			+ " left join pago as p on p.inscripcion_id = i.id "
			+ " group by c.id";
		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql);
	}

}
