package g41.si2022.coiipa.modificar_curso;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.*;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.DocenciaDTO;
import g41.si2022.dto.SesionDTO;

public class ModificarCursoTest extends g41.si2022.coiipa.TestCase {

    private static ModificarCursosModel model;

    @Override
    public void loadData() {
        try {this.getDatabase().executeScript("src/test/java/g41/si2022/coiipa/modificar_curso/try.sql");}
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @BeforeClass
    public static void initialize() {
        model = new ModificarCursosModel();
    }

    private CursoDTO getDTO(String id) {
        CursoDTO curso = model.getCurso(id);
        curso.updateState(LocalDate.now());
        return curso;
    }

    /*
     * Referencias para el test (ids de curso):
     *
     * - PLANEADO: 5
     * - EN_INSCRIPCION: 4
     * - INSCRIPCION_CERRADA: 3
     * - EN_CURSO: 2
     * - FINALIZADO: 1
     */

    @Test
    public void testInfo() {
        CursoDTO curso;
        /*
         * La siguiente información de un curso se puede modificar dependiendo de su estado:
         *
         * - PLANEADO, EN_INSCRIPCION, INSCRIPCION_CERRADA: todo, siempre que las plazas
         *   sean mayores que el número de inscritos.
         * - EN_CURSO: solo descripción.
         * - FINALIZADO, CERRADO: nada.
         */

        // PLANEADO
        curso = getDTO("5");
        Assert.assertTrue("PLANEADO (todo ok)", model.updateCurso(curso, "Nuevo nombre", "Nueva descripción", "100"));
        Assert.assertFalse("PLANEADO (plazas insuficientes)", model.updateCurso(curso, "Nuevo nombre", "Nueva descripción", "1"));

        // EN_INSCRIPCION
        curso = getDTO("4");
        Assert.assertTrue("EN_INSCRIPCION (todo ok)", model.updateCurso(curso, "Nuevo nombre", "Nueva descripción", "100"));
        Assert.assertFalse("EN_INSCRIPCION (plazas insuficientes)", model.updateCurso(curso, "Nuevo nombre", "Nueva descripción", "1"));

        // INSCRIPCION_CERRADA
        curso = getDTO("3");
        Assert.assertTrue("INSCRIPCION_CERRADA (todo ok)", model.updateCurso(curso, "Nuevo nombre", "Nueva descripción", "100"));
        Assert.assertFalse("INSCRIPCION_CERRADA (plazas insuficientes)", model.updateCurso(curso, "Nuevo nombre", "Nueva descripción", "1"));

        // EN_CURSO
        curso = getDTO("2");
        Assert.assertTrue("EN_CURSO (descripción)", model.updateCurso(curso, curso.getNombre(), "Nueva descripción", curso.getPlazas()));
        Assert.assertFalse("EN_CURSO (nombre)", model.updateCurso(curso, "Nuevo nombre", curso.getDescripcion(), curso.getPlazas()));
        Assert.assertFalse("EN_CURSO (plazas)", model.updateCurso(curso, curso.getNombre(), curso.getDescripcion(), "1"));

        // FINALIZADO
        curso = getDTO("1");
        Assert.assertFalse("FINALIZADO (descripción)", model.updateCurso(curso, curso.getNombre(), "desc test", curso.getPlazas()));
        Assert.assertFalse("FINALIZADO (nombre)", model.updateCurso(curso, "Nuevo nombre", curso.getDescripcion(), curso.getPlazas()));
        Assert.assertFalse("FINALIZADO (plazas)", model.updateCurso(curso, curso.getNombre(), curso.getDescripcion(), "1"));
    }

    @Test
    public void testSesiones() {
        /*
         * Las sesiones de un curso se pueden modificar dependiendo de su estado:
         *
         * - PLANEADO, EN_INSCRIPCION, INSCRIPCION_CERRADA: todo.
         * - EN_CURSO: añadir y eliminar sesiones posteriores a la fecha de hoy.
         * - FINALIZADO, CERRADO: nada.
         */

        CursoDTO curso;
        List<SesionDTO> sesiones;

        // PLANEADO
        curso = getDTO("5");
        sesiones = model.getSesionesFromCurso(curso.getId());
        Assert.assertTrue("PLANEADO (subset 0-1)", model.updateSesiones(curso, sesiones.subList(0, 1), LocalDate.now()));

        // EN_INSCRIPCION
        curso = getDTO("4");
        sesiones = model.getSesionesFromCurso(curso.getId());
        Assert.assertTrue("EN_INSCRIPCION (subset 0-1)", model.updateSesiones(curso, sesiones.subList(0, 1), LocalDate.now()));

        // INSCRIPCION_CERRADA
        curso = getDTO("3");
        sesiones = model.getSesionesFromCurso(curso.getId());
        Assert.assertTrue("INSCRIPCION_CERRADA (subset 0-1)", model.updateSesiones(curso, sesiones.subList(0, 1), LocalDate.now()));

        LocalDate dateToTest = LocalDate.parse("2023-05-20");

        // EN_CURSO
        curso = getDTO("2");
        sesiones = model.getSesionesFromCurso(curso.getId());
        Assert.assertTrue("EN_CURSO (subset pasado)", model.updateSesiones(curso, sesiones.stream().filter(s -> LocalDate.parse(s.getFecha()).isBefore(dateToTest)).collect(Collectors.toList()), dateToTest));
        Assert.assertFalse("EN_CURSO (subset futuro)", model.updateSesiones(curso, sesiones.stream().filter(s -> LocalDate.parse(s.getFecha()).isAfter(dateToTest)).collect(Collectors.toList()), dateToTest));

        // FINALIZADO
        curso = getDTO("1");
        sesiones = model.getSesionesFromCurso(curso.getId());
        Assert.assertFalse("FINALIZADO (subset 0-1)", model.updateSesiones(curso, sesiones.stream().filter(s -> LocalDate.parse(s.getFecha()).isBefore(dateToTest)).collect(Collectors.toList()), LocalDate.now()));
    }

    @Test
    public void testCostes() {
        /*
         * Los costes de un curso se pueden modificar:
         *
         * - Si el curso no tiene inscripciones, todo.
         * - Si el curso tiene inscripciones o está cerrado, nada.
         */

        // Cursos con y sin inscripciones
        Assert.assertTrue("Sin inscripciones", model.updateCostes(getDTO("6"), new LinkedList<>()));
        Assert.assertFalse("Con inscripciones", model.updateCostes(getDTO("2"), new LinkedList<>()));
    }

    @Test
    public void testDocencias() {
        /*
         * Las docencias de un curso se pueden modificar:
         *
         * - Si el curso no está en progreso, todo.
         * - Si el curso está en progreso, solo añadir nuevas docencias.
         * - Si el curso está finalizado, solo modificar acuerdos ya existentes.
         * - Si el curso está cerrado, nada.
         */

        CursoDTO curso;
        List<DocenciaDTO> docencias;

        // PLANEADO
        curso = getDTO("5");
        Assert.assertTrue("PLANEADO (todo)", model.updateDocencias(curso, new LinkedList<>()));

        // EN_INSCRIPCION
        curso = getDTO("4");
        Assert.assertTrue("EN_INSCRIPCION (todo)", model.updateDocencias(curso, new LinkedList<>()));

        // INSCRIPCION_CERRADA
        curso = getDTO("3");
        Assert.assertTrue("INSCRIPCION_CERRADA (todo)", model.updateDocencias(curso, new LinkedList<>()));

        // EN_CURSO
        curso = getDTO("2");
        docencias = model.getListaProfesores(curso.getId());
        docencias.add(new DocenciaDTO(curso.getId(), "3", "100"));
        Assert.assertTrue("EN_CURSO (añadir)", model.updateDocencias(curso, docencias));
        Assert.assertFalse("EN_CURSO (eliminar)", model.updateDocencias(curso, docencias.subList(0, 1)));
        docencias.get(0).setRemuneracion("30");
        Assert.assertFalse("EN_CURSO (modificar)", model.updateDocencias(curso, docencias));

        // FINALIZADO
        curso = getDTO("1");
        docencias = model.getListaProfesores(curso.getId());
        docencias.add(new DocenciaDTO(curso.getId(), "3", "100"));
        Assert.assertFalse("FINALIZADO (añadir)", model.updateDocencias(curso, docencias));
        Assert.assertFalse("FINALIZADO (eliminar)", model.updateDocencias(curso, docencias.subList(0, 1)));
        docencias = model.getListaProfesores(curso.getId());
        docencias.get(0).setRemuneracion("30");
        Assert.assertTrue("FINALIZADO (modificar)", model.updateDocencias(curso, docencias));
    }

}
