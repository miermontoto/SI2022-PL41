package g41.si2022.coiipa.inscribir_usuario;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.table.TableModel;

import g41.si2022.dto.AlumnoDTO;
import g41.si2022.dto.ColectivoDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.util.Dialog;
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

    	KeyAdapter ka = new KeyAdapter() {
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

        this.getView().getBtnInscribir().addActionListener(e -> SwingUtil.exceptionWrapper(() -> handleInscripcion()));
    }

    /**
     * Método que introduce las inscripciones en la base de datos.
     * Dependiendo de si se trata de un usuario nuevo o uno ya existente,
     * se utilizan los datos existentes en la BBDD o no. Se comprueba que
     * el alumno que se trata de introducir no esté ya inscrito en el curso.
     * <p> Se envía un email de confirmación de inscripción.
     */
    public void handleInscripcion() {
        if (cursoId == null) return;

        String email = "";
        AlumnoDTO alumno;
        switch (this.getView().getRadioSignin().isSelected() ? "sign-in" : "sign-up") {
            case "sign-in":
                email = this.getView().getTxtEmailLogin().getText();
                break;
            case "sign-up":
                email = this.getView().getTxtEmail().getText();
                getModel().insertAlumno(
                    getView().getTxtNombre().getText(),
                    getView().getTxtApellidos().getText(),
                    getView().getTxtEmail().getText(),
                    getView().getTxtTelefono().getText()
                );
                break;
        }

        alumno = getModel().getAlumnoFromEmail(email);

        if(getModel().checkAlumnoInCurso(alumno.getId(), cursoId)) {
            Dialog.showError("Ya está inscrito en este curso");
            return;
        }

        this.getModel().insertInscripcion(getView().getMain().getToday().toString(),
            cursoId, alumno.getId(), ((ColectivoDTO) getView().getCbColectivo().getSelectedItem()).getId());
        getListaCursos();
        Util.sendEmail(email, "COIIPA: Inscripción realizada", "Su inscripción al curso " + SwingUtil.getSelectedKey(this.getView().getTablaCursos()) + " ha sido realizada con éxito.");

        Dialog.show("Inscripción realizada con éxito");
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
                    Dialog.showError("No quedan plazas libres para este curso");
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
        this.loadColectivosComboBox();
        SwingUtil.autoAdjustColumns(this.getView().getTablaCursos());
    }

    /**
     * This method will update the contents of the colectivos ComboBox.
     * @param cursos List of cursos ID to be added
     */
    @SuppressWarnings("unchecked")
    private void loadColectivosComboBox() {
    	javax.swing.JComboBox cb = this.getView().getCbColectivo();
    	cb.removeAllItems(); // Clear the cb
    	this.getModel().getColectivos().forEach(c -> cb.addItem(c));
    }
}
