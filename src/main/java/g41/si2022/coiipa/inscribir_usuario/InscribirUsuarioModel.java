package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.util.Database;

public class InscribirUsuarioModel {

    private Database db = new Database();

    public List<CursoDTO> getListaCursos() {
        String sql =
                "select nombre, plazas, start_inscr, end_inscr"
                + " from curso order by nombre asc;";
        return db.executeQueryPojo(CursoDTO.class, sql);
    }

}
