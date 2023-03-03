package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;

import g41.si2022.coiipa.dto.AlumnoDTO;
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

    public List<AlumnoDTO> getAlumnoFromEmail(String email) {
        String sql = "select id, nombre, apellidos, email, telefono"
            + " from alumno where email like ?;";
        return db.executeQueryPojo(AlumnoDTO.class, sql, email);
    }

    public void insertAlumno(String nombre, String apellidos, String email, String telefono) {
        String sql =
                "insert into alumno (nombre, apellidos, email, telefono)"
                + " values (?, ?, ?, ?);";
        db.executeUpdate(sql, nombre, apellidos, email, telefono);
    }

    public void insertInscripcion(String fecha, String estado, String curso_id, String alumno_id) {
        String sql = "insert into inscripcion (fecha, estado, curso_id, alumno_id)"
            + " values (?, ?, ?, ?)";
        db.executeUpdate(sql, fecha, estado, curso_id, alumno_id);
    }

    public boolean verifyEmail(String email) {
        return Util.verifyEmailInAlumno(db, email);
    }
}
