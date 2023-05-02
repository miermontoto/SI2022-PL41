package g41.si2022.coiipa.gestionar_lista_espera;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.DocenciaDTO;
import g41.si2022.dto.ListaEsperaDTO;

public class GestionarListaEsperaModel extends g41.si2022.mvc.Model {

	public List<ListaEsperaDTO> getListaEspera(String cursoID) {
		String sql = "SELECT le.id, le.fecha_entrada, alu.nombre, alu.apellidos, le.inscripcion_id FROM lista_espera as le"
			+ " INNER JOIN inscripcion insc ON le.inscripcion_id = insc.id"
			+ " INNER JOIN alumno alu ON insc.alumno_id = alu.id"
			+ " INNER JOIN curso cu ON insc.curso_id = cu.id"
			+ " WHERE cu.id = ?";
		return this.getDatabase().executeQueryPojo(ListaEsperaDTO.class, sql, cursoID);
	}

	public void insertPago(String fecha, String importe, String factura_id) {
		String sql = "insert into pago (fecha, importe, factura_id) values (?, ?, ?)";
		this.getDatabase().executeUpdate(sql, fecha, importe, factura_id);
	}

	public void insertFactura(String fecha, String docencia_id) {
		String sql = "insert into factura (fecha, docencia_id) values (?, ?)";
		this.getDatabase().executeUpdate(sql, fecha, docencia_id);
	}

	public List<CursoDTO> getListaCursos(String today) {
		String sql = "select * from curso as c where c.end > ? order by c.end desc";
		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql, today);
	}

	public List<CursoDTO> getListaCursosConEspera(String today) {
		String sql = "select DISTINCT(c.nombre) from curso c "
				+ "INNER JOIN inscripcion insc on insc.curso_id = c.id "
				+ "INNER JOIN lista_espera li  ON li.inscripcion_id = insc.id "
				+ "where c.end > ? order by c.end desc";

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

	public String getCursoId(String nombreCurso) {
		String sql = "SELECT id FROM curso WHERE nombre = ?";
		return this.getDatabase().executeQuerySingle(sql, nombreCurso).toString();
	}

	public void eliminarListaEspera(String idLista) {
		String sql = "DELETE FROM lista_espera WHERE id = ?";
		this.getDatabase().executeUpdate(sql, idLista);

	}

	public void eliminarInscripcion(String idLista) {

		String sql = "";
		String idInscripcion = "";
		sql = "SELECT inscripcion_id FROM lista_espera"
				+ " WHERE id = ?";

		if(this.getDatabase().executeQuerySingle(sql, idLista) != null)
			idInscripcion = this.getDatabase().executeQuerySingle(sql, idLista).toString();

		sql = "DELETE FROM inscripcion WHERE id = ?";
		this.getDatabase().executeUpdate(sql, idInscripcion);

		eliminarListaEspera(idLista);

	}
}
