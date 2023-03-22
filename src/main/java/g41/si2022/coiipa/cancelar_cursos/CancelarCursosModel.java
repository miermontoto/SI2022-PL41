package g41.si2022.coiipa.cancelar_cursos;

import java.util.List;

import g41.si2022.dto.CursoDTO;

public class CancelarCursosModel extends g41.si2022.mvc.Model {
    
    public List<CursoDTO> getListaCursos()
    {
        String sql ="SELECT * FROM curso";

        return getDatabase().executeQueryPojo(CursoDTO.class, sql);
    }

}
