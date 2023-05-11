package g41.si2022.coiipa.gestionar_curso;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.*;

import g41.si2022.coiipa.gestionar_inscripciones.GestionarInscripcionesModel;
import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;

public class GestionarCursoTest extends g41.si2022.coiipa.TestCase {

    private static GestionarCursoModel model;

    @Override
    public void loadData() {
        try {
            this.getDatabase().executeScript("src/test/java/g41/si2022/coiipa/gestionar_curso/datos.sql");
        } catch (Exception e) {
            System.exit(1);
        }
    }
    
    @BeforeClass
    public static void initialize() {
        model = new GestionarCursoModel();
    }

    private CursoDTO getCursoDTO(String id) {
        // Obtener curso en función de la id
        CursoDTO curso = model.getCurso(id);
        // Calcular estado del curso
        curso.updateEstado(LocalDate.now());

        return curso;
    }

    private List<InscripcionDTO> getInscrCurso(String idCurso) {
        // Obtener inscripciones del curso pasado por parámetro
        List<InscripcionDTO> inscripciones = model.getCursoInscripciones(idCurso);
        // Calcular estado de las isncripciones
        for (InscripcionDTO inscripcion: inscripciones)
            inscripcion.updateEstado(LocalDate.now());

        return inscripciones;
    }
    
    /**
     * Controlar el estado de un curso para su posterior cancelación.
     */
    @Test
    public void testEstadoCurso() {
        CursoDTO curso;
        /*
         * Sólo se puede cancelar un curso con estado EN_INSCRIPCION o PLANEADO.
         * Los datos introducidos para este test son:
         *     - Curso con estado EN_INSCRIPCION (id:1)
         *     - Curso con estado EN_CURSO (id:2)
         *     - Curso con estado PLANEADO (id:3)
         *     - Curso con estado FINALIZADO (id:4)
         *     - Curso con estado CANCELADO (id:5)
         */

        // Curso con estado EN_INSCRIPCION
        curso = getCursoDTO("1");
        // Debería devolver cierto
        assertTrue(model.cancelarCursoTest(curso));

        // Curso con estado EN_CURSO
        curso = getCursoDTO("2");
        // Debería devolver falso
        assertFalse(model.cancelarCursoTest(curso));

        // Curso con estado PLANEADO
        curso = getCursoDTO("3");
        // Debería devolver cierto
        assertTrue(model.cancelarCursoTest(curso));

        // Curso con estado FINALIZADO
        // Debería devolver falso
        curso = getCursoDTO("4");
        assertFalse(model.cancelarCursoTest(curso));

        // Curso con estado CANCELADO
        // Debería devolver falso
        curso = getCursoDTO("5");
        assertFalse(model.cancelarCursoTest(curso));
    }

    @Test
    public void testInscripciones() {
        // Obtener curso con estado EN_INSCRIPCION
        CursoDTO curso_EN_INSCRIPCION = getCursoDTO("1");
        // Obtener curso con estado PLANEADO
        CursoDTO curso_PLANEADO = getCursoDTO("2");
        List<InscripcionDTO> inscripciones;

        /*
         * Se probará la cancelación de inscripciones de los cursos que se pueden cancelar 
         * (EN_INSCRIPCIÓN y PLANEADO).
         * 
         * Para este test se insertan los siguientes datos:
         *     - Inscripción con estado PAGADA (id:1) del curso EN_INSCRIPCION (id:1)
         *     - Inscripción con estado PENDIENTE (id:2) del curso EN_INSCRIPCION (id:1)
         *     - Inscripción con estado CANCELADA (id:3) del curso EN_INSCRIPCION (id:1)
         * 
         * El curso en estado PLANEADO no puede tener inscripciones, ya que aún no ha 
         * comenzado el periodo de inscripción. Por lo tanto, no tiene inscripciones para
         * cancelar.
         */ 

        // Comprobar proceso de cancelación de inscripciones para el curso EN_INSCRIPCIÓN, 
        // que tiene tres inscripciones, debe devolver cierto
        assertTrue(model.cancelarInscrCursoTest(curso_EN_INSCRIPCION));

        // Comprobar proceso de cancelación de inscripciones para el curso PLANEADO, no tiene
        // inscripciones, debe devolver falso
        assertFalse(model.cancelarInscrCursoTest(curso_PLANEADO));
       

        // Obtener inscripciones del curso en estado EN_INSCRIPCION
        inscripciones = getInscrCurso("1");
        InscripcionDTO inscripcion1 = inscripciones.get(0);
        InscripcionDTO inscripcion2 = inscripciones.get(1);
        InscripcionDTO inscripcion3 = inscripciones.get(2);
       
        // Debe devolver cierto, se imprime un mensaje indicando que se tiene que realizar
        // una devolución
        assertTrue(model.cancelarInscripcionTest(inscripcion1));

        // Debe devolver cierto, se indica que no se debe realizar devolución
        assertTrue(model.cancelarInscripcionTest(inscripcion2));

        // Debe devolver falso
        assertFalse(model.cancelarInscripcionTest(inscripcion3));
    }
}
