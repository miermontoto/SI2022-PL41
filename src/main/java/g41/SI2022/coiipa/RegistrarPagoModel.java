package g41.SI2022.coiipa;

import java.util.Date;
import java.util.List;

import giis.demo.tkrun.CarreraDisplayDTO;
import giis.demo.util.Database;
import giis.demo.util.Util;

public class RegistrarPagoModel {
	private Database db = new Database();

	public List<CarreraDisplayDTO> getListaCursos(Date fechaInscripcion) {

		// TODO: todo el modelo de la BBDD

		//validateNotNull(fechaInscripcion,MSG_FECHA_INSCRIPCION_NO_NULA);
		String sql =
				"SELECT id,descr,"
				+ " case when ?<inicio then ''" // antes de inscripcion
				+ "   when ?<=fin then '(Abierta)'" // fase 1
				+ "   when ?<fecha then '(Abierta)'" // fase 2
				+ "   when ?=fecha then '(Abierta)'" // fase 3
				+ "   else '' " // despues de fin carrera
				+ " end as abierta"
				+ " from curso  where fecha>=? order by id";
		String d = Util.dateToIsoString(fechaInscripcion);
		return db.executeQueryPojo(CarreraDisplayDTO.class, sql, d, d, d, d, d);
	}
}
