package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;
import java.util.function.BiConsumer;

import g41.si2022.dto.AlumnoDTO;
import g41.si2022.dto.ColectivoDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.util.Util;

public class InscribirUsuarioModel extends g41.si2022.mvc.Model {

    public List<CursoDTO> getListaCursos(String date) {
        String sql = "select *, (c.plazas -"
        + " (select count(*) from inscripcion as i where i.curso_id = c.id and i.cancelada = 0)"
        + ") as plazas_libres from curso as c where start_inscr <= ? and end_inscr >= ?";
        return this.getDatabase().executeQueryPojo(CursoDTO.class, sql, date, date);
    }

    public List<CursoDTO> getListaCursos() {
        return this.getDatabase().executeQueryPojo(CursoDTO.class, "select * from curso");
    }

    public List<String> getColectivosFromCursos(List<String> idCursos) {
    	String sql = "SELECT c.nombre FROM colectivo AS c";
    	return this.getDatabase()
            .executeQueryPojo(ColectivoDTO.class, sql)
            .stream().collect(new g41.si2022.util.HalfwayListCollector<ColectivoDTO, String>() {
                @Override
                public BiConsumer<List<String>, ColectivoDTO> accumulator() {
                    return (list, data) -> list.add(data.getNombre());
                }
        });
    }

    public List<ColectivoDTO> getColectivos() {
        String sql = "select * from colectivo";
        return getDatabase().executeQueryPojo(ColectivoDTO.class, sql);
    }

    public AlumnoDTO getAlumnoFromEmail(String email) {
        String sql = "select id, nombre, apellidos, email, telefono"
            + " from alumno where email like ?";
        return this.getDatabase().executeQueryPojo(AlumnoDTO.class, sql, email).get(0);
    }

    /**
     * Método que comprueba si un alumno está ya inscrito en un curso
     * en base a su email.
     * @param alumno_id
     * @param curso_id
     * @return Valor booleano: true si ya está inscrito.
     */
    public boolean checkAlumnoInCurso(String alumno_id, String curso_id) {
        String sql = "select count(*) from inscripcion where alumno_id = ? and curso_id = ?";
        String result = getDatabase().executeQuerySingle(sql, alumno_id, curso_id).toString();
        return !result.equals("0");
    }

    public void insertAlumno(String nombre, String apellidos, String email, String telefono) {
        String sql = "insert into alumno (nombre, apellidos, email, telefono)"
            + " values (?, ?, ?, ?);";
        this.getDatabase().executeUpdate(sql, nombre, apellidos, email, telefono);
    }

    public void insertInscripcion(String fecha, String curso_id, String alumno_id, String coste_id) {
        String sql = "insert into inscripcion (curso_id, alumno_id, fecha, cancelada, coste_id)"
            + " values (?, ?, ?, 0, ?)";
        this.getDatabase().executeUpdate(sql, curso_id, alumno_id, fecha, coste_id);
    }

    public boolean verifyEmail(String email) {
        return Util.verifyEmailInAlumno(this.getDatabase(), email);
    }

    public void insertListaEspera(String inscripcion_id, String fecha_entrada) {
        String sql = "insert into lista_espera (inscripcion_id, fecha_entrada)"
            + " values (?, ?)";
        this.getDatabase().executeUpdate(sql, inscripcion_id, fecha_entrada);
    }

    public InscripcionDTO getIdInscripcionFromCursoAlumno(String alumno_id, String curso_id) {
        String sql = "select id"
            + " from inscripcion where alumno_id = ? and curso_id = ?";
        return this.getDatabase().executeQueryPojo(InscripcionDTO.class, sql, alumno_id, curso_id).get(0);
    }

}
