package g41.si2022.coiipa.gestionar_facturas_profesores;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.DocenciaDTO;
import g41.si2022.dto.FacturaDTO;
import g41.si2022.ui.util.Dialog;

public class GestionarFacturasProfModel extends g41.si2022.mvc.Model {

	public List<FacturaDTO> getListaFacturas() {
		String sql = "select f.*, dca.remuneracion,"
		+ " dce.nombre as doc_nombre, dce.apellidos as doc_apellidos, c.nombre as curso_nombre,"
		+ " CASE WHEN sum(p.importe) IS NOT NULL THEN sum(p.importe) ELSE 0 END as pagado"
		+ " from factura as f"
		+ " inner join docencia as dca on f.docencia_id = dca.id"
		+ " inner join curso as c on dca.curso_id = c.id"
		+ " inner join docente as dce on dca.docente_id = dce.id"
		+ " left join pago as p on f.id = p.factura_id"
		+ " group by f.id order by f.fecha";
		return this.getDatabase().executeQueryPojo(FacturaDTO.class, sql);
	}

	public void insertPago(String fecha, String importe, String factura_id) {
		String sql = "insert into pago (fecha, importe, factura_id) values (?, ?, ?)";
		this.getDatabase().executeUpdate(sql, fecha, importe, factura_id);
	}

	public boolean insertFactura(String fecha, String docencia_id, String importe) {
		String sql;

		// Comprobar que la docencia no tiene ya una factura asociada (imposible por la view)
		/*sql = "select count(*) from factura as f where f.docencia_id = ?";
		if (!getDatabase().executeQuerySingle(sql, docencia_id).equals("0")) {
			Dialog.showError("La docencia ya tiene una factura asociada");
			return false;
		}*/

		// Comprobar que el importe introducido concuerda con lo acordado.
		sql = "select dca.remuneracion from docencia as dca where dca.id = ?";
		Double acordado = (double) getDatabase().executeQuerySingle(sql, docencia_id);
		Double insertado = Double.parseDouble(importe);
		if (!acordado.equals(insertado)) {
			Dialog.showError("El importe introducido no concuerda con lo acordado."
				+ "\nNo se ha introducido ningún dato en la base de datos.");
			return false;
		}

		// Insertar factura tras comprobar la validez del importe.
		sql = "insert into factura (fecha, docencia_id) values (?, ?)";
		this.getDatabase().executeUpdate(sql, fecha, docencia_id);
		Dialog.show("Factura creada correctamente");
		return true;
	}

	public List<CursoDTO> getListaCursos(String today) {
		String sql = "select * from curso as c where c.end < ? order by c.end desc";
		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql, today);
	}

    public List<DocenciaDTO> getListaDocentes(String idCurso) {
        String sql = "select da.*, de.nombre, de.apellidos from docencia as da"
		+ " inner join docente as de on de.id = da.docente_id"
		+ " where da.curso_id = ? and not exists (select * from factura as f where f.docencia_id = da.id)";
		return this.getDatabase().executeQueryPojo(DocenciaDTO.class, sql, idCurso);
    }

	public String getDocenciaId(String idCurso, String idDocente) {
		String sql = "select dca.id from docencia as dca"
		+ " where dca.curso_id = ? and dca.docente_id = ?";
		return this.getDatabase().executeQuerySingle(sql, idCurso, idDocente).toString();
	}
}
