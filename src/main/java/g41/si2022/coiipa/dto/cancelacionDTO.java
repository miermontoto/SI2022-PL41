package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class cancelacionDTO {

	private int id;
	private int alumno_id;
	private String nombre;
	private String coste;
	private String nombre_curso;
	private String inicio_curso;

	public cancelacionDTO() { }

	public cancelacionDTO(int id, int alumno_id, String Nombre_alumno, String Coste_curso, String Nombre_curso, String Inicio_curso) {
		this.id = id;
		this.alumno_id = alumno_id;
		this.nombre = Nombre_alumno;
		this.coste = Coste_curso;
		this.nombre_curso = Nombre_curso;
		this.inicio_curso = Inicio_curso;
	}
}
