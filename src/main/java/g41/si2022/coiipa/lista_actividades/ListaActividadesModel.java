package g41.si2022.coiipa.lista_actividades;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.ProfesorDTO;
import g41.si2022.util.db.Database;

public class ListaActividadesModel extends g41.si2022.mvc.Model {

    private Database db = new Database();

    public List<CursoDTO> getListaCursos() {
        String sql = "SELECT * FROM curso";
        return db.executeQueryPojo(CursoDTO.class, sql);
    }

    public String getDescripcionCurso(String idCurso) {
        String sql = "SELECT descripcion FROM curso WHERE id = ?";
        return String.valueOf((String) db.executeQuerySingle(sql, idCurso));
    }

    public String getNumIscripciones(String idCurso) {
        String sql = "SELECT count(inscripcion.id) - count(inscripcioncancelada.id) from CURSO "
                   + "INNER JOIN inscripcion on curso.id = inscripcion.curso_id "
                   + "LEFT JOIN inscripcioncancelada ON inscripcioncancelada.inscripcion_id = inscripcion.id "
                   + "WHERE inscripcion.curso_id = ?";

        return String.valueOf((Integer) db.executeQuerySingle(sql, idCurso));
    }

    public List<ProfesorDTO> getDocentesCurso(String idCurso) {
        String sql = "select de.nombre, de.apellidos from docente as de "
                   + "inner join docencia as da on de.id = da.docente_id "
                   + "inner join curso as c on da.curso_id = c.id "
                   + "where c.id = ?";

        return db.executeQueryPojo(ProfesorDTO.class, sql, idCurso);
    }

    public String getLugarCurso(String idCurso) {
        String sql = "SELECT localizacion FROM curso WHERE curso.id = ?";
        return String.valueOf((String) db.executeQuerySingle(sql, idCurso));
    }
}
