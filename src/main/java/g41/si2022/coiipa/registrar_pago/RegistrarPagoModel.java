package g41.si2022.coiipa.registrar_pago;

import java.util.Date;
import java.util.List;

import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.util.Database;
import g41.si2022.util.Util;

public class RegistrarPagoModel {
	private Database db = new Database();

	public List<PagoDTO> getListaInscripciones(Date fechaInscripcion) {

		// TODO: todo el modelo de la BBDD

		//validateNotNull(fechaInscripcion,MSG_FECHA_INSCRIPCION_NO_NULA);
		String sql =
				"SELECT alu.nombre , insc.coste, insc.fecha insc.estado"
				+ " from inscripcion insc INNER JOIN alumno alu ON insc.id_alumno = alu.id  where fecha>=? order by fecha asc";
		String d = Util.dateToIsoString(fechaInscripcion);
		return db.executeQueryPojo(PagoDTO.class, sql, d); //Statement preparado.
	}

	//Valores: importe del pago, fecha del pago y el idisncripción.
	public void registrarPago(int importe, String fecha, int idinscripcion) {
		String sql="INSERT INTO pago (importe, fecha, inscripcion_id) VALUES(?,?,?)";
		db.executeUpdate(sql, importe, Util.isoStringToDate(fecha), idinscripcion);
		this.actualizarInscripcion(idinscripcion);

	}

	public void actualizarInscripcion(int id) {
		String sql="UPDATE inscripcion SET estado=? WHERE id=?";
		db.executeUpdate(sql, "Pagado", id);
	}


}
