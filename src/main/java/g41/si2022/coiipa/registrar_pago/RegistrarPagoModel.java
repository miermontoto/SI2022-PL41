package g41.si2022.coiipa.registrar_pago;

import java.util.Date;
import java.util.List;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.coiipa.dto.InscripcionDTO;
import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.util.Database;
import g41.si2022.util.Util;

public class RegistrarPagoModel {
	private Database db = new Database();

	public List<InscripcionDTO> getInscripciones(String date) {
		String sql =
				"select i.id as inscripcion_id, "
				+ " i.alumno_id as inscripcion_alumno_id, "
				+ " a.nombre as alumno_nombre, "
				+ " c.coste as curso_coste, "
				+ " c.nombre as curso_nombre, "
				+ " i.fecha as inscripcion_fecha"
				+ " from inscripcion as i inner join alumno as a ON i.alumno_id = a.id"
				+ " inner join curso as c on c.id = i.curso_id where i.fecha<=? order by i.fecha asc";
		return db.executeQueryPojo(InscripcionDTO.class, sql, date);
	}
	
	public CursoDTO getCurso (String id) {
		String sql = 
				" SELECT * "
				+ " FROM curso "
				+ " WHERE curso.id = ?";
		return db.executeQueryPojo(CursoDTO.class, sql, id).get(0);
	}

	public List<PagoDTO> getPagos () {
		String sql =
				"SELECT * "
				+ " FROM pago "
				+ " INNER JOIN inscripcion ON inscripcion.id = pago.inscripcion_id ";
		return db.executeQueryPojo(PagoDTO.class, sql);
	}
	
	public List<PagoDTO> getPagos (String alumnoId, String cursoId) {
		String sql =
				"SELECT * "
				+ " FROM pago "
				+ " INNER JOIN inscripcion ON inscripcion.id = pago.inscripcion_id "
				+ " WHERE inscripcion.alumno_id = ? "
				+ " AND inscripcion.curso_id = ?";
		return db.executeQueryPojo(PagoDTO.class, sql, alumnoId, cursoId);
	}

	public void registrarPago(String importe, String fecha, String idInscripcion) {
		String sql = "INSERT INTO pago (importe, fecha, inscripcion_id) VALUES(?,?,?)";
		db.executeUpdate(sql, importe, fecha, idInscripcion);
	}

	public String getEmailAlumno(String idAlumno) {
		String sql = "select email from alumno where id=?";
		return (String) db.executeQueryArray(sql, idAlumno).get(0)[0];
	}
}
