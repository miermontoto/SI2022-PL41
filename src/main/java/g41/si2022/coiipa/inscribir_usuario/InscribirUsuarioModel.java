package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;

import g41.si2022.coiipa.dto.AlumnoDTO;
import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.util.Database;
import g41.si2022.util.Util;

public class InscribirUsuarioModel {

    private Database db = new Database();

    public List<CursoDTO> getListaCursos(String date) {
        String sql = "select *, (c.plazas -"
        + " (select count(*) from inscripcion as i where i.curso_id = c.id)"
        //+ " (select count(*) from cancelada as ca where ca.curso_id = c.id)"
        + ") as plazas_libres from curso as c where start_inscr <= ? and end_inscr >= ?";
        return db.executeQueryPojo(CursoDTO.class, sql, date, date);
    }

    public List<CursoDTO> getListaCursos() {
        return db.executeQueryPojo(CursoDTO.class, "select * from curso");
    }

    public List<AlumnoDTO> getAlumnoFromEmail(String email) {
        String sql = "select id, nombre, apellidos, email, telefono"
            + " from alumno where email like ?;";
        return db.executeQueryPojo(AlumnoDTO.class, sql, email);
    }

    public boolean checkAlumnoInCurso(String alumno_id, String curso_id) {
        String sql = "select count(*) from inscripcion where alumno_id = ? and curso_id = ?";
        return db.executeQueryArray(sql, alumno_id, curso_id).get(0)[0].equals("0");
    }

    public void insertAlumno(String nombre, String apellidos, String email, String telefono) {
        String sql =
                "insert into alumno (nombre, apellidos, email, telefono)"
                + " values (?, ?, ?, ?);";
        db.executeUpdate(sql, nombre, apellidos, email, telefono);
    }

    public void insertInscripcion(String fecha, String curso_id, String alumno_id) {
        String sql = "insert into inscripcion (fecha, curso_id, alumno_id)"
            + " values (?, ?, ?)";
        db.executeUpdate(sql, fecha, curso_id, alumno_id);
    }

    public boolean verifyEmail(String email) {
        return Util.verifyEmailInAlumno(db, email);
    }
}
