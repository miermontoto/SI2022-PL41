package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class CursoDTO {

	private String
		nombre,
		descripcion,
		estado,
		start_inscr,
		end_inscr,
		start,
		end,
		id,
		plazas,
		docente_id;

	public CursoDTO() { }

	public CursoDTO (
			String nombre, String descripcion, String estado,
			String start_inscr, String end_inscr, String start, String end,
			String id, String plazas, String docente_id
	) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estado = estado;
		this.start_inscr = start_inscr;
		this.end_inscr = end_inscr;
		this.start = start;
		this.end = end;
		this.id = id;
		this.plazas = plazas;
		this.docente_id = docente_id;
	}

	public CursoDTO(String id, String nombre, String plazas, String start_inscr, String end_inscr) {
		this.id = id;
		this.nombre = nombre;
		this.plazas = plazas;
		this.start_inscr = start_inscr;
		this.end_inscr = end_inscr;
	}
}
