package g41.si2022.ui;

import g41.si2022.coiipa.consultar_ingresos_gastos.ConsultarIngresosGastosView;
import g41.si2022.coiipa.registrar_curso.RegistrarCursoView;

public class TabsResponsable extends TabbedFrame {

    public TabsResponsable(SwingMain main) {
        super(main);
        frame.setTitle("COIIPA: Panel del responsable de formación");

        tabs.put("Consultar ingresos y gastos", new ConsultarIngresosGastosView(main));
        tabs.put("Consultar cursos", new RegistrarCursoView(main));

        addTabs();
    }
}
