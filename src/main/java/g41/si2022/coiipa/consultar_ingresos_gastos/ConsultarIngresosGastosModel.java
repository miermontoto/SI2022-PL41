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
}
