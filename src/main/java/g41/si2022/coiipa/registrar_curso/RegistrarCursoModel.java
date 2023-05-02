package g41.si2022.coiipa.registrar_curso;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.omg.CORBA.IdentifierHelper;

import java.util.function.Function;

import g41.si2022.dto.ColectivoDTO;
import g41.si2022.dto.DTO;
import g41.si2022.dto.EntidadDTO;
import g41.si2022.dto.EventoDTO;
import g41.si2022.dto.ProfesorDTO;
import g41.si2022.dto.SesionDTO;

public class RegistrarCursoModel extends g41.si2022.mvc.Model {

	public java.util.List<ProfesorDTO> getListaProfesores() {
		String sql = "SELECT * FROM docente ORDER BY nombre";
		return this.getDatabase().executeQueryPojo(ProfesorDTO.class, sql);
	}

	/**
	 * Obtain a list of all entities
	 * 
	 * @return List of stored entities
	 */
	public java.util.List<EntidadDTO> getListaEntidades() {
		String sql = "SELECT * FROM entidad ORDER BY nombre";

		return this.getDatabase().executeQueryPojo(EntidadDTO.class, sql);
	}

	/**
	 * insertColectivos. This method inserts the missing colectivos that are not already in the database.
	 *
	 * It will return a Map of the colectivos that were inserted.
	 *
	 * @param colectivosInTable List of colectivos to be added. This is a list of colectivo Names
	 * @return List of colectivos that were actually added.
	 */
	public Map<String, ColectivoDTO> insertColectivos (List<String> colectivosInTable) {
		Map<String, ColectivoDTO> existingColectivos = this.getColectivos().stream()
				.collect(Collectors.toMap(e -> e.getNombre(), e -> e));

		List<String> missingColectivosInDatabase = colectivosInTable.stream()
			.filter(nombreColectivo -> !existingColectivos.keySet().contains(nombreColectivo))
			.collect(Collectors.toList());

		String sql;
		if (!missingColectivosInDatabase.isEmpty()) {
			sql = "INSERT INTO colectivo (nombre) VALUES (?)";
			for (int i = 0 ; i < missingColectivosInDatabase.size() - 1 ; i++) {
				sql += ", (?)";
			}
			this.getDatabase().executeUpdate(sql+";", missingColectivosInDatabase.toArray());
		}

		sql = "SELECT * FROM colectivo WHERE colectivo.nombre IN (?";
		for (int i = 0 ; i < colectivosInTable.size() - 1 ; i++) {
			sql += ", ?";
		}
		sql += ")";

		return this.getDatabase().executeQueryPojo(ColectivoDTO.class, sql, colectivosInTable.toArray())
				.stream().collect(Collectors.toMap(e -> e.getNombre(), e -> e));
	}

	/**
	 * insertCostes. This method will insert the colectivo/costes according to the View's table.<br>
	 * If a given colectivo is not featured in the database, it will be added automatically.
	 *
	 * @param costes Map of Colectivo Name, Colectivo cost for the curso.
	 * @param idCurso Id of the curso.
	 */
	public void insertCostes (Map<String, Double> costes, String idCurso) {
		Map<String, ColectivoDTO> colectivos = insertColectivos(costes.keySet().stream().collect(Collectors.toList()));
		java.util.Iterator<Map.Entry<String, Double>> itr = costes.entrySet().stream()
			.collect(Collectors.toMap(
				row -> colectivos.get(row.getKey()).getId(),
				row -> row.getValue()
			))
			.entrySet().iterator();
		Map.Entry<String, Double> current = itr.next();
		String sql = "INSERT INTO coste (colectivo_id, coste, curso_id) VALUES (?, ?, ?)";
		java.util.List<String> data = new java.util.ArrayList<> ();
		data.add(current.getKey());
		data.add(current.getValue().toString());
		data.add(idCurso);
		while (itr.hasNext()) {
			current = itr.next();
			sql += ", (?, ?, ?)";
			data.add(current.getKey());
			data.add(current.getValue().toString());
			data.add(idCurso);
		}
		this.getDatabase().executeUpdate(sql, data.toArray());
	}

	/**
	 * Inserta un curso en la base de datos.
	 * @param nombre Nombre del curso
	 * @param descripcion Descripcion del curso
	 * @param inscrStart Fecha de inicio de inscripción
	 * @param inscrEnd Fecha de fin de inscripción
	 * @param start Fecha de inicio del curso
	 * @param end Fecha de fin del curso
	 * @param plazas Cantidad de plazas disponibles
	 * @param localizacion Localización del curso
	 * @param costes Diccionario de costes Colectivo, Coste
	 * @return El ID del curso insertado
	 */
	public String insertCurso(
			String nombre, String descripcion,
			String inscrStart, String inscrEnd, String start, String end,
			String plazas, java.util.Map<String, Double> costes) {
		String sql = "INSERT INTO curso (nombre, descripcion, start_inscr, end_inscr, plazas, start, end) "
						+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
		this.getDatabase().executeUpdate(sql,
				nombre, descripcion,
				inscrStart, inscrEnd, plazas, start, end);

		String idCurso = String.valueOf(this.getDatabase().executeQuerySingle("select id from curso"
		 + " where nombre = ? and start_inscr = ? and start = ? and plazas = ?",
		  nombre, inscrStart, start, plazas));

		this.insertCostes(costes, idCurso);
		return idCurso;
	}

	/**
	 * Inserta un curso externo (contratado a una entidad) en la base de datos
	 * @param nombre Nombre del curso
	 * @param descripcion Descripcion del curso
	 * @param inscrStart Fecha de inicio de inscripción
	 * @param inscrEnd Fecha de fin de inscripción
	 * @param start Fecha de inicio del curso
	 * @param end Fecha de fin del curso
	 * @param plazas Cantidad de plazas disponibles
	 * @param localizacion Localización del curso
	 * @param costes Diccionario de costes Colectivo, Coste
	 * @param idEntidad Entidad asociada al curso
	 * @return El ID del curso insertado
	 */
	public String insertCursoExterno(
			String nombre, String descripcion,
			String inscrStart, String inscrEnd, String start, String end,
			String plazas, java.util.Map<String, Double> costes, String idEntidad, String importe) {

		String sql = "INSERT INTO curso(nombre, descripcion, start_inscr, end_inscr, plazas, start, end, entidad_id, importe)"
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		this.getDatabase().executeUpdate(sql, nombre, descripcion, inscrStart, inscrEnd, plazas, start, end, idEntidad, importe);

		String idCurso = String.valueOf(this.getDatabase().executeQuerySingle(
			"SELECT id FROM curso WHERE nombre = ? and start_inscr = ?" + 
			" and start = ? and plazas = ? and entidad_id = ? and importe = ?",
			nombre, inscrStart, start, plazas, idEntidad, importe));

		this.insertCostes(costes, idCurso);

		return idCurso;
	}

	/**
	 * Returns the list of colectivos
	 *
	 * @return List of colectivos
	 */
	public java.util.List<g41.si2022.dto.ColectivoDTO> getColectivos () {
		return this.getDatabase().executeQueryPojo(g41.si2022.dto.ColectivoDTO.class, "SELECT * FROM colectivo;");
	}

	/**
	 * insertDocencia. Inserts the docencia data for a curso.
	 *
	 * @param docentes List of docentes that take part in the curso.
	 * @param idCurso ID of the curso that they are taking part in.
	 */
	public void insertDocencia(List<ProfesorDTO> docentes, String idCurso) {
		this.getDatabase().insertBulk(
				"docencia",
				new String[] { "remuneracion", "docente_id", "curso_id" },
				docentes,
				new java.util.ArrayList<Function<DTO, Object>> () {
					private static final long serialVersionUID = 1L;
					{
						add(e -> ((ProfesorDTO) e).getRemuneracion());
						add(e -> ((ProfesorDTO) e).getId());
						add(e -> idCurso);
					}
				});
	}

	/**
	 * insertEvento. Inserts the eventos data for a curso.
	 *
	 * @param eventos List of eventos that compose the curso.
	 * @param cursoId ID of the curso that the eventos are part of.
	 */
	public void insertEvento(List<SesionDTO> eventos, String cursoId) {
		this.getDatabase().insertBulk(
				"sesion",
				new String[] {"loc", "fecha", "hora_ini", "hora_fin", "curso_id"},
				eventos,
				new java.util.ArrayList<Function<DTO, Object>> () {
					private static final long serialVersionUID = 1L;
					{
						add(e -> ((SesionDTO) e).getLoc());
						add(e -> ((SesionDTO) e).getFecha());
						add(e -> ((SesionDTO) e).getHoraIni());
						add(e -> ((SesionDTO) e).getHoraFin());
						add(e -> cursoId);
					}}
				);
	}

	/**
	 * Obtain an entity given it's id
	 * 
	 * @param idEntidad id of the entity to get
	 * @return the specified entity
	 */
	public EntidadDTO getEntidadById (String idEntidad) {
		String sql = "SELECT * FROM entidad"
				   + " WHERE id = ?";
		
		return this.getDatabase().executeQueryPojo(EntidadDTO.class, sql, idEntidad).get(0);
	}
}
