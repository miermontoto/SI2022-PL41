package g41.si2022.coiipa.registrar_pago_profesor;

import java.util.List;

import g41.si2022.dto.FacturaDTO;

public class RegistrarPagoProfesorModel extends g41.si2022.mvc.Model {

	public List<FacturaDTO> getListaFacturas(String today) {
		String sql = "select f.*, dca.remuneracion,"
		+ " dce.nombre as doc_nombre, dce.apellidos as doc_apellidos, c.nombre as curso_nombre"
		+ " from factura as f"
		+ " inner join docencia as dca on f.docencia_id = dca.id"
		+ " inner join curso as c on dca.curso_id = c.id"
		+ " inner join docente as dce on dca.docente_id = dce.id"
		+ " where f.fecha_introd <= ? order by fecha_introd";
		return this.getDatabase().executeQueryPojo(FacturaDTO.class, sql, today);
	}

	public List<FacturaDTO> getListaFacturasSinPagar(String today) {
		String sql = "select f.*, dca.remuneracion,"
		+ " dce.nombre as doc_nombre, dce.apellidos as doc_apellidos, c.nombre as curso_nombre"
		+ " from factura as f"
		+ " inner join docencia as dca on f.docencia_id = dca.id"
		+ " inner join curso as c on dca.curso_id = c.id"
		+ " inner join docente as dce on dca.docente_id = dce.id"
		+ " where f.fecha_introd <= ? and f.fecha_pago = ''"
		+ " order by fecha_introd";
		return this.getDatabase().executeQueryPojo(FacturaDTO.class, sql, today);
	}

	public void updateFactura(String id, String date) {
		String sql = "update factura set fecha_pago = ? where id = ?";
		this.getDatabase().executeUpdate(sql, date, id);
	}
}
