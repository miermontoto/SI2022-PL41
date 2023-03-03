package g41.si2022.coiipa.consultar_cursos;

import java.util.List;

import g41.si2022.coiipa.dto.CCursoDTO;
import g41.si2022.coiipa.dto.InscripcionDTO;
import g41.si2022.util.Database;

public class ConsultarCursosModel {
	
	private Database db = new Database();
	private ConsultarCursosController cursoController;
	
	public List<CCursoDTO> getListaCursos() {
		String sql =
				"SELECT nombre, estado, plazas, start, end "
				+ "FROM curso";
				
		return db.executeQueryPojo(CCursoDTO.class, sql);
	}
	
	public List<InscripcionDTO> getListaInscr() {
		String sql = 
				"SELECT fecha, coste, estado "
				+ "FROM inscripcion where curso_id=" + cursoController.getIdCurso();
		
		return db.executeQueryPojo(InscripcionDTO.class, sql);
	}
}
