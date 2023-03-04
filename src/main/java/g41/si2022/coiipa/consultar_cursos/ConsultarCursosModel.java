package g41.si2022.coiipa.consultar_cursos;

import java.util.List;


import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.coiipa.dto.DocenciaDTO;
import g41.si2022.coiipa.dto.InscripcionDTO;
import g41.si2022.util.Database;

public class ConsultarCursosModel {

	private Database db = new Database();

	public List<CursoDTO> getListaCursos() {

		return db.executeQueryPojo(CursoDTO.class, CursoDTO.getSqlQuery());
	}

	public List<InscripcionDTO> getListaInscr(String idCurso) {
		String sql = "SELECT * FROM inscripcion where curso_id = ?";

		return db.executeQueryPojo(InscripcionDTO.class, sql, idCurso);

	}

	public List<DocenciaDTO> getListaGastos(String idCurso) {
		String sql = "SELECT remuneracion from docencia where curso_id = ?";

		return db.executeQueryPojo(DocenciaDTO.class, sql, idCurso);
	}

	public double getIngresosEstimados(String idCurso) {
		String sql = "SELECT coste * plazas as ingrEst from curso where id = ?";

		return Double.parseDouble((String) db.executeQueryArray(sql, idCurso).get(0)[0]);
	} 
}
