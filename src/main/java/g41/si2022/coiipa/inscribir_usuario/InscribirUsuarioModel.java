package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.util.Database;
import g41.si2022.util.Util;

public class InscribirUsuarioModel {

    private Database db = new Database();

    public List<CursoDTO> getListaCursos() {
        String sql = "select id, nombre, plazas, start_inscr, end_inscr"
            + " from curso order by id asc;";
        return db.executeQueryPojo(CursoDTO.class, sql);
    }

    public String getIdAlumno(String email) {
        String sql = "select id from alumno where email like ?";
        return (String) db.executeQueryArray(sql, email).get(0)[0];
    }

    public void insertAlumno(String nombre, String apellidos, String email, String telefono) {
        String sql =
                "insert into alumno (nombre, apellidos, email, telefono)"
                + " values (?, ?, ?, ?);";
        db.executeUpdate(sql, nombre, apellidos, email, telefono);
    }

    public void insertInscripcion(String cursoId, String email) {
    }

    public boolean verifyEmail(String email) {
        return Util.verifyEmailInAlumno(db, email);
    }
}
