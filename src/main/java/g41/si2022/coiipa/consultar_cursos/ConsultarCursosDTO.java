// El objeto en el que se guarda la query de la base de datos
	
	// Hacer DTO de las inscripciones
	// El DTO de cursos lo comparto con Mier

package g41.si2022.coiipa.consultar_cursos;

public class ConsultarCursosDTO {
	// nombre, estado, plazas, start_inscr, end_inscr
	private String nombre;
	private String estado;
	private String plazas;
	private String start_inscr; // Debería de ser start_curso
	private String end_inscr;   // Debería de ser end_curso

	public ConsultarCursosDTO() {}

	public ConsultarCursosDTO(String nombre, String estado, String plazas, String start_inscr, String end_inscr) {
		this.nombre = nombre;
		this.estado = estado;
		this.plazas = plazas;
		this.start_inscr = start_inscr; // Debería de ser start_curso
		this.end_inscr = end_inscr;     // Debería de ser end_curso
	}

	public String getNombre() { return this.nombre; }
	public String getEstado() { return this.estado; }
	public String getPlazas() { return this.plazas; }
	public String getStartInscr() { return this.start_inscr; }
	public String getEndInscr() { return this.end_inscr; }

	public void setNombre(String nombre) { this.nombre = nombre; }
	public void setEstado(String estado) { this.estado = estado; }
	public void setPlazas(String plazas) { this.plazas = plazas; }
	public void setStartInscr(String start_inscr) { this.start_inscr = start_inscr; }
	public void setEndInscr(String end_inscr) { this.end_inscr = end_inscr; }
}
