package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.util.Database;
import g41.si2022.util.Util;

public class InscribirUsuarioModel {

    private Database db = new Database();

    public List<CursoDTO> getListaCursos() {
        String sql =
                "select id, nombre, plazas, start_inscr, end_inscr"
                + " from curso order by id asc;";
        return db.executeQueryPojo(CursoDTO.class, sql);
    }

    public boolean verifyAlumnoEmail(String email) {
        return Util.verifyAlumnoEmail(db, email);
    }
}
