package g41.SI2022.coiipa.registrarPago;

import java.util.Date;
import java.util.List;

import giis.demo.tkrun.CarreraDisplayDTO;
import giis.demo.util.Database;
import giis.demo.util.Util;

public class RegistrarPagoModel {
	private Database db = new Database();

	public List<insertapagoDTO> getListaInscripciones(Date fechaInscripcion) {

		// TODO: todo el modelo de la BBDD

		//validateNotNull(fechaInscripcion,MSG_FECHA_INSCRIPCION_NO_NULA);
		String sql =
				"SELECT id ,coste, estado"
				+ " from inscripcion  where fecha>=? order by fecha asc";
		String d = Util.dateToIsoString(fechaInscripcion);
		return db.executeQueryPojo(insertapagoDTO.class, sql, d); //Statement preparado.
	}
}
