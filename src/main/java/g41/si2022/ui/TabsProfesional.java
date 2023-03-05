package g41.si2022.ui;

import g41.si2022.coiipa.inscribir_usuario.InscribirUsuarioView;

public class TabsProfesional extends TabbedFrame {

    public TabsProfesional(SwingMain main) {
        super(main);

        tabs.put("Inscripción", new InscribirUsuarioView(main));

        addTabs();
    }
}
