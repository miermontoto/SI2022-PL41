package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;
import java.util.stream.Stream;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import g41.si2022.dto.AlumnoDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.util.Util;

public class InscribirUsuarioController extends g41.si2022.mvc.Controller<InscribirUsuarioView, InscribirUsuarioModel> {

    private List<CursoDTO> cursos;
    private String cursoId;

    public InscribirUsuarioController(InscribirUsuarioModel m, InscribirUsuarioView v) {
    	super(v, m);
        this.cursoId = null;
    }

    @Override
    public void initVolatileData() {
    	this.getListaCursos();
    }

    @Override
    public void initNonVolatileData() {
    	this.getView().getTablaCursos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent ent) {
                SwingUtil.exceptionWrapper(() -> updateCursoValue());
                SwingUtil.exceptionWrapper(() -> manageForm());
            }
        });

    	KeyAdapter ka = new KeyAdapter () {
    		@Override
                public void keyReleased(KeyEvent evt) {
                    SwingUtil.exceptionWrapper(() -> manageForm());
                }
    	};

    	Stream.of(
    		this.getView().getTxtEmailLogin(),
            this.getView().getTxtEmail(),
            this.getView().getTxtNombre(),
            this.getView().getTxtApellidos(),
            this.getView().getTxtTelefono(),
            this.getView().getRadioSignin(),
            this.getView().getRadioSignup()
    	).forEach(x -> x.addKeyListener(ka));

    	Stream.of(
    		this.getView().getRadioSignin(),
    		this.getView().getRadioSignup()
    	).forEach(x -> x.addActionListener(e -> SwingUtil.exceptionWrapper( () -> manageForm())));

        this.getView().getBtnInscribir().addActionListener(e -> SwingUtil.exceptionWrapper(() -> manageMain()));
    }

    public void manageMain() {
        if (cursoId == null) return;

        String email = "";
        AlumnoDTO alumno;
        switch (this.getView().getRadioSignin().isSelected() ? "sign-in" : "sign-up") {
            case "sign-in":
                email = this.getView().getTxtEmailLogin().getText();
                break;
            case "sign-up":
                email = this.getView().getTxtEmail().getText();
                this.getModel().insertAlumno(
                    this.getView().getTxtNombre().getText(),
                    this.getView().getTxtApellidos().getText(),
                    this.getView().getTxtEmail().getText(),
                    this.getView().getTxtTelefono().getText()
                );
                break;
        }

        alumno = this.getModel().getAlumnoFromEmail(email).get(0);

        if(this.getModel().checkAlumnoInCurso(alumno.getId(), cursoId)) {
            SwingUtil.showMessage("Ya está inscrito en este curso", "Inscripción de alumno", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.getModel().insertInscripcion(LocalDate.now().toString(), cursoId, alumno.getId());
        getListaCursos();
        Util.sendEmail(email, "COIIPA: Inscripción realizada", "Su inscripción al curso " + SwingUtil.getSelectedKey(this.getView().getTablaCursos()) + " ha sido realizada con éxito.");

        SwingUtil.showMessage("Inscripción realizada con éxito", "Inscripción de alumno");
    }

    public void manageForm() {
        this.getView().getBtnInscribir().setEnabled(false);
        if (cursoId == null) return;
        String signinEmail = this.getView().getTxtEmailLogin().getText();
        String signupEmail = this.getView().getTxtEmail().getText();

        if(signinEmail.isEmpty() && signupEmail.isEmpty()) return;

        this.getView().getLblSignin().setForeground(Color.RED);
        this.getView().getLblSignup().setForeground(Color.RED);

        boolean validEmail = false;
        JLabel target = null;
        String errorMsg = "";
        switch(this.getView().getRadioSignin().isSelected() ? "sign-in" : "sign-up") {
            case "sign-in":
                target = this.getView().getLblSignin();
                if (!Util.verifyEmailStructure(signinEmail)) { // Check basic structure
                    target.setText("");
                    break;
                }
                validEmail = this.getModel().verifyEmail(signinEmail); // Check if email exists in database
                errorMsg = "Email desconocido";
                break;

            case "sign-up":
                target = this.getView().getLblSignup();
                if (!Util.verifyEmailStructure(signupEmail)) {
                    this.getView().getLblSignup().setText("");
                    break;
                }
                validEmail = !this.getModel().verifyEmail(signupEmail); // Check if email doesn't already exist in db
                errorMsg = "El email ya está registrado";
                break;
        }
        target.setText(validEmail ? "" : errorMsg);
        this.getView().getBtnInscribir().setEnabled(validEmail);
    }

    public void updateCursoValue() {
        cursoId = null;
        for (CursoDTO curso : cursos) {
            if (curso.getNombre().equals(SwingUtil.getSelectedKey(this.getView().getTablaCursos()))) {
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
        cursos = this.getModel().getListaCursos(this.getView().getMain().getToday().toString());
        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursos, new String[] { "nombre", "plazas_libres", "start_inscr", "end_inscr" },
        		new String[] { "Nombre", "Plazas libres", "Fecha ini. inscr.", "Fecha fin inscr." }, null);
        this.getView().getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(this.getView().getTablaCursos());
    }
}
