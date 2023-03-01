package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;

import javax.swing.table.TableModel;

import g41.si2022.coiipa.dto.CursoInscripcionDTO;
import g41.si2022.util.SwingUtil;

public class InscribirUsuarioController {
    private InscribirUsuarioModel model;
    private InscribirUsuarioView view;

    public InscribirUsuarioController(InscribirUsuarioModel m, InscribirUsuarioView v) {
        this.model = m;
        this.view = v;
        this.initView();
    }

    public void initView() {
        SwingUtil.exceptionWrapper(() -> getListaCursos());
    }

    public void getListaCursos() {
        List<CursoInscripcionDTO> cursos = model.getListaCursos();
        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursos, new String[] { "nombre", "plazas", "start_inscr", "end_inscr" },
        		new String[] { "Nombre", "Plazas", "Fecha ini. inscr.", "Fecha fin inscr." }, null);
        view.getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(view.getTablaCursos());
    }
}
