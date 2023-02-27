package g41.si2022.coiipa.inscribir_usuario;

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
