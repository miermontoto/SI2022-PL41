package g41.si2022.coiipa.gestionar_cursos;

import java.util.List;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.coiipa.dto.ProfesorDTO;

public class GestionarCursosModel extends g41.si2022.mvc.Model {

    public List<CursoDTO> getListaCursos() {
        String sql = "SELECT * FROM curso";

        return this.getDatabase().executeQueryPojo(CursoDTO.class, sql);
    }
    
    public String getDescripcionCurso(String idCurso) {
        String sql = "SELECT descripcion FROM curso WHERE id = ?";

        return String.valueOf((String) this.getDatabase().executeQueryArray(sql, idCurso).get(0)[0]);
    }

    public String getNumIscripciones(String idCurso) {
        String sql = "SELECT count(inscripcion.id) - count(inscripcioncancelada.id) from CURSO "
                   + "INNER JOIN inscripcion on curso.id = inscripcion.curso_id "
                   + "LEFT JOIN inscripcioncancelada ON inscripcioncancelada.inscripcion_id = inscripcion.id "
                   + "WHERE inscripcion.curso_id = ?";
                    
        return String.valueOf((Integer) this.getDatabase().executeQueryArray(sql, idCurso).get(0)[0]);
    }

    public List<ProfesorDTO> getDocentesCurso(String idCurso) {
        String sql = "SELECT docente.nombre, docente.apellidos FROM curso "
                   + "INNER JOIN docente ON curso.id = docente.id "
                   + "WHERE curso.id = ?";

        return this.getDatabase().executeQueryPojo(ProfesorDTO.class, sql, idCurso);
    }

    public String getLugarCurso(String idCurso) {
        String sql = "SELECT localizacion FROM curso "
                   + "WHERE curso.id = ?";
        
        return String.valueOf((String) this.getDatabase().executeQueryArray(sql, idCurso).get(0)[0]);
    }
}
