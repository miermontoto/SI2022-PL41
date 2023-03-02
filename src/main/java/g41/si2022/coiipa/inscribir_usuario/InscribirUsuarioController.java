package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.TableModel;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.util.ApplicationException;
import g41.si2022.util.SwingUtil;

public class InscribirUsuarioController {
    private InscribirUsuarioModel model;
    private InscribirUsuarioView view;
    private List<CursoDTO> cursos;
    private String cursoId;

    public InscribirUsuarioController(InscribirUsuarioModel m, InscribirUsuarioView v) {
        this.model = m;
        this.view = v;
        this.initView();
    }

    public void initView() {
        SwingUtil.exceptionWrapper(() -> getListaCursos());
        view.getTablaCursos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent ent) {
                SwingUtil.exceptionWrapper(() -> updateCursoValue());
            }
        });
    }

    public void updateCursoValue() {
        for (CursoDTO curso : cursos) {
            if (curso.getNombre().equals(SwingUtil.getSelectedKey(view.getTablaCursos()))) {
                cursoId = curso.getId();
                return;
            }
        }
        throw new ApplicationException("Curso seleccionado desconocido");
    }

    public void getListaCursos() {
        cursos = model.getListaCursos();
        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursos, new String[] { "nombre", "plazas", "start_inscr", "end_inscr" },
        		new String[] { "Nombre", "Plazas", "Fecha ini. inscr.", "Fecha fin inscr." }, null);
        view.getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(view.getTablaCursos());
    }
}
