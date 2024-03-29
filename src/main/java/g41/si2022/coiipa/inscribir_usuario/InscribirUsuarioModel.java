package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;
import java.util.Optional;

import g41.si2022.dto.AlumnoDTO;
import g41.si2022.dto.ColectivoDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.util.Util;

public class InscribirUsuarioModel extends g41.si2022.mvc.Model {

    public List<CursoDTO> getListaCursos(String date) {
        String sql = "select c.*, (c.plazas -"
        + " (select count(*) from inscripcion as i where i.curso_id = c.id and i.cancelada = 0)"
        + ") as plazas_libres from curso as c where start_inscr <= ? and end_inscr >= ?";
        return this.getDatabase().executeQueryPojo(CursoDTO.class, sql, date, date);
    }

    public List<AlumnoDTO> getAlumnosInCurso(String idCurso) {
        String sql = "select a.* from alumno as a"
        + " inner join inscripcion as i on i.alumno_id = a.id"
        + " inner join curso as c on i.curso_id = c.id"
        + " where c.id = ?";
        return this.getDatabase().executeQueryPojo(AlumnoDTO.class, sql, idCurso);
    }

    public List<CursoDTO> getListaCursos() {
        return this.getDatabase().executeQueryPojo(CursoDTO.class, "select * from curso");
    }

    public List<ColectivoDTO> getColectivos(String cursoId) {
        return this.getDatabase().executeQueryPojo(ColectivoDTO.class,
                "SELECT colectivo.*, coste.coste as coste "
                + "FROM colectivo "
                + "INNER JOIN coste ON colectivo.id = coste.colectivo_id "
                + "INNER JOIN curso ON curso.id = coste.curso_id "
                + "WHERE curso.id = ?",
                cursoId);
    }

    public String getCostes(String idCurso, String nombreColectivo) {
		String sql = "SELECT coste.id "
				+ "FROM coste "
				+ "INNER JOIN colectivo ON coste.colectivo_id = colectivo.id "
				+ "WHERE curso_id = ? AND colectivo.nombre = ?";
		return this.getDatabase().executeQuerySingle(sql, idCurso, nombreColectivo).toString();
	}

    public Optional<AlumnoDTO> getAlumnoFromEmail(String email) {
        String sql = "select alumno.*"
            + " from alumno where email like ?";
        List<AlumnoDTO> list = this.getDatabase().executeQueryPojo(AlumnoDTO.class, sql, email);
        return Optional.ofNullable(list.isEmpty() ? null : list.get(0));
    }

    /**
     * Método que comprueba si un alumno está ya inscrito en un curso
     * en base a su email.
     * @param idAlumno
     * @param idCurso
     * @return Valor booleano: true si ya está inscrito.
     */
    public boolean checkAlumnoInCurso(String idAlumno, String idCurso) {
        String sql = "select count(*) from inscripcion where alumno_id = ? and curso_id = ?";
        String result = getDatabase().executeQuerySingle(sql, idAlumno, idCurso).toString();
        return !result.equals("0");
    }

    public void insertAlumno(String nombre, String apellidos, String email, String telefono) {
        String sql = "insert into alumno (nombre, apellidos, email, telefono)"
            + " values (?, ?, ?, ?);";
        this.getDatabase().executeUpdate(sql, nombre, apellidos, email, telefono);
    }

    public void insertInscripcion(String fecha, String idCurso, String idAlumno, String idCoste) {
        String sql = "insert into inscripcion (curso_id, alumno_id, fecha, cancelada, coste_id)"
            + " values (?, ?, ?, 0, ?)";
        this.getDatabase().executeUpdate(sql, idCurso, idAlumno, fecha, idCoste);
    }

    public boolean verifyEmail(String email) {
        return Util.verifyEmailInAlumno(this.getDatabase(), email);
    }

    public void insertListaEspera(String idInscripcion, String fechaEntrada) {
        String sql = "insert into lista_espera (inscripcion_id, fecha_entrada)"
            + " values (?, ?)";
        this.getDatabase().executeUpdate(sql, idInscripcion, fechaEntrada);
    }

    public InscripcionDTO getIdInscripcionFromCursoAlumno(String idAlumno, String idCurso) {
        String sql = "select id"
            + " from inscripcion where alumno_id = ? and curso_id = ?";
        return this.getDatabase().executeQueryPojo(InscripcionDTO.class, sql, idAlumno, idCurso).get(0);
    }

}
