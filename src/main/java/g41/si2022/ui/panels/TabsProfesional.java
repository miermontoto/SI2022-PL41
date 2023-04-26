package g41.si2022.ui.panels;

import g41.si2022.coiipa.inscribir_usuario.InscribirUsuarioView;
import g41.si2022.coiipa.inscribir_multiples_usuarios_entidad.InscribirMultiplesUsuariosEntidadView;
import g41.si2022.coiipa.inscribir_multiples_usuarios.InscribirMultiplesUsuariosView;
import g41.si2022.ui.SwingMain;

public class TabsProfesional extends TabbedFrame {

    public TabsProfesional(SwingMain main) {
        super(main);

        tabs.put("Inscripción", new InscribirUsuarioView(main));
        tabs.put("Inscripción múltiple", new InscribirMultiplesUsuariosView(main));
        tabs.put("Inscripción múltiple (Entidades y Empresas)", new InscribirMultiplesUsuariosEntidadView(main));

        addTabs();
    }
}
