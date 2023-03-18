package g41.si2022.ui.panels;

import g41.si2022.coiipa.estado_actividades.EstadoActividadesView;
import g41.si2022.coiipa.insertar_devolucion.InsertarDevolucionView;
import g41.si2022.coiipa.lista_actividades.ListaActividadesView;
import g41.si2022.coiipa.registrar_pago_alumno.RegistrarPagoAlumnoView;
import g41.si2022.coiipa.registrar_pago_profesor.RegistrarPagoProfesorView;
import g41.si2022.ui.SwingMain;

public class TabsSecretaria extends TabbedFrame {

    public TabsSecretaria(SwingMain main) {
        super(main);

        tabs.put("Registrar pagos de alumnos", new RegistrarPagoAlumnoView(main));
        tabs.put("Estado de actividades de formación", new EstadoActividadesView(main));
        tabs.put("Registrar pagos a profesores", new RegistrarPagoProfesorView(main));
        tabs.put("Cancelar inscripciones", new InsertarDevolucionView(main));
        tabs.put("Lista de actividades de formación", new ListaActividadesView(main));

        addTabs();
    }
}
