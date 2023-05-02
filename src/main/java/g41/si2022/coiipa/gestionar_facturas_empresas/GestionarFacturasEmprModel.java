package g41.si2022.coiipa.gestionar_facturas_empresas;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.FacturaDTO;
import g41.si2022.ui.util.Dialog;


public class GestionarFacturasEmprModel extends g41.si2022.mvc.Model {


	/**
	 * Obtain unpaid bills from companies
	 * 
	 * @return list of bills
	 */
	public List<FacturaDTO> getListaFacturasEmpr() {
		String sql = "SELECT f.*, c.nombre AS curso_nombre, c.importe AS remuneracion, e.nombre AS nombre_entidad," 
				   + " CASE WHEN sum(p.importe) IS NOT NULL THEN sum(p.importe) ELSE 0 END AS pagado"
				   + " FROM factura AS f"
				   + " INNER JOIN curso AS c ON f.curso_id = c.id"
				   + " INNER JOIN entidad AS e ON c.entidad_id = e.id"
				   + " LEFT JOIN pago AS p ON f.id = p.factura_id"
				   + " GROUP BY f.id ORDER BY f.fecha";

		return this.getDatabase().executeQueryPojo(FacturaDTO.class, sql);
	}



	public void insertPago(String fecha, String importe, String factura_id) {
		String sql = "insert into pago (fecha, importe, factura_id) values (?, ?, ?)";

		this.getDatabase().executeUpdate(sql, fecha, importe, factura_id);
	}


	/**
	 * Insert a new bill from a company to pay for the teaching of a course
	 * 
	 * @param fecha date of the bill
	 * @param curso_id id of the course associated to the company
	 * @param entidad_id id of the company
	 * @param importe amount to be paid
	 * 
	 * @return {@code true} if the bill is inserted correctly
	 * 		   {@code false} if the amount of money to pay does not match the agreed 
	 */
	public boolean insertFacturaEmpresa(String fecha, String curso_id, String entidad_id, String importe) {
		String sql;

		// Comprobar la validez del importe
		sql = "SELECT c.importe as remuneracion FROM curso AS c"
		    + " WHERE c.entidad_id = ?";
		Double acordado = Double.parseDouble(getDatabase().executeQuerySingle(sql, entidad_id).toString()) ;
		Double insertado = Double.parseDouble(importe);

		if (!acordado.equals(insertado)) {
			Dialog.showError("El importe introducido no concuerda con lo acordado."
							+ "\nOperación cancelada.");

			return false;
		}

		// Insertar factura una vez realizada la comprobación
		sql = "INSERT INTO factura(fecha, curso_id) VALUES (?, ?)";
		this.getDatabase().executeUpdate(sql, fecha, curso_id);
		Dialog.show("Factura registrada correctamente");

		return true;
	}

	/**
	 * Obtain a list of courses associated with a company
	 * 
	 * @return list of courses
	 */
	public List<CursoDTO> getListaCursosEmpr() {
		String sql = "SELECT c.*, e.nombre as e_nombre FROM curso as c"
				   + " INNER JOIN entidad as e"
				   + " ON c.entidad_id = e.id";

		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql);
	}
}
