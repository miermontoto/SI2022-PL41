package g41.si2022.coiipa.modificar_curso;

import g41.si2022.mvc.*;

public class ModificarCursosController extends Controller<ModificarCursosView, ModificarCursosModel> {

    public ModificarCursosController(ModificarCursosModel model, ModificarCursosView view) {
        super(view, model);
    }

    @Override
    public void initNonVolatileData() {
        throw new UnsupportedOperationException("Unimplemented method 'initNonVolatileData'");
    }

    @Override
    public void initVolatileData() {
        throw new UnsupportedOperationException("Unimplemented method 'initVolatileData'");
    }

}
