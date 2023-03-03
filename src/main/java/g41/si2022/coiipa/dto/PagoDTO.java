package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class PagoDTO {

	private String id;
	private String alumno_id;
	private String nombre;
	private String coste;
	private String fecha;
	private String estado;

	public PagoDTO() { }

	public PagoDTO(String id, String alumno_id, String nombre, String coste, String fecha, String estado) {
		this.id = id;
		this.alumno_id = alumno_id;
		this.nombre = nombre;
		this.coste = coste;
		this.fecha = fecha;
		this.estado = estado;
	}
}
