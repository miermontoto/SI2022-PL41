package g41.si2022.coiipa.gestionar_cursos;

import java.util.List;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.util.Database;

public class GestionarCursosModel {

    private Database db = new Database();

    public List<CursoDTO> getListaCursos() 
    {
        String sql = "SELECT * FROM curso";

        return db.executeQueryPojo(CursoDTO.class, sql);
    }

    public String getDescripcionCurso(String idCurso)
    {
        String sql = "SELECT descripcion FROM curso where id = ?";

        return String.valueOf(db.executeQueryArray(sql, idCurso).get(0)[0]);
    }
}
