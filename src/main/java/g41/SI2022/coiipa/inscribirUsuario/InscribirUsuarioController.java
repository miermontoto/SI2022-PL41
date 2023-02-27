package g41.SI2022.coiipa.inscribirUsuario;

public class InscribirUsuarioController {
    private InscribirUsuarioModel model;
    private InscribirUsuarioView view;

    public InscribirUsuarioController(InscribirUsuarioModel m, InscribirUsuarioView v) {
        this.model = m;
        this.view = v;
        this.initView();
    }

    public void initView() { }
}
