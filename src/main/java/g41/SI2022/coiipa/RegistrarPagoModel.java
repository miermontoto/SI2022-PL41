package g41.SI2022.coiipa;

import java.util.Date;
import java.util.List;

import giis.demo.tkrun.CarreraDisplayDTO;
import giis.demo.util.Database;
import giis.demo.util.Util;

public class RegistrarPagoModel {
	private Database db = new Database();

	public List<RegistrarPagoDTO> getListaInscripciones(Date fechaInscripcion) {

		// TODO: todo el modelo de la BBDD

		//validateNotNull(fechaInscripcion,MSG_FECHA_INSCRIPCION_NO_NULA);
		String sql =
				"SELECT insc.id , alu.nombre, insc.coste, insc.estado"
				+ " from inscripcion insc INNER JOIN alumno alu ON insc.alumno_id=alu.id  where insc.fecha>=? order by insc.fecha asc";
		String d = Util.dateToIsoString(fechaInscripcion);
		return db.executeQueryPojo(RegistrarPagoDTO.class, sql, d); //Statement preparado.
	}
}
