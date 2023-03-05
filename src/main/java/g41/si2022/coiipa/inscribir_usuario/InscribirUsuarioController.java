package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
        view = v;
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

        view.getTxtEmailLogin().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                SwingUtil.exceptionWrapper(() -> manageForm());
            }
        });

        view.getTxtEmail().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                SwingUtil.exceptionWrapper(() -> manageForm());
            }
        });

        view.getTxtNombre().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                SwingUtil.exceptionWrapper(() -> manageForm());
            }
        });

        view.getTxtApellidos().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                SwingUtil.exceptionWrapper(() -> manageForm());
            }
        });

        view.getTxtTelefono().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
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

        model.insertInscripcion(LocalDate.now().toString(), cursoId, alumno.getId());

        if(model.checkAlumnoInCurso(alumno.getId(), cursoId)) {
            SwingUtil.showMessage("Ya está inscrito en este curso", "Inscripción de alumno", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        model.insertInscripcion(LocalDate.now().toString(), cursoId, alumno.getId());
        getListaCursos();
        Util.sendEmail(email, "COIIPA: Inscripción realizada", "Su inscripción al curso " + SwingUtil.getSelectedKey(view.getTablaCursos()) + " ha sido realizada con éxito.");

        SwingUtil.showMessage("Inscripción realizada con éxito", "Inscripción de alumno");
    }

    public void manageForm() {
        view.getBtnInscribir().setEnabled(false);
        if (cursoId == null) return;
        String signinEmail = view.getTxtEmailLogin().getText();
        String signupEmail = view.getTxtEmail().getText();

        if(signinEmail.isEmpty() && signupEmail.isEmpty()) return;

        view.getLblSignin().setForeground(Color.RED);
        view.getLblSignup().setForeground(Color.RED);

        boolean validEmail = false;
        JLabel target = null;
        switch(view.getRadioSignin().isSelected() ? "sign-in" : "sign-up") {
            case "sign-in":
                target = view.getLblSignin();
                if (!Util.verifyEmailStructure(signinEmail)) { // Check basic structure
                    target.setText("");
                    break;
                }
                validEmail = model.verifyEmail(signinEmail); // Check if email exists in database
                break;

            case "sign-up":
                if (!Util.verifyEmailStructure(signupEmail)) {
                    view.getLblSignup().setText("");
                    break;
                }
                validEmail = !model.verifyEmail(signupEmail); // Check if email doesn't already exist in db
                break;
        }
        target.setText(validEmail ? "" : "Email desconocido");
        view.getBtnInscribir().setEnabled(validEmail);
    }

    public void updateCursoValue() {
        cursoId = null;
        for (CursoDTO curso : cursos) {
            if (curso.getNombre().equals(SwingUtil.getSelectedKey(view.getTablaCursos()))) {
                if (curso.getPlazas_libres().equals("0")) {
                    SwingUtil.showMessage("No quedan plazas libres para este curso", "Inscripción de alumno", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                cursoId = curso.getId();
                return;
            }
        }
    }

    public void getListaCursos() {
        cursos = model.getListaCursos(view.getMain().getToday().toString());
        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursos, new String[] { "nombre", "plazas_libres", "start_inscr", "end_inscr" },
        		new String[] { "Nombre", "Plazas libres", "Fecha ini. inscr.", "Fecha fin inscr." }, null);
        view.getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(view.getTablaCursos());
    }
}
