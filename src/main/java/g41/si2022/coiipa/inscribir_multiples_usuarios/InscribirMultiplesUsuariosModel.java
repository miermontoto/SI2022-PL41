package g41.si2022.coiipa.inscribir_multiples_usuarios;

import java.util.List;

import g41.si2022.dto.AlumnoDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.GrupoDTO;
import g41.si2022.util.Util;

public class InscribirMultiplesUsuariosModel extends g41.si2022.mvc.Model {

	public List<CursoDTO> getListaCursos(String date) {
		String sql = "select *, (c.plazas -"
				+ " (select count(*) from inscripcion as i where i.curso_id = c.id)"
				//+ " (select count(*) from cancelada as ca where ca.curso_id = c.id)"
				+ ") as plazas_libres from curso as c where start_inscr <= ? and end_inscr >= ?";
		return this.getDatabase().executeQueryPojo(CursoDTO.class, sql, date, date);
	}

	public List<CursoDTO> getListaCursos() {
		return this.getDatabase().executeQueryPojo(CursoDTO.class, "select * from curso");
	}

	public List<GrupoDTO> getGrupoFromEmail(String email) {
		String sql = "select id, nombre, email, telefono"
				+ " from grupo where email like ?;";
		return this.getDatabase().executeQueryPojo(GrupoDTO.class, sql, email);
	}

	public List<AlumnoDTO> getAlumnoFromEmail(String email) {
		String sql = "select id, nombre, email, telefono"
				+ " from alumno where email like ?;";
		return this.getDatabase().executeQueryPojo(AlumnoDTO.class, sql, email);
	}

	/**
	 * getAlumnosFromEmails.
	 * <p>
	 * When a new alumno is signed up, their e-mail may or not be already registered in the DB.
	 * In the case that the e-mail is already registered, we need it returned.
	 * In the case that the e-mail is not registered, we register it and return it.
	 * </p>
	 * @param alumnos Data regarding the alumnos that should be registered
	 * @return List of all the Alumnos that are now in the DB.
	 */
	public List<AlumnoDTO> getAlumnosFromEmails (List<AlumnoDTO> alumnos) {
		String sql = "WITH new_entries AS ("
				+ "  SELECT '?' AS email, '?' AS nombre, '?' AS apellidos, '?' AS telefono ";
		for (AlumnoDTO alumno : alumnos.subList(1, alumnos.size())) 
			sql += "  UNION ALL SELECT '?' AS email, '?' AS nombre, '?' AS apellidos, '?' AS telefono ";
		sql += "),"
				+ "upserted_entries AS ( "
				+ "  INSERT INTO alumno (email, nombre, apellidos, telefono) "
				+ "  SELECT email, nombre, apellidos, telefono "
				+ "  FROM new_entries "
				+ "  ON CONFLICT (email) "
				+ "  DO NOTHING "
				+ "  RETURNING * "
				+ ") "
				+ "SELECT * FROM upserted_entries "
				+ "UNION ALL "
				+ "SELECT * FROM alumno "
				+ "WHERE email IN ( "
				+ "  SELECT email FROM new_entries "
				+ ");";
		String[] datos = new String[4 * alumnos.size()];
		for (int i = 0 ; i < alumnos.size() ; i++) {
			datos[i] = alumnos.get(i).getEmail();
			datos[i] = alumnos.get(i).getNombre();
			datos[i] = alumnos.get(i).getApellidos();
			datos[i] = alumnos.get(i).getTelefono();
		}
		return this.getDatabase().executeQueryPojo(AlumnoDTO.class, sql, (Object[]) datos);
	}

	public boolean checkAlumnoInCurso(String alumno_id, String curso_id) {
		String sql = "select count(*) from inscripcion where alumno_id = ? and curso_id = ?";
		return this.getDatabase().executeQuerySingle(sql, alumno_id, curso_id).equals("0");
	}

	public void insertGrupo(String nombre, String email, String telefono) {
		String sql =
				"insert into grupo (nombre, email, telefono)"
						+ " values (?, ?, ?);";
		this.getDatabase().executeUpdate(sql, nombre, email, telefono);
	}

	public List<AlumnoDTO> insertInscripciones(String fecha, String curso_id, List<String> alumno_id, String grupo_id) {
		// TODO: This query is incomplete. We need to link the curso_id and grupo_id to each VALUE.
		// The data is not in the alumno_id array. 
		String sql = "WITH new_values (id, nombre, apellidos, email, grupo_id, curso_id) AS ( "
				+ "    VALUES (?, ?, ?, ?, ?, ?) ";
		for (int i = 0 ; i < alumno_id.size()-1; i++) {
			sql += ", VALUES (?, ?, ?, ?, ?, ?) ";
		}
		sql += "), "
				+ "existing_values AS ( "
				+ "    SELECT * "
				+ "    FROM inscripcion "
				+ "    WHERE email IN (SELECT email FROM new_values) "
				+ ") "
				+ "INSERT INTO inscripcion (id, nombre, apellidos, email, telefono, grupo_id, curso_id) "
				+ "SELECT * "
				+ "FROM new_values "
				+ "WHERE NOT EXISTS ( "
				+ "    SELECT 1 FROM existing_values "
				+ "    WHERE existing_values.email = new_values.email "
				+ ") "
				+ "RETURNING *;";
		return this.getDatabase().executeQueryPojo(AlumnoDTO.class, sql, alumno_id.toArray());
	}

	public boolean verifyEmail(String email) {
		return Util.verifyEmailInGrupo(this.getDatabase(), email);
	}
}
