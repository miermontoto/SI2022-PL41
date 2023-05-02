package g41.si2022.coiipa.inscribir_multiples_usuarios;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import g41.si2022.dto.AlumnoDTO;
import g41.si2022.dto.ColectivoDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.GrupoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.util.Util;

public class InscribirMultiplesUsuariosModel extends g41.si2022.mvc.Model {

	private List<ColectivoDTO> listaColectivos;

	/**
	 * getListaColectivos. Returns the list of colectivos.
	 * 
	 * @return List of existing colectivos in database.
	 */
	public List<ColectivoDTO> getListaColectivos () {
		if (this.listaColectivos == null)
			this.listaColectivos = this.getDatabase().executeQueryPojo(ColectivoDTO.class, "SELECT * FROM colectivo");
		return this.listaColectivos;
	}
	
	/**
	 * getColectivosFromCurso. Returns the colectivos that appear on a given curso.
	 * 
	 * @param cursoId ID of the curso
	 * @return List of colectivos that may access the curso.
	 */
	public List<ColectivoDTO> getColectivosFromCurso (String cursoId) {
		return this.getDatabase().executeQueryPojo(ColectivoDTO.class, 
				"SELECT colectivo.* "
				+ "FROM colectivo "
				+ "INNER JOIN coste ON colectivo.id = coste.colectivo_id "
				+ "INNER JOIN curso ON curso.id = coste.curso_id "
				+ "WHERE curso.id = ?",
				cursoId);
	}

	public List<CursoDTO> getListaCursos(String date) {
		String sql = "select *, (c.plazas -"
				+ " (select count(*) from inscripcion as i where i.curso_id = c.id)"
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
	 * @param List of AlumnoDTOs
	 */
	public List<AlumnoDTO> insertAlumnos (List<AlumnoDTO> alumnos) {
		this.insertMissingAlumnos(alumnos);
		return getAlumnosFromEmails (alumnos);
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
			this.getDatabase().insertBulk(
				"alumno",
				new String[] {"nombre", "apellidos", "email", "telefono"}, 
				alumnosToRegister, 
				new java.util.ArrayList<java.util.function.Function<g41.si2022.dto.DTO, Object>> () {
					private static final long serialVersionUID = 1L;
				{
					this.add(e -> ((AlumnoDTO) e).getNombre());
					this.add(e -> ((AlumnoDTO) e).getApellidos());
					this.add(e -> ((AlumnoDTO) e).getEmail());
					this.add(e -> ((AlumnoDTO) e).getTelefono());
				}}
			);
		}
	}

	/**
	 * getAlumnosIDsFromEmails.
	 * <p>
	 * This function will return a list of the AlumnoDTOs
	 * whose email is contained in the input array.
	 * </p> <p>
	 * To do this, the emails must be contained in the {@link AlumnoDTO}s
	 * that are passed as parameter.
	 * </p>
	 *
	 * @param alumnos Array of alumnos the be checked
	 * @return List of AlumnoDTO
	 */
	private List<AlumnoDTO> getAlumnosFromEmails (List<AlumnoDTO> alumnos) {
		if (alumnos.size() != 0) {
			String outputSql = "SELECT * FROM alumno WHERE alumno.email IN (? ";
			String[] outputDatos = new String[alumnos.size()];
			outputDatos[0] = alumnos.get(0).getEmail();
			for (int i = 1 ; i < alumnos.size() ; i++) {
				outputSql += ", ?";
				outputDatos[i] = alumnos.get(i).getEmail();
			}

			return InscribirMultiplesUsuariosModel.this.getDatabase()
					.executeQueryPojo(AlumnoDTO.class, outputSql+");", (Object[]) outputDatos);
		}
		return new java.util.ArrayList<AlumnoDTO>();
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

	/**
	 * getCoste. Gets the coste_id for a given curso and nombreColectivo.
	 * 
	 * @param idCurso Curso to be checked
	 * @param nombreColectivo Name of the colectivo to be checked
	 * @return Coste_id that the colectivo has to pay for the curso.
	 */
	public String getCostes(String idCurso, String nombreColectivo) {
		String sql = "SELECT coste.id "
				+ "FROM coste "
				+ "INNER JOIN colectivo ON coste.colectivo_id = colectivo.id "
				+ "WHERE curso_id = ? AND colectivo.nombre = ?";
		return this.getDatabase().executeQuerySingle(sql, idCurso, nombreColectivo).toString();
	}

	/**
	 * getAlumnosInCurso. This method will return the list of alumnos in a given curso.
	 *
	 * @param curso_id ID of the curso to be retrieved
	 * @return List of alumnos that are in the curso with curso_id
	 */
	public List<InscripcionDTO> getAlumnosInCurso (String curso_id) {
		String sql = "select * from inscripcion where curso_id = ?";
		return this.getDatabase().executeQueryPojo(InscripcionDTO.class, sql, curso_id);
	}

	/**
	 * insertInscripciones. Inserts a batch of inscripciones.
	 * If an alumno is already inserted for this course, they will not be added again.
	 * If an alumno is not in the database, they'll be added.
	 *
	 * @param alumnos List of alumnos to be added
	 * @param fecha today's date
	 * @param curso_id curso id
	 */
	public void insertInscripciones(List<AlumnoDTO> alumnos, String fecha, String curso_id) {
		List<InscripcionDTO> alumnosInCurso = this.getAlumnosInCurso(curso_id);
		List<AlumnoDTO> alumnosModifiable = new java.util.ArrayList<AlumnoDTO> ();
		alumnos.forEach(alumno -> alumnosModifiable.add(alumno));
		alumnosModifiable.removeIf(alumno -> alumnosInCurso.parallelStream().allMatch(
			alumnoInCurso -> alumnoInCurso.getAlumno_id().equals(alumno.getId())
		));
		List<AlumnoDTO> emails = this.getAlumnosFromEmails(alumnosModifiable);
		alumnosModifiable.forEach(alumno -> { 
			AlumnoDTO current;
			java.util.Iterator<AlumnoDTO> itr = emails.iterator();
			do
				current = itr.next();
			while (itr.hasNext() && current.getEmail().equals(alumno.getEmail()));
			alumno.setId(current.getId());
		});

		this.getDatabase().insertBulk(
				"inscripcion",
				new String[] {"fecha", "alumno_id", "curso_id", "coste_id"},
				alumnos,
				new java.util.ArrayList<java.util.function.Function<g41.si2022.dto.DTO, Object>> () {
					private static final long serialVersionUID = 1L;
				{
					this.add(e -> fecha);
					this.add(e -> ((AlumnoDTO) e).getId());
					this.add(e -> curso_id);
					this.add(e -> InscribirMultiplesUsuariosModel.this.getCostes(curso_id, ((AlumnoDTO) e).getNombreColectivo()));
				}});
	}

	public boolean verifyEmail(String email) {
		return Util.verifyEmailInGrupo(this.getDatabase(), email);
	}

}
