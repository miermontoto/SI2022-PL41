package g41.si2022.coiipa.registrar_curso;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.List;
import java.util.stream.Collectors;

import g41.si2022.dto.ColectivoDTO;
import g41.si2022.dto.ProfesorDTO;

public class RegistrarCursoModel extends g41.si2022.mvc.Model {

	public java.util.List<ProfesorDTO> getListaProfesores() {
		String sql = "SELECT * FROM docente ORDER BY nombre";
		return this.getDatabase().executeQueryPojo(ProfesorDTO.class, sql);
	}
	
	/**
	 * insertColectivos. This method inserts the missing colectivos that are not already in the database.
	 * 
	 * It will return a Map of the colectivos that were inserted.
	 * 
	 * @param colectivosInTable List of colectivos to be added. This is a list of colectivo Names
	 * @return List of colectivos that were actually added. This is a map of Colectivo Name and Colectivo ID
	 */
	public Map<String, String> insertColectivos (List<String> colectivosInTable) {
		Map<String, ColectivoDTO> existingColectivos = this.getColectivos().stream()
				.collect(Collectors.toMap(e -> e.getNombre(), e -> e));

		List<String> missingColectivosInDatabase = colectivosInTable.stream()
			.filter((nombreColectivo) -> !existingColectivos.keySet().contains(nombreColectivo))
			.collect(Collectors.toList());

		String sql;
		if (missingColectivosInDatabase.size() > 0) {
			sql = "INSERT INTO colectivo (nombre) VALUES (?)";
			for (int i = 0 ; i < missingColectivosInDatabase.size() - 1 ; i++) {
				sql += ", (?)";
			}
			this.getDatabase().executeUpdate(sql+";", missingColectivosInDatabase.toArray());
		}
		
		sql = "SELECT * FROM colectivo WHERE colectivo.nombre IN (?) ";
		for (int i = 0 ; i < missingColectivosInDatabase.size() - 1 ; i++) {
			sql += ", (?)";
		}
		
		return this.getDatabase().executeQueryMap(sql, colectivosInTable.toArray())
				.stream().collect(Collectors.toMap(row -> row.get("nombre").toString(), row -> row.get("id").toString()));
	}
	
	public void insertCostes (Map<String, Double> costes, String idCurso) {
		java.util.Iterator<Map.Entry<String, Double>> itr = costes.entrySet().iterator();
		Map.Entry<String, Double> current = itr.next();
		String sql = "INSERT INTO coste (colectivo_id, coste, curso_id) VALUES (?, ?, ?)";
		java.util.List<String> data = new java.util.ArrayList<String> ();
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
	 * Returns the list of colectivos
	 *
	 * @return List of colectivos
	 */
	public java.util.List<g41.si2022.dto.ColectivoDTO> getColectivos () {
		return this.getDatabase().executeQueryPojo(g41.si2022.dto.ColectivoDTO.class, "SELECT * FROM colectivo;");
	}

	public void insertDocencia(String remuneracion, String profesor_id, String curso_id) {
		String sql = "INSERT INTO docencia (remuneracion, docente_id, curso_id) VALUES (?, ?, ?)";
		this.getDatabase().executeUpdate(sql, remuneracion, profesor_id, curso_id);
	}

	public void insertEvento(String loc, String fecha, String horaIni, String horaFin, String cursoId) {
		String sql = "INSERT INTO evento (loc, fecha, hora_ini, hora_fin, curso_id) VALUES (?, ?, ?, ?, ?)";
		this.getDatabase().executeUpdate(sql, loc, fecha, horaIni, horaFin, cursoId);
	}
}
