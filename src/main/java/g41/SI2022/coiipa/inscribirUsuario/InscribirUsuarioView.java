package g41.SI2022.coiipa.inscribirUsuario;

import java.awt.BorderLayout;

import g41.SI2022.util.Tab;
import g41.SI2022.util.SwingMain;

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
