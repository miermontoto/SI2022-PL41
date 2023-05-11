package g41.si2022.coiipa.inscripciones_multiples;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.junit.*;

import g41.si2022.coiipa.inscribir_multiples_usuarios.InscribirMultiplesUsuariosModel;
import g41.si2022.coiipa.inscribir_multiples_usuarios_entidad.InscribirMultiplesUsuariosEntidadModel;
import g41.si2022.dto.AlumnoDTO;
import g41.si2022.util.db.Database;

public class InscripcionesMultiplesTest extends g41.si2022.coiipa.TestCase {

    private static InscribirMultiplesUsuariosModel model1;
    private static InscribirMultiplesUsuariosEntidadModel model2;

    @Override
    public void loadData() {
        try {
            this.getDatabase().executeScript(Database.SQL_LOAD);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @BeforeClass
    public static void initialize() {
        model1 = new InscribirMultiplesUsuariosModel();
        model2 = new InscribirMultiplesUsuariosEntidadModel();
    }

    private int getNumInscripciones(String cursoId) {
        return Integer.parseInt(getDatabase().executeQuerySingle("SELECT COUNT(*) FROM inscripcion WHERE curso_id = " + cursoId).toString());
    }

    /*
     * Ambos model deberían funcionar igual en las funciones que compartan.
     */

    @Test
    public void testPrincipal() {
        List<AlumnoDTO> alumnos = new LinkedList<>();
        alumnos.add(new AlumnoDTO());
        alumnos.add(new AlumnoDTO());

        // Datos de ejemplo
        alumnos.get(0).setNombre("Alejandro");
        alumnos.get(0).setApellidos("Gallego");
        alumnos.get(0).setEmail("alejandrogallegogaimer@gmail.com");
        alumnos.get(0).setNombreColectivo("Estudiantes");

        alumnos.get(1).setNombre("Francisco Gabriel");
        alumnos.get(1).setApellidos("Puga Lojo");
        alumnos.get(1).setEmail("franciscopuga@protonmail.com");
        alumnos.get(1).setNombreColectivo("Estudiantes");

        // Prueba de registro de alumnos
        model1.insertAlumnos(alumnos);
        model2.insertAlumnos(alumnos);

        // Prueba de inscripción de alumnos
        int size = getNumInscripciones("4");
        model1.insertInscripciones(alumnos, LocalDate.now().toString(), "4");
        Assert.assertEquals(size + 2, getNumInscripciones("4"));

        loadData();

        size = getNumInscripciones("4");
        model2.insertInscripciones(LocalDate.now().toString(), "4", alumnos, "1");
        Assert.assertEquals(size + 2, getNumInscripciones("4"));
    }

}
