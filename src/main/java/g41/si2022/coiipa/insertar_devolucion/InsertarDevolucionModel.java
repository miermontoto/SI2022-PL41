package g41.si2022.coiipa.insertar_devolucion;

import java.util.Date;
import java.util.List;

import g41.si2022.dto.CancelacionDTO;
import g41.si2022.util.Util;

public class InsertarDevolucionModel extends g41.si2022.mvc.Model {

	public List<CancelacionDTO> getListaInscripciones(Date fechaActual) {

		//validateNotNull(fechaInscripcion, MSG_FECHA_INSCRIPCION_NO_NULA);
		String sql =
				"select i.id, i.alumno_id, a.nombre, a.apellidos, c.coste, c.nombre as nombre_curso, c.start as inicio_curso"
				+ " from inscripcion as i inner join alumno as a ON i.alumno_id = a.id"
				+ " inner join curso as c on c.id = i.curso_id "
				+ " left join inscripcioncancelada as ic on i.id = ic.inscripcion_id "
				+ " where c.start>=? and "
				+ " ic.importedevuelto is NULL "
				+ " order by i.fecha asc";
		String d = Util.dateToIsoString(fechaActual);
		return this.getDatabase().executeQueryPojo(CancelacionDTO.class, sql, d); // Statement preparado.
	}

	public void registrarDevolucion(double importe, String fecha, int idInscripcion) {
		String sql = "INSERT INTO inscripcioncancelada (importedevuelto, fechacancelacion, inscripcion_id) VALUES(?,?,?)";
		this.getDatabase().executeUpdate(sql, importe, Util.isoStringToDate(fecha), idInscripcion);

	}

	public String getEmailAlumno(String idAlumno) {
		String sql = "select email from alumno where id=?";
		return (String) this.getDatabase().executeQueryArray(sql, idAlumno).get(0)[0];
	}

}
