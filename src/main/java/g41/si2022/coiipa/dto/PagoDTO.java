package g41.si2022.coiipa.dto;
/**
 * Cada una de las filas que muestran al usuario las carreras y su estado
 * IMPORTANTE: Cuando se usan los componentes de Apache Commons DbUtils debe
 * mantenerse de forma estricta el convenio de capitalización de Java:
 *  - Capitalizar todas las palabras que forman un identificador
 *    excepto la primera letra de nombres de métodos y variables.
 *  - No utilizar subrayados
 * Seguir tambien estos mismos criterios en los nombres de tablas y campos de la BD
 */
public class PagoDTO {
	int id;
	int alumno_id;
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


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getCoste() {
		return coste;
	}


	public void setCoste(String coste) {
		this.coste = coste;
	}


	

	public int getAlumno_id() {
		return alumno_id;
	}


	public void setAlumno_id(int alumno_id) {
		this.alumno_id = alumno_id;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFecha() {
		return fecha;
	}


	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	
}
