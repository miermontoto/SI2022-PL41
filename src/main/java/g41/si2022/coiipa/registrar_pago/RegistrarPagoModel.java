package g41.si2022.coiipa.registrar_pago;

import java.util.Date;
import java.util.List;

import g41.si2022.util.Database;
import g41.si2022.util.Util;

public class RegistrarPagoModel {
	private Database db = new Database();

	public List<InsertarPagoDTO> getListaInscripciones(Date fechaInscripcion) {

		// TODO: todo el modelo de la BBDD

		//validateNotNull(fechaInscripcion,MSG_FECHA_INSCRIPCION_NO_NULA);
		String sql =
				"SELECT id ,coste, estado"
				+ " from inscripcion  where fecha>=? order by fecha asc";
		String d = Util.dateToIsoString(fechaInscripcion);
		return db.executeQueryPojo(InsertarPagoDTO.class, sql, d); //Statement preparado.
	}
}