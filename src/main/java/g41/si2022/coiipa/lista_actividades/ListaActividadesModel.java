package g41.si2022.coiipa.lista_actividades;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.ProfesorDTO;
import g41.si2022.util.Database;

public class ListaActividadesModel extends g41.si2022.mvc.Model {

    private Database db = new Database();

    public List<CursoDTO> getListaCursos()
    {
        String sql = "SELECT * FROM curso";

        return db.executeQueryPojo(CursoDTO.class, sql);
    }

    public String getDescripcionCurso(String idCurso)
    {
        String sql = "SELECT descripcion FROM curso WHERE id = ?";

        return String.valueOf((String) db.executeQueryArray(sql, idCurso).get(0)[0]);
    }

    public String getNumIscripciones(String idCurso)
    {
        String sql = "SELECT count(inscripcion.id) - count(inscripcioncancelada.id) from CURSO "
                   + "INNER JOIN inscripcion on curso.id = inscripcion.curso_id "
                   + "LEFT JOIN inscripcioncancelada ON inscripcioncancelada.inscripcion_id = inscripcion.id "
                   + "WHERE inscripcion.curso_id = ?";

        return String.valueOf((Integer) db.executeQueryArray(sql, idCurso).get(0)[0]);
    }

    public List<ProfesorDTO> getDocentesCurso(String idCurso)
    {
        String sql = "SELECT docente.nombre, docente.apellidos FROM curso "
                   + "INNER JOIN docente ON curso.id = docente.id "
                   + "WHERE curso.id = ?";

        return db.executeQueryPojo(ProfesorDTO.class, sql, idCurso);
    }

    public String getLugarCurso(String idCurso)
    {
        String sql = "SELECT localizacion FROM curso "
                   + "WHERE curso.id = ?";

        return String.valueOf((String) db.executeQueryArray(sql, idCurso).get(0)[0]);
    }

    // prueba
    // public String getNumIscripciones2(String idCurso)
    // {
    //     String sql = "SELECT count(inscripcion.id)"

    //     return String.valueOf((Integer) db.executeQueryArray(sql, idCurso).get(0)[0]);
    // }
}
