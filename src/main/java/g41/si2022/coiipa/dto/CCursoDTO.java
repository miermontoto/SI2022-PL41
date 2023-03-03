// El objeto en el que se guarda la query de la base de datos

	// Hacer DTO de las inscripciones
	// El DTO de cursos lo comparto con Mier

package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;

//@Getter 
//@Setter
public class CCursoDTO {

	private String nombre;
	private String estado;
	private String plazas;
	private String start;
	private String end;

	public CCursoDTO() {}

	public CCursoDTO(String nombre, String estado, String plazas, String start, String end) {
		this.nombre = nombre;
		this.estado = estado;
		this.plazas = plazas;
		this.start  = start;
		this.end    = end;
	}

	public String getNombre() { return nombre; }

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPlazas() {
		return plazas;
	}

	public void setPlazas(String plazas) {
		this.plazas = plazas;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	
}
