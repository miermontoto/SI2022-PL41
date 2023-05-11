package g41.si2022.coiipa.registrar_curso;

import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import g41.si2022.dto.SesionDTO;

public class RegistrarEventosTest extends g41.si2022.coiipa.TestCase {
	
	private RegistrarCursoModel rcm;
	private String cursoId;

	@Test
	/**
	 * This test inserts a new Evento to a Curso.
	 * There is no error on the Evento as its date matches the allowed window.
	 */
	public void test1 () {
		assertEquals(Integer.valueOf(1), rcm.insertEvento(
			new java.util.ArrayList<SesionDTO> () {{
				this.add(new SesionDTO(
					"AN-B3",
					"2023-02-03",
					"09:00",
					"10:00"
				));
			}},
			this.cursoId
		).get());
	}

	@Test
	/**
	 * This test inserts a new Evento to a Curso.
	 * This Evento takes place before the Curso starts, so it's denied and nothing is added.
	 * The function returns that 0 Eventos have been added.
	 */
	public void test2 () {
		assertEquals(Integer.valueOf(0), rcm.insertEvento(
			new java.util.ArrayList<SesionDTO> () {{
				this.add(new SesionDTO(
					"AN-B3",
					"2023-01-01",
					"09:00",
					"10:00"
				));
			}},
			this.cursoId
		).get());
	}
	
	@Test
	/**
	 * This test inserts a new Evento to a Curso.
	 * In this test, several Eventos will be added (4), two of which contain mistaked in their dates.
	 * Therefore, two are added and two are denied by the function.
	 */
	public void test3 () {
		assertEquals(Integer.valueOf(2), rcm.insertEvento(
			new java.util.ArrayList<SesionDTO> () {{
				this.add(new SesionDTO(
					"AN-B3",
					"2023-02-07", // Ok (allowed)
					"09:00",
					"10:00"
				));
				this.add(new SesionDTO(
					"AN-B3",
					"2023-04-05", // Ok (allowed)
					"09:00",
					"10:00"
				));
				this.add(new SesionDTO(
					"AN-B3",
					"2020-01-01", // Way too soon (denied)
					"09:00",
					"10:00"
				));
				this.add(new SesionDTO(
					"AN-B3",
					"2025-01-01", // Way too late (denied)
					"09:00",
					"10:00"
				));
			}},
			this.cursoId
		).get());
	}
	
	@Override
	public void loadData() {
		rcm = new RegistrarCursoModel();
		this.cursoId = this.rcm.insertCurso(
			"Kafka",
			"En este curso se estudiarán las bases de Kafka.", 
			"2023-01-01", "2023-02-01", "2023-02-02", "2023-05-01",
			"20",
			new HashMap<String, Double> () {{
				this.put("Colegiados", 30.0);
				this.put("Estudiantes de Uniovi", 10.0);
			}}
		).get();
	}

}
