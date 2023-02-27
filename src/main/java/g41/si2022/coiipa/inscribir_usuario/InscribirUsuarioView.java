package g41.si2022.coiipa.inscribir_usuario;

import java.awt.BorderLayout;

import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;

public class InscribirUsuarioView extends Tab {

    public InscribirUsuarioView(SwingMain main) {
        super(main);
        initialize();
    }

    private void initialize() {
        this.setLayout(new BorderLayout(0, 0));
    }

    @Override
    public void initController() { }

}
