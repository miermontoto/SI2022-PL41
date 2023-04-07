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
        String sql = "SELECT count(i.id) from CURSO as c "
                   + "INNER JOIN inscripcion as i on c.id = i.curso_id "
                   + "WHERE i.curso_id = ? AND i.cancelada = FALSE";

        return String.valueOf((Integer) getDatabase().executeQuerySingle(sql, idCurso));
    }

    public List<ProfesorDTO> getDocentesCurso(CursoDTO curso) {
        return getDocentesCurso(curso.getId());
    }

    public List<ProfesorDTO> getDocentesCurso(String idCurso) {
        String sql = "select de.nombre, de.apellidos, da.remuneracion from docente as de "
                   + "inner join docencia as da on de.id = da.docente_id "
                   + "inner join curso as c on da.curso_id = c.id "
                   + "where c.id = ?";

        return getDatabase().executeQueryPojo(ProfesorDTO.class, sql, idCurso);
    }

    /**
     * Devuelve un array con los eventos del curso "curso".
     * <p> Sobrecarga de {@link #getLugarCurso(String)}.
     * @param curso {@link EventoDTO} del curso
     * @return Array con los eventos del curso
     */
    public List<EventoDTO> getEventosCurso(CursoDTO curso) {
        return getEventosCurso(curso.getId());
    }

    public List<EventoDTO> getEventosCurso(String idCurso) {
        String sql = "SELECT *, hora_ini as horaIni, hora_fin as horaFin FROM evento WHERE curso_id = ?";
        return getDatabase().executeQueryPojo(EventoDTO.class, sql, idCurso);
    }
}
