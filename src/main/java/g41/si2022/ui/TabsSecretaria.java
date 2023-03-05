package g41.si2022.ui;

import g41.si2022.coiipa.consultar_cursos.ConsultarCursosView;
import g41.si2022.coiipa.registrar_pago.RegistrarPagoView;

public class TabsSecretaria extends TabbedFrame {

    public TabsSecretaria(SwingMain main) {
        super(main);

        tabs.put("Registrar pagos", new RegistrarPagoView(main));
        tabs.put("Consultar cursos", new ConsultarCursosView(main));

        addTabs();
    }
}
