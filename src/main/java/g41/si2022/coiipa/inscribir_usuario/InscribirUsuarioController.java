package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.awt.Color;

import javax.swing.table.TableModel;

import g41.si2022.coiipa.dto.AlumnoDTO;
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

        view.getBtnInscribir().addActionListener(e -> SwingUtil.exceptionWrapper(() -> manageMain()));
        view.getRadioSignin().addActionListener(e -> SwingUtil.exceptionWrapper(() -> manageForm()));
        view.getRadioSignup().addActionListener(e -> SwingUtil.exceptionWrapper(() -> manageForm()));
    }

    public void manageMain() {
        if (cursoId == null) return;

        String email = "";
        AlumnoDTO alumno;
        switch (view.getRadioSignin().isSelected() ? "sign-in" : "sign-up") {
            case "sign-in":
                email = view.getTxtEmailLogin().getText();
                break;
            case "sign-up":
                email = view.getTxtEmail().getText();
                model.insertAlumno(
                    view.getTxtNombre().getText(),
                    view.getTxtApellidos().getText(),
                    view.getTxtEmail().getText(),
                    view.getTxtTelefono().getText()
                );
                break;
        }

        alumno = model.getAlumnoFromEmail(email).get(0);
        model.insertInscripcion(LocalDate.now().toString(), "Pendiente", cursoId, alumno.getId());
    }

    public void manageForm() {
        view.getBtnInscribir().setEnabled(false);
        if (cursoId == null) return;
        String signinEmail = view.getTxtEmailLogin().getText();
        String signupEmail = view.getTxtEmail().getText();

        if(signinEmail.isEmpty() && signupEmail.isEmpty()) return;

        view.getLblSignin().setForeground(Color.RED);
        view.getLblSignup().setForeground(Color.RED);

        boolean validEmail;
        switch(view.getRadioSignin().isSelected() ? "sign-in" : "sign-up") {
            case "sign-in":
                if (!Util.verifyEmailStructure(signinEmail)) { // Check basic structure
                    view.getLblSignin().setText("");
                    break;
                }
                validEmail = model.verifyEmail(signinEmail); // Check if email exists in database
                view.getLblSignin().setText(validEmail ? "" : "Email desconocido");
                view.getBtnInscribir().setEnabled(validEmail);
                break;

            case "sign-up":
                if (!Util.verifyEmailStructure(signupEmail)) {
                    view.getLblSignup().setText("");
                    break;
                }
                validEmail = !model.verifyEmail(signupEmail); // Check if email doesn't already exist in db
                view.getLblSignup().setText(validEmail ? "" : "Email ya registrado");
                view.getBtnInscribir().setEnabled(validEmail);
                break;
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
