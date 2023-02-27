package g41.si2022.coiipa.registrar_pago;
/**
 * Cada una de las filas que muestran al usuario las carreras y su estado
 * IMPORTANTE: Cuando se usan los componentes de Apache Commons DbUtils debe
 * mantenerse de forma estricta el convenio de capitalización de Java:
 *  - Capitalizar todas las palabras que forman un identificador
 *    excepto la primera letra de nombres de métodos y variables.
 *  - No utilizar subrayados
 * Seguir tambien estos mismos criterios en los nombres de tablas y campos de la BD
 */
public class InsertarPagoDTO {

	private String id;
	private String coste;
	private String estado;

	public InsertarPagoDTO() { }


	public InsertarPagoDTO(String rowId, String rowCoste, String rowEstado) {
		this.id = rowId;
		this.coste = rowCoste;
		this.estado = rowEstado;
	}


	public String getId() { return id; }

	public void setId(String id) { this.id = id; }

	public String getCoste() { return coste; }

	public void setCoste(String coste) { this.coste = coste; }

	public String getEstado() { return estado; }

	public void setEstado(String estado) { this.estado = estado; }
}
