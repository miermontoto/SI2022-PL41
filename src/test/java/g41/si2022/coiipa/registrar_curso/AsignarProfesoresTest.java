package g41.si2022.coiipa.registrar_curso;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import g41.si2022.dto.ProfesorDTO;

public class AsignarProfesoresTest extends g41.si2022.coiipa.TestCase {
	
	private RegistrarCursoModel rcm;
	private String cursoId;
	private List<ProfesorDTO> profesores;
	
	@Test
	/**
	 * This test inserts a new Profesor to a Docencia.
	 * In this test, only one Profesor is inserted with correct data, so no error is returned.
	 */
	public void test1 () {
		profesores.get(0).setRemuneracion("250");
		assertEquals(
			Integer.valueOf(1),
			this.rcm.insertDocencia(profesores.subList(0, 1), cursoId).get()
		);
	}
	
	@Test
	/**
	 * This test inserts a new Profesor to a Docencia.
	 * In this test, only one Profesor is inserted with incorrect data, so none are inserted.
	 */
	public void test10 () {
		profesores.get(0).setRemuneracion("-250");
		assertEquals(
			Integer.valueOf(0),
			this.rcm.insertDocencia(profesores.subList(0, 1), cursoId).get()
		);
	}
	
	@Test
	/**
	 * This test inserts a new Profesor to a Docencia.
	 * In this test, two Profesores are inserted, both of which contain correct data, so no error is returned.
	 */
	public void test2 () {
		profesores.get(0).setRemuneracion("250");
		profesores.get(1).setRemuneracion("200");
		assertEquals(
			Integer.valueOf(2),
			this.rcm.insertDocencia(profesores, cursoId).get()
		);
	}
	
	@Test
	/**
	 * This test inserts a new Profesor to a Docencia.
	 * In this test, two Profesores are inserted, only one of which contain correct data, so only one is inserted.
	 */
	public void test3 () {
		profesores.get(0).setRemuneracion("250");
		profesores.get(1).setRemuneracion("-200");
		assertEquals(
			Integer.valueOf(1),
			this.rcm.insertDocencia(profesores, cursoId).get()
		);
	}
	
	@Test
	/**
	 * This test inserts a new Profesor to a Docencia.
	 * In this test, two Profesores are inserted, both of which contain correct data, so none are inserted.
	 */
	public void test4 () {
		profesores.get(0).setRemuneracion("-250");
		profesores.get(1).setRemuneracion("-200");
		assertEquals(
			Integer.valueOf(0),
			this.rcm.insertDocencia(profesores, cursoId).get()
		);
	}

	@Override
	public void loadData() {
		this.rcm = new RegistrarCursoModel();
		this.cursoId = this.rcm.insertCurso(
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
		this.profesores = new java.util.ArrayList<ProfesorDTO> () {
			private static final long serialVersionUID = 1L;
		{
			add(new ProfesorDTO("Pedro", "Angel Martinez", "545232121A", "C/ Albeniz, 5", "panma@gmail.com", "664225423"));
			add(new ProfesorDTO("Miguel", "Rubio Sanz", "789776221C", "Avd. Sol, 22", "miru@gmail.com", "123222888"));
		}};
		this.rcm.getDatabase().insertBulk(
			"docente",
			new String[] { "nombre", "apellidos", "dni", "email", "telefono", "direccion" },
			this.profesores,
			new java.util.ArrayList<java.util.function.Function<g41.si2022.dto.DTO, Object>> () {
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
		this.profesores = this.rcm.getDatabase().executeQueryPojo(ProfesorDTO.class, "SELECT * FROM docente");
	}

}
