package g41.si2022.coiipa.dto;

import java.util.Date;

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
	
	public void setNombre(String nombre) { this.nombre = nombre; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
	public void setEstado(String estado) { this.estado = estado; }
	public void setStart_inscr(Date start_inscr) { this.start_inscr = start_inscr; }
	public void setEnd_inscr(Date end_inscr) { this.end_inscr = end_inscr; }
	public void setStart(Date start) { this.start = start; }
	public void setEnd(Date end) { this.end = end; }
	public void setId(int id) { this.id = id; }
	public void setPlazas(int plazas) { this.plazas = plazas; }
	public void setDocente_id(int docente_id) { this.docente_id = docente_id; }
		
	public String getNombre() { return this.nombre; }
	public String getDescripcion() { return this.descripcion; }
	public String getEstado () { return this.estado; }
	public Date getStart_inscr() { return this.start_inscr; }
	public Date getEnd_inscr() { return this.end_inscr; }
	public Date getStart() { return this.start; }
	public Date getEnd() { return this.end; }
	public int getId() { return this.id; }
	public int getPlazas() { return this.plazas; }
	public int getDocente_id() { return this.docente_id; }
}
