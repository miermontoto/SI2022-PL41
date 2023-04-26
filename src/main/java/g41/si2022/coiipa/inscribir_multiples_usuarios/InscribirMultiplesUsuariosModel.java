package g41.si2022.coiipa.inscribir_multiples_usuarios;

import java.util.List;
import java.util.function.BiConsumer;

import g41.si2022.dto.AlumnoDTO;
import g41.si2022.dto.ColectivoDTO;
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
		String sql = "select * from entidad where email like ?;";
		return this.getDatabase().executeQueryPojo(GrupoDTO.class, sql, email);
	}

	public List<AlumnoDTO> getAlumnoFromEmail(String email) {
		String sql = "select id, nombre, email, telefono"
				+ " from alumno where email like ?;";
		return this.getDatabase().executeQueryPojo(AlumnoDTO.class, sql, email);
	}

	/**
	 * getAlumnosFromEmails.
	 * This function will add all non already existing alumnos in the DB.
	 * Then it will return all the IDs of all the alumnos in the array
	 * no matter whether they were already in the DB or not.
	 *
	 * @param IDs of the alumnos that have been registered
	 */
	public List<String> insertAlumnos (List<AlumnoDTO> alumnos) {
		this.insertMissingAlumnos(alumnos);
		return getAlumnosIDsFromEmails (alumnos);
	}

	/**
	 * insertMissingAlumnos.
	 * This function will add all non already existing alumnos in the DB.
	 *
	 * @param alumnos
	 */
	private void insertMissingAlumnos(List<AlumnoDTO> alumnos) {
		List<AlumnoDTO> existingAlumnos = InscribirMultiplesUsuariosModel.this.getAlumnos(),
				alumnosToRegister = alumnos.parallelStream() // Remove the alumnos that are already in the DB
				.filter((alumnoInsertar) -> existingAlumnos.parallelStream()
						.allMatch((alumnoDatabase) -> !alumnoDatabase.getEmail().equals(alumnoInsertar.getEmail())))
				.collect(java.util.stream.Collectors.toList());

		if (alumnosToRegister.size() != 0) {
			for(AlumnoDTO alumno : alumnosToRegister) {
				String sql = "insert into alumno (nombre, apellidos, email, telefono) values (?, ?, ?, ?);";
				InscribirMultiplesUsuariosModel.this.getDatabase().executeUpdate(sql, alumno.getNombre(),
					alumno.getApellidos(),alumno.getEmail(), alumno.getTelefono());
			}
		}
	}

	/**
	 * getAlumnosIDsFromEmails.
	 * <p>
	 * This function will return a list of the IDs of the alumnos
	 * whose email is contained in the input array.
	 * </p> <p>
	 * To do this, the emails must be contained in the {@link AlumnoDTO}s
	 * that are passed as parameter.
	 * </p>
	 *
	 * @param alumnos Array of alumnos the be checked
	 * @return List of IDs
	 */
	private List<String> getAlumnosIDsFromEmails (List<AlumnoDTO> alumnos) {
		if (alumnos.size() != 0) {
			String outputSql = "SELECT id FROM alumno WHERE alumno.email IN (? ";
			String[] outputDatos = new String[alumnos.size()];
			outputDatos[0] = alumnos.get(0).getEmail();
			for (int i = 1 ; i < alumnos.size() ; i++) {
				outputSql += ", ?";
				outputDatos[i] = alumnos.get(i).getEmail();
			}

			return InscribirMultiplesUsuariosModel.this.getDatabase()
					.executeQueryArray(outputSql+");", (Object[]) outputDatos) // Query all the alumnos IDs. This will return a List<Object[]>
					.stream() //  Form a stream
					.collect(new g41.si2022.util.collector.HalfwayListCollector<Object[], String> () {
						// This Collector will go from List<Object[]> to List<String> where String is each alumno's ID
						@Override
						public BiConsumer<List<String>, Object[]> accumulator() {
							return (list, item) -> list.add(item[0].toString());
						}

					});
		}
		return new java.util.ArrayList<String>();
	}

	/**
	 * getAlumnos. This method will return the list of alumnos.
	 *
	 * @return List of alumnos.
	 */
	public List<AlumnoDTO> getAlumnos() {
		String sql = "select * from alumno;";
		return this.getDatabase().executeQueryPojo(AlumnoDTO.class, sql);
	}

	public List<ColectivoDTO> getCostes(String idCurso) {
		String sql = "select * from coste where curso_id = ?;";
		return this.getDatabase().executeQueryPojo(ColectivoDTO.class, sql, idCurso);
	}

	/**
	 * getAlumnosInCurso. This method will return the list of alumnos in a given curso.
	 *
	 * @param curso_id ID of the curso to be retrieved
	 * @return List of alumnos that are in the curso with curso_id
	 */
	public List<AlumnoDTO> getAlumnosInCurso (String curso_id) {
		String sql = "select * from inscripcion where curso_id = ?";
		return this.getDatabase().executeQueryPojo(AlumnoDTO.class, sql, curso_id);
	}

	public void insertGrupo(String nombre, String email, String telefono) {
		String sql =
				"insert into entidad (nombre, email, telefono)"
						+ " values (?, ?, ?);";
		this.getDatabase().executeUpdate(sql, nombre, email, telefono);
	}

	/**
	 * insertInscripciones. Inserts a batch of inscripciones.
	 * If an alumno is already inserted for this course, they will not be added again.
	 *
	 * @param fecha today's date
	 * @param curso_id curso id
	 * @param alumno_id alumno id
	 * @param grupo_id grupo id
	 */
	public void insertInscripciones(String fecha, String curso_id, List<String> alumno_id, String grupo_id) {
		List<AlumnoDTO> alumnosInCurso = this.getAlumnosInCurso(curso_id);
		alumno_id = alumno_id.parallelStream() // Remove alumnos that are already in this curso
				.filter((id) -> alumnosInCurso.parallelStream()
						.allMatch((alumnoInCurso) -> !alumnoInCurso.getId().equals(id)))
				.collect(java.util.stream.Collectors.toList());

		if (alumno_id.size() != 0) {
			String sql = "INSERT INTO inscripcion (fecha, alumno_id, entidad_id, curso_id, coste_id)"
					+ " VALUES (?, ?, ?, ?, ?);";
			for (String id : alumno_id) {
				this.getDatabase().executeUpdate(sql, fecha, id, grupo_id, curso_id, "1");
			}
		}
	}

	public boolean verifyEmail(String email) {
		return Util.verifyEmailInGrupo(this.getDatabase(), email);
	}
}
