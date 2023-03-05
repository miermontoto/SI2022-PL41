package g41.si2022.coiipa.consultar_cursos;

import java.util.List;


import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.coiipa.dto.DocenciaDTO;
import g41.si2022.coiipa.dto.InscripcionDTO;
import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.util.Database;

public class ConsultarCursosModel {

	private Database db = new Database();

	public List<CursoDTO> getListaCursos() {

		return db.executeQueryPojo(CursoDTO.class, CursoDTO.getSqlQuery());
	}

	public List<InscripcionDTO> getListaInscr(String idCurso) {
		String sql = "SELECT * FROM inscripcion WHERE curso_id = ?";

		return db.executeQueryPojo(InscripcionDTO.class, sql, idCurso);
	}

	public String getGastos(String idCurso) {
		String sql = "SELECT remuneracion FROM docencia WHERE curso_id = ?";

		try {
			return String.valueOf((int) db.executeQueryArray(sql, idCurso).get(0)[0]);
		} catch (IndexOutOfBoundsException ioob) {return "-";}
	}

	public String getCosteCurso(String idCurso) {
		String sql = "SELECT coste FROM curso where id = ?";

		return String.valueOf((double) db.executeQueryArray(sql, idCurso).get(0)[0]);
	}

	public String getIngresosEstimados(String idCurso) {
		String sql = "SELECT coste * plazas as ingrEst from curso WHERE id = ?";

		return String.valueOf((double) db.executeQueryArray(sql, idCurso).get(0)[0]);
	}

	public List<PagoDTO> getListaPagos(String idCurso) {
		String sql = "SELECT * FROM pago "
					 + "INNER JOIN inscripcion ON pago.inscripcion_id = inscripcion.id "
					 + "INNER JOIN curso ON inscripcion.curso_id = curso.id "
					 + "WHERE curso.id = ?" ;

		return db.executeQueryPojo(PagoDTO.class, sql, idCurso);
	}

	public String getImportePagos(String idCurso) {
		String sql = "SELECT sum(importe) FROM pago "
					 + "INNER JOIN inscripcion ON pago.inscripcion_id = inscripcion.id "
					 + "INNER JOIN curso ON inscripcion.curso_id = curso.id"
			 		 + "WHERE curso.id = ?" ;

		return String.valueOf((double) db.executeQueryArray(sql, idCurso).get(0)[0]);
	}
}
