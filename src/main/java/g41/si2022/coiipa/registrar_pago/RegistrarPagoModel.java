package g41.si2022.coiipa.registrar_pago;

import java.util.Date;
import java.util.List;

import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.util.Database;
import g41.si2022.util.Util;

public class RegistrarPagoModel {
	private Database db = new Database();

	public List<PagoDTO> getInscripciones(String date) {
		String sql =
				"select i.id, i.alumno_id, a.nombre, c.coste, i.fecha, i.estado"
				+ " from inscripcion as i inner join alumno as a ON i.alumno_id = a.id"
				+ " inner join curso as c on c.id = i.curso_id where i.fecha<=? order by i.fecha asc";
		return db.executeQueryPojo(PagoDTO.class, sql, date);
	}

	public List<PagoDTO> getInscripcionesPendientes(String date) {
		String sql =
				"select i.id, i.alumno_id, a.nombre, c.coste, i.fecha, i.estado"
				+ " from inscripcion as i inner join alumno as a ON i.alumno_id = a.id"
				+ " inner join curso as c on c.id = i.curso_id where i.fecha<=? and i.estado!='Pagado' order by i.fecha asc";
		return db.executeQueryPojo(PagoDTO.class, sql, date);
	}

	public void registrarPago(String importe, String fecha, String idInscripcion) {
		String sql = "INSERT INTO pago (importe, fecha, inscripcion_id) VALUES(?,?,?)";
		db.executeUpdate(sql, importe, Util.isoStringToDate(fecha), idInscripcion);

		this.actualizarInscripcion(idInscripcion);
	}

	public void actualizarInscripcion(String id) {
		String sql = "UPDATE inscripcion SET estado=? WHERE id=?";
		db.executeUpdate(sql, "Pagado", id);
	}

	public String getEmailAlumno(String idAlumno) {
		String sql = "select email from alumno where id=?";
		return (String) db.executeQueryArray(sql, idAlumno).get(0)[0];
	}
}
