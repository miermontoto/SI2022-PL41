package g41.si2022.coiipa.gestionar_curso;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.List;

import org.junit.*;

import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;

import g41.si2022.coiipa.gestionar_inscripciones.GestionarInscripcionesModel;
import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;

public class CancelarInscripcinesTest extends g41.si2022.coiipa.TestCase {
    
    private static GestionarInscripcionesModel model;

    @Override
    public void loadData() {
        try {
            this.getDatabase().executeScript("src/test/java/g41/si2022/coiipa/cancelar_inscripciones/datos.sql");
        } catch (Exception e) {
            System.exit(1);
        }
    }

    @BeforeClass
    public static void initialize() {
        model = new GestionarCursoModel();
    }

    /**
     * Método para obtener una inscripción dado su id
     * @param idInscr id de la inscripción
     * @return inscripción correspondiente a dicho id
     */
    public InscripcionDTO getInscripcion(String idInscr) {
        InscripcionDTO inscripcion = model.getInscrById(idInscr);
        inscripcion.updateEstado(LocalDate.today());

        return inscripcion;
    }

    /**
     * Método para obtener el curso mediante su id
     * @param idInscripcion id del curso
     * @return curso asociado a esa id
     */
    public CursoDTO getCursoFromInscripcion(String idCurso) {
        CursoDTO curso = model.getCursoFromInscr(idCurso);
        curso.updateEstado(LocalDate.today());

        return curso;
    }

    @Test 
    public void testEstadoCancelarInscripcion() {
        // Obtener la primera inscripción almacenada. Estado PENDIENTE.
        InscripcionDTO inscripcion = getInscripcion("1");

        // Se comprueba si se puede cancelar. Debe devolver cierto.
        assertTrue(model.cancelarInscripcionTest(inscripcion));

        // Obtener la segunda inscripción almacenada. Estado CANCELADA.
        inscripcion = getInscripcion("2");

        // Se comprueba si se puede cancelar. Debe devolver falso.
        assertTrue(model.cancelarInscripcionTest(inscripcion));

        /**
         * No se comprueba la cancelación para el resto de estados de una
         * inscripción ya que resulta redundante. Siempre se debe de poder
         * cancelar una inscripción exceptuando una que ya se haya cancelado.
         */
    }

    @Test 
    public void testEstadoCursoCancelarInscripcion() {
        // Obtener primera inscripción almacenada a cancelar. Estado PENDIENTE.
        InscripcionDTO inscripcionACancelar1 = getInscripcion("1");

        // Obtener curso asociado a esa inscripción
        CursoDTO curso1 = getCursoFromInscripcion(inscripcionACancelar1.getCurso_id());

        // Comprobar el estado del curso para cancelar la inscripción. Se trata
        // de un curso con estado EN_INSCRIPCION, luego la inscripción se puede
        // cancelar y debe devolver cierto
        assertTrue(model.cancelarInscripcionEstadoCursoTest(curso1));

        // Obtener la tercera inscripción almacenada a cancelar. Estado PENDIENTE. Puede
        // cancelarse
        InscripcionDTO inscripcionACancelar3 = getInscripcion("3");

        // Obtener curso asociado a la inscripción 3
        CursoDTO curso2 = getCursoFromInscripcion(inscripcionACancelar3.getCurso_id());

        // Comprobar el estado del curso para cancelar la inscripción. Se trata de un curso
        // con estado EN_CURSO, luego la inscripción no se puede cancelar. Debe devolver falso
        assertFalse(model.cancelarInscripcionEstadoCursoTest(curso2));

    }
}
