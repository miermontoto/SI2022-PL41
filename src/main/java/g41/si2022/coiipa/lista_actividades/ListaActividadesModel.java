package g41.si2022.coiipa.lista_actividades;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.EventoDTO;
import g41.si2022.dto.ProfesorDTO;

public class ListaActividadesModel extends g41.si2022.mvc.Model {

    public List<CursoDTO> getListaCursos() {
        String sql = "SELECT * FROM curso";
        return getDatabase().executeQueryPojo(CursoDTO.class, sql);
    }

    public String getDescripcionCurso(String idCurso) {
        String sql = "SELECT descripcion FROM curso WHERE id = ?";
        return String.valueOf((String) getDatabase().executeQuerySingle(sql, idCurso));
    }

    public String getNumInscripciones(String idCurso) {
        String sql = "SELECT count(inscripcion.id) - count(inscripcioncancelada.id) from CURSO "
                   + "INNER JOIN inscripcion on curso.id = inscripcion.curso_id "
                   + "LEFT JOIN inscripcioncancelada ON inscripcioncancelada.inscripcion_id = inscripcion.id "
                   + "WHERE inscripcion.curso_id = ?";

        return String.valueOf((Integer) getDatabase().executeQuerySingle(sql, idCurso));
    }

    public List<ProfesorDTO> getDocentesCurso(CursoDTO curso) {
        return getDocentesCurso(curso.getId());
    }

    public List<ProfesorDTO> getDocentesCurso(String idCurso) {
        String sql = "select de.nombre, de.apellidos from docente as de "
                   + "inner join docencia as da on de.id = da.docente_id "
                   + "inner join curso as c on da.curso_id = c.id "
                   + "where c.id = ?";

        return getDatabase().executeQueryPojo(ProfesorDTO.class, sql, idCurso);
    }

    /**
     * Devuelve un array con las localizaciones de la tabla "evento" del curso "curso".
     * <p> Sobrecarga de {@link #getLugarCurso(String)}.
     * @param curso DTO del curso
     * @return Array con las localizaciones del curso
     */
    public List<EventoDTO> getLugarCurso(CursoDTO curso) {
        return getLugarCurso(curso.getId());
    }

    /**
     * Devuelve un array con las localizaciones de la tabla "evento" del curso con id "idCurso".
     * @param idCurso Id del curso
     * @return Array con las localizaciones del curso
     */
    public List<EventoDTO> getLugarCurso(String idCurso) {
        String sql = "SELECT loc from evento WHERE curso_id = ?";
        return getDatabase().executeQueryPojo(EventoDTO.class, sql, idCurso);
    }
}
