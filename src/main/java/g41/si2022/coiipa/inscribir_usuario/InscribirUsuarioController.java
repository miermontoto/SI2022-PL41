package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;
import java.util.regex.Pattern;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.TableModel;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.util.SwingUtil;

public class InscribirUsuarioController {
    private InscribirUsuarioModel model;
    private InscribirUsuarioView view;
    private List<CursoDTO> cursos;
    private String cursoId;

    private static String regexEmail = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public InscribirUsuarioController(InscribirUsuarioModel m, InscribirUsuarioView v) {
        this.model = m;
        this.view = v;
        cursoId = null;
        this.initView();
    }

    public void initView() {
        SwingUtil.exceptionWrapper(() -> getListaCursos());
        view.getTablaCursos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent ent) {
                SwingUtil.exceptionWrapper(() -> updateCursoValue());
                SwingUtil.exceptionWrapper(() -> manageForm());
            }
        });

        view.getTxtEmailLogin().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SwingUtil.exceptionWrapper(() -> manageForm());
            }
        });
    }

    public void manageForm() {
        if (cursoId == null) {
            view.getBtnInscribir().setEnabled(false);
            return;
        }

        if (view.getRadioSignin().isSelected()) {
            if (!view.getTxtEmailLogin().getText().isEmpty()) {
                view.getBtnInscribir().setEnabled(Pattern.compile(regexEmail).matcher(view.getTxtEmailLogin().getText()).matches());
            }
        }
    }

    public void updateCursoValue() {
        for (CursoDTO curso : cursos) {
            if (curso.getNombre().equals(SwingUtil.getSelectedKey(view.getTablaCursos()))) {
                cursoId = curso.getId();
                return;
            }
        }
        cursoId = null;
    }

    public void getListaCursos() {
        cursos = model.getListaCursos();
        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursos, new String[] { "nombre", "plazas", "start_inscr", "end_inscr" },
        		new String[] { "Nombre", "Plazas", "Fecha ini. inscr.", "Fecha fin inscr." }, null);
        view.getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(view.getTablaCursos());
    }
}
