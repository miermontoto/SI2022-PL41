package g41.si2022.coiipa.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter @SuppressWarnings("unused")
public class CursoDTO {

	private String
		nombre,
		descripcion,
		estado;
	private Date
		start_inscr,
		end_inscr,
		start,
		end;
	private int
		id,
		plazas,
		docente_id;
	
	public CursoDTO (
			String nombre, String descripcion, String estado,
			Date start_inscr, Date end_inscr, Date start, Date end,
			int id, int plazas, int docente_id
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
	
}
