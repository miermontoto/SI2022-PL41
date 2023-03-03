// El objeto en el que se guarda la query de la base de datos

	// Hacer DTO de las inscripciones
	// El DTO de cursos lo comparto con Mier

package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CCurso {

	private String nombre;
	private String estado;
	private String plazas;
	private String start;
	private String end;

	public CCurso() {}

	public CCurso(String nombre, String estado, String plazas, String start, String end) {
		this.nombre = nombre;
		this.estado = estado;
		this.plazas = plazas;
		this.start  = start;
		this.end    = end;
	}
}
