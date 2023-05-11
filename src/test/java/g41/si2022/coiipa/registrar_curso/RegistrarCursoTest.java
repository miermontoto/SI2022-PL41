package g41.si2022.coiipa.registrar_curso;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

/**
 * RegistrarCurso. Test class used to test the registration of different Cursos under various conditions.
 * 
 * @author Alex // UO281827
 *
 */
public class RegistrarCursoTest extends g41.si2022.coiipa.TestCase {
	
	private RegistrarCursoModel rcm;
	private String nombre = "Kafka";
	private String descripcion = "En este curso se estudiarán las bases de Kafka.";
	private String startInscr = "2023-01-01";
	private String endInscr = "2023-02-01";
	private String start = "2023-02-02";
	private String end = "2023-05-01";
	private String plazas = "20";
	private Map<String, Double> costes = new HashMap<String, Double> () {
		private static final long serialVersionUID = 1L;
	{
		this.put("Colegiados", 30.0);
		this.put("Estudiantes de Uniovi", 10.0);
	}};

	@Test
	/**
	 * This test inserts a Curso that is all okay.
	 * The method will return the id of the Curso.
	 * As we have just created the DB, this ID will be 1.
	 */
	public void test1() {
		assertEquals("1", rcm.insertCurso(
			this.nombre,
			this.descripcion,
			this.startInscr, this.endInscr, this.start, this.end,
			this.plazas,
			this.costes
		).get());
	}
	
	@Test
	/**
	 * This test inserts a Curso with null costs for the different collectives.
	 * This will return an error in the form of {@code false} in the {@link ReturnValue}.
	 * @see ReturnValue
	 */
	public void test2() {
		assertEquals(false, rcm.insertCurso(
			this.nombre,
			this.descripcion,
			this.startInscr, this.endInscr, this.start, this.end,
			this.plazas,
			null
		).isOkay());
	}
	
	@Test
	/**
	 * This test inserts a Curso with empty costs for the different collectives.
	 * This will return an error in the form of {@code false} in the {@link ReturnValue}.
	 * @see ReturnValue
	 */
	public void test3() {
		assertEquals(false, rcm.insertCurso(
			this.nombre,
			this.descripcion,
			this.startInscr, this.endInscr, this.start, this.end,
			this.plazas,
			new HashMap<String, Double> ()
		).isOkay());
	}

	@Test
	/**
	 * This test inserts a Curso with negative free seats.
	 * This will return an error in the form of {@code false} in the {@link ReturnValue}.
	 * @see ReturnValue
	 */
	public void test4() {
		assertEquals(false, rcm.insertCurso(
			this.nombre,
			this.descripcion,
			this.startInscr, this.endInscr, this.start, this.end,
			"-5",
			this.costes
		).isOkay());
	}
	
	@Test
	/**
	 * This test inserts a Curso with no free seats.
	 * This will return an error in the form of {@code false} in the {@link ReturnValue}.
	 * @see ReturnValue
	 */
	public void test5() {
		assertEquals(false, rcm.insertCurso(
			this.nombre,
			this.descripcion,
			this.startInscr, this.endInscr, this.start, this.end,
			"0",
			this.costes
		).isOkay());
	}

	@Test
	/**
	 * This test inserts a Curso whose start_inscr date is after the end of the Curso.
	 * This will return an error in the form of {@code false} in the {@link ReturnValue}.
	 * @see ReturnValue
	 */
	public void test6() {
		assertEquals(false, rcm.insertCurso(
			this.nombre,
			this.descripcion,
			"2025-01-01", "2023-02-01", "2023-02-02", "2023-05-01",
			this.plazas,
			this.costes
		).isOkay());
	}
	
	@Test
	/**
	 * This test inserts a Curso whose start_inscr date is after the end of the end_inscr of the Curso.
	 * This will return an error in the form of {@code false} in the {@link ReturnValue}.
	 * @see ReturnValue
	 */
	public void test7() {
		assertEquals(false, rcm.insertCurso(
			this.nombre,
			this.descripcion,
			"2023-01-01", "2022-02-01", "2023-02-02", "2023-05-01",
			this.plazas,
			this.costes
		).isOkay());
	}
	
	@Test
	/**
	 * This test inserts a Curso whose start date is before the start_inscr of the Curso.
	 * This will return an error in the form of {@code false} in the {@link ReturnValue}.
	 * @see ReturnValue
	 */
	public void test8() {
		assertEquals(false, rcm.insertCurso(
			this.nombre,
			this.descripcion,
			"2023-01-01", "2023-02-01", "2022-02-02", "2023-05-01",
			this.plazas,
			this.costes
		).isOkay());
	}
	
	@Test
	/**
	 * This test inserts a Curso assigned to an external entity.
	 * There are no errors in this insertion, so the method returns the new ID ("1").
	 */
	public void test10 () {
		this.getDatabase().executeUpdate(
				"INSERT INTO entidad "
				+ "(nombre, email, telefono) VALUES "
				+ "('cursados', 'info@cursados.com', '775221330')");
		assertEquals(
			"1",
			rcm.insertCursoExterno(
				this.nombre,
				this.descripcion,
				"2023-01-01", "2023-02-01", "2023-02-02", "2023-05-01",
				this.plazas,
				this.costes,
				"1",
				"500"
			).get()
		);
	}
	
	@Test
	/**
	 * This test inserts a Curso assigned to an external entity.
	 * There is an error in the payment assigned to this entity, as it is negative.
	 * The method will return an error as false.
	 */
	public void test11 () {
		this.getDatabase().executeUpdate(
				"INSERT INTO entidad "
				+ "(nombre, email, telefono) VALUES "
				+ "('cursados', 'info@cursados.com', '775221330')");
		assertEquals(
			false,
			rcm.insertCursoExterno(
				this.nombre,
				this.descripcion,
				"2023-01-01", "2023-02-01", "2023-02-02", "2023-05-01",
				this.plazas,
				this.costes,
				"1",
				"-500"
			).isOkay()
		);
	}
	
	@Test
	/**
	 * This test inserts a Curso assigned to an external entity.
	 * There is an error in the payment assigned to this entity, as it is zero.
	 * The method will return an error as false.
	 */
	public void test12 () {
		this.getDatabase().executeUpdate(
				"INSERT INTO entidad "
				+ "(nombre, email, telefono) VALUES "
				+ "('cursados', 'info@cursados.com', '775221330')");
		assertEquals(
			false,
			rcm.insertCursoExterno(
				this.nombre,
				this.descripcion,
				"2023-01-01", "2023-02-01", "2023-02-02", "2023-05-01",
				this.plazas,
				this.costes,
				"1",
				"0"
			).isOkay()
		);
	}

	@Override
	public void loadData() { 
		this.rcm = new RegistrarCursoModel ();
	}

}
