package g41.si2022.coiipa.insertar_devolucion;

import java.util.Date;
import java.util.List;

import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.coiipa.dto.cancelacionDTO;
import g41.si2022.util.Database;
import g41.si2022.util.Util;

public class InsertarDevolucionModel {
	private Database db = new Database();
	
	public List<cancelacionDTO> getListaInscripciones(Date fechaActual) {

		//validateNotNull(fechaInscripcion, MSG_FECHA_INSCRIPCION_NO_NULA);
		String sql =
				"select i.id, i.alumno_id, a.nombre, c.coste, c.nombre as nombre_curso, c.start as inicio_curso"
				+ " from inscripcion as i inner join alumno as a ON i.alumno_id = a.id"
				+ " inner join curso as c on c.id = i.curso_id where c.start>=? and i.estado='Pagado' order by i.fecha asc";
		String d = Util.dateToIsoString(fechaActual);
		return db.executeQueryPojo(cancelacionDTO.class, sql, d); // Statement preparado.
	}

	public void registrarPago(int importe, String fecha, int idInscripcion) {
		String sql = "INSERT INTO pago (importe, fecha, inscripcion_id) VALUES(?,?,?)";
		db.executeUpdate(sql, importe, Util.isoStringToDate(fecha), idInscripcion);

		this.actualizarInscripcion(idInscripcion);
	}

	public void actualizarInscripcion(int id) {
		String sql = "UPDATE inscripcion SET estado=? WHERE id=?";
		db.executeUpdate(sql, "Pagado", id);
	}
}