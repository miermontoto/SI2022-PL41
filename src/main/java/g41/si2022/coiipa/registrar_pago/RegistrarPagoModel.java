package g41.si2022.coiipa.registrar_pago;

import java.util.Date;
import java.util.List;

import g41.si2022.util.Database;
import g41.si2022.util.Util;
import giis.demo.tkrun.CarreraEntity;

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
