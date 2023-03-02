package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;
import java.util.regex.Pattern;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.TableModel;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.util.SwingUtil;
import g41.si2022.util.Util;

public class InscribirUsuarioController {
    private InscribirUsuarioModel model;
    private InscribirUsuarioView view;
    private List<CursoDTO> cursos;
    private String cursoId;

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

        view.getTxtEmail().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SwingUtil.exceptionWrapper(() -> manageForm());
            }
        });

        view.getTxtNombre().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SwingUtil.exceptionWrapper(() -> manageForm());
            }
        });

        view.getTxtApellidos().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SwingUtil.exceptionWrapper(() -> manageForm());
            }
        });

        view.getTxtTelefono().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SwingUtil.exceptionWrapper(() -> manageForm());
            }
        });

        view.getRadioSignin().addActionListener(e -> SwingUtil.exceptionWrapper(() -> manageForm()));
        view.getRadioSignup().addActionListener(e -> SwingUtil.exceptionWrapper(() -> manageForm()));
    }

    public void manageForm() {
        view.getBtnInscribir().setEnabled(false);
        if (cursoId == null) return;

        if (view.getRadioSignin().isSelected()) { // Sign-in
            if (!view.getTxtEmailLogin().getText().isEmpty()) {
                view.getBtnInscribir().setEnabled(model.verifyAlumnoEmail(view.getTxtEmailLogin().getText()));
            }
            return;
        }

        // Sign-up
        if (view.getTxtEmail().getText().isEmpty() || view.getTxtNombre().getText().isEmpty() || view.getTxtApellidos().getText().isEmpty()
                || model.verifyAlumnoEmail(view.getTxtEmail().getText()) || !Util.verifyStructureEmail(view.getTxtEmail().getText()) ||
                    (view.getTxtTelefono().getText().length() != 9 && !view.getTxtTelefono().getText().isEmpty())) {
            view.getBtnInscribir().setEnabled(false);
            return;
        }

        view.getBtnInscribir().setEnabled(true); // If all checks were successful, enable button
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
