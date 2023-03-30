package g41.si2022.coiipa.registrar_pago_profesor;

import java.util.List;

import g41.si2022.dto.FacturaDTO;

public class RegistrarPagoProfesorModel extends g41.si2022.mvc.Model {

	public List<FacturaDTO> getListaFacturas() {
		String sql = "select f.*, dca.remuneracion,"
		+ " dce.nombre as doc_nombre, dce.apellidos as doc_apellidos, c.nombre as curso_nombre"
		+ " from factura as f"
		+ " inner join docencia as dca on f.docencia_id = dca.id"
		+ " inner join curso as c on dca.curso_id = c.id"
		+ " inner join docente as dce on dca.docente_id = dce.id"
		+ " order by fecha";
		return this.getDatabase().executeQueryPojo(FacturaDTO.class, sql);
	}

	public List<FacturaDTO> getListaFacturasSinPagar() {
		String sql = "select f.*, dca.remuneracion,"
		+ " dce.nombre as doc_nombre, dce.apellidos as doc_apellidos, c.nombre as curso_nombre"
		+ " from factura as f"
		+ " inner join docencia as dca on f.docencia_id = dca.id"
		+ " inner join curso as c on dca.curso_id = c.id"
		+ " inner join docente as dce on dca.docente_id = dce.id"
		+ " inner join pago as p on f.id = p.factura_id"
		+ " where sum(p.importe) < dca.remuneracion"
		+ " order by f.fecha";
		return this.getDatabase().executeQueryPojo(FacturaDTO.class, sql);
	}
}
