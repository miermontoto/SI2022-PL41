package g41.si2022.coiipa.facturacion;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import g41.si2022.coiipa.gestionar_facturas_profesores.GestionarFacturasProfModel;
import g41.si2022.coiipa.registrar_curso.RegistrarCursoModel;
import g41.si2022.dto.ProfesorDTO;

import java.util.HashMap;
import java.util.ArrayList;

public class FacturacionTest extends g41.si2022.coiipa.TestCase {
	
	private GestionarFacturasProfModel gfpm;
	private String cursoId;
	private ProfesorDTO profesor;
	private String docenciaId;

	@Override
	public void loadData () {
		this.gfpm = new GestionarFacturasProfModel ();
		RegistrarCursoModel rcm = new RegistrarCursoModel ();
		this.cursoId = rcm.insertCurso(
			"Kafka",
			"En este curso se estudiarán las bases de Kafka.", 
			"2023-01-01", "2023-02-01", "2023-02-02", "2023-05-01",
			"20",
			new HashMap<String, Double> () {
				private static final long serialVersionUID = 1L;
			{
				this.put("Colegiados", 30.0);
				this.put("Estudiantes de Uniovi", 10.0);
			}}
		).get();
		this.profesor = 
			new ProfesorDTO("Pedro", "Angel Martinez", "545232121A", "C/ Albeniz, 5", "panma@gmail.com", "664225423");
		this.gfpm.getDatabase().insertBulk(
			"docente",
			new String[] { "nombre", "apellidos", "dni", "email", "telefono", "direccion" },
			new ArrayList<ProfesorDTO> () {
				private static final long serialVersionUID = 1L;
			{ 
				this.add(FacturacionTest.this.profesor);
			}},
			new ArrayList<java.util.function.Function<g41.si2022.dto.DTO, Object>> () {
				private static final long serialVersionUID = 1L;
			{
				add(e -> ((ProfesorDTO) e).getNombre());
				add(e -> ((ProfesorDTO) e).getApellidos());
				add(e -> ((ProfesorDTO) e).getDni());
				add(e -> ((ProfesorDTO) e).getEmail());
				add(e -> ((ProfesorDTO) e).getTelefono());
				add(e -> ((ProfesorDTO) e).getDireccion());
			}}
		);
		this.profesor = this.gfpm.getDatabase().executeQueryPojo(
			ProfesorDTO.class, "SELECT * FROM docente"
		).get(0);
		this.profesor.setRemuneracion("250");
		rcm.insertDocencia(
			new ArrayList<ProfesorDTO> () {
				private static final long serialVersionUID = 1L;
			{ 
				this.add(FacturacionTest.this.profesor);
			}},
			cursoId
		);
		this.docenciaId = String.valueOf(this.getDatabase().executeQuerySingle(
			"SELECT id FROM docencia LIMIT(1)"
		));
	}

	@Test
	/**
	 * This test case will try to create a new Factura for a given docencia.
	 * The costs match, so the entry will be created and a {@code true} will be returned.
	 */
	public void test1 () {
		assertEquals(
			true,
			this.gfpm.insertFactura("2023-05-01", this.docenciaId, "250")
		);
	}

	@Test
	/**
	 * This test case will try to create a new Factura for a given docencia.
	 * The costs will not match, so an error will be returned in the form of {@code false}.
	 */
	public void test2 () {
		assertEquals(
			false,
			this.gfpm.insertFactura("2023-05-01", this.docenciaId, "150")
		);
	}
	
}
