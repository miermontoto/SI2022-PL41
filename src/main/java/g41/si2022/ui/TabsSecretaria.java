package g41.si2022.ui;

import g41.si2022.coiipa.consultar_cursos.ConsultarCursosView;
import g41.si2022.coiipa.gestionar_cursos.GestionarCursosView;
import g41.si2022.coiipa.insertar_devolucion.InsertarDevolucionView;
import g41.si2022.coiipa.registrar_pago_alumno.RegistrarPagoAlumnoView;
import g41.si2022.coiipa.registrar_pago_profesor.RegistrarPagoProfesorView;

public class TabsSecretaria extends TabbedFrame {

    public TabsSecretaria(SwingMain main) {
        super(main);

        tabs.put("Registrar pagos de alumnos", new RegistrarPagoAlumnoView(main));
        tabs.put("Consultar cursos", new ConsultarCursosView(main));
        tabs.put("Registrar pagos a profesores", new RegistrarPagoProfesorView(main));
        tabs.put("Cancelar inscripciones", new InsertarDevolucionView(main));
        tabs.put("Gestionar cursos", new GestionarCursosView(main));

        addTabs();
    }
}
