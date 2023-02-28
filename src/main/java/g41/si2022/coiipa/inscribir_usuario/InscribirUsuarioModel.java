package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;

import g41.si2022.util.Database;

public class InscribirUsuarioModel {

    private Database db = new Database();

    public List<CursoDisplayDTO> getListaCursos() {
        String sql =
                "SELECT id, nombre, plazas, start_inscr, end_inscr"
                + " FROM curso ORDER BY id;";
        return db.executeQueryPojo(CursoDisplayDTO.class, sql);
    }

}
