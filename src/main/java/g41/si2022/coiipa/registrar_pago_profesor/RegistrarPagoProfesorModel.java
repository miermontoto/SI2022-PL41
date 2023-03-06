package g41.si2022.coiipa.registrar_pago_profesor;

import java.util.List;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.coiipa.dto.InscripcionDTO;
import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.util.Database;

public class RegistrarPagoProfesorModel {
	private Database db = new Database();

	public List<FacturaDTO> getListaFacturas(String today) {
		String sql = "select *, doc.importe from factura as f"
		+ " inner join docencia as doc on f.docencia_id = doc.id"
		+ " where f.fecha_introd <= ?";
		return db.executeQueryPojo(FacturaDTO.class, sql, today);
	}

	public List<FacturaDTO> getListaFacturasSinPagar(String today) {
		String sql = "select *, doc.importe from factura as f"
		+ " inner join docencia as doc on f.docencia_id = doc.id"
		+ " where f.fecha_introd <= ? and f.fecha_pago is null";
		return db.executeQueryPojo(FacturaDTO.class, sql, today);
	}
}
