package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data @SuppressWarnings("unused")
public class PagoDTO {

	private int id;
	private int alumno_id;
	private String nombre;
	private String coste;
	private String fecha;
	private String estado;

	public PagoDTO() { }

	public PagoDTO(int id, int alumno_id, String nombre, String coste, String fecha, String estado) {
		this.id = id;
		this.alumno_id = alumno_id;
		this.nombre = nombre;
		this.coste = coste;
		this.fecha = fecha;
		this.estado = estado;
	}
}
