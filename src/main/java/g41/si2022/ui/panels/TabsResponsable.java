package g41.si2022.ui.panels;

import g41.si2022.coiipa.consultar_ingresos_gastos.ConsultarIngresosGastosView;
import g41.si2022.coiipa.gestionar_curso.GestionarCursoView;
import g41.si2022.coiipa.registrar_curso.RegistrarCursoView;
import g41.si2022.ui.SwingMain;

public class TabsResponsable extends TabbedFrame {

    public TabsResponsable(SwingMain main) {
        super(main);

        tabs.put("Consultar ingresos y gastos", new ConsultarIngresosGastosView(main));
        tabs.put("Registrar cursos", new RegistrarCursoView(main));
        tabs.put("Modificar fechas", new GestionarCursoView(main));



        addTabs();
    }
}
