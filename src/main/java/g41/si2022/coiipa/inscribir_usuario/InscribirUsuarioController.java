package g41.si2022.coiipa.inscribir_usuario;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import g41.si2022.dto.AlumnoDTO;
import g41.si2022.dto.ColectivoDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.util.Dialog;
import g41.si2022.util.Util;
import g41.si2022.util.exception.ApplicationException;

public class InscribirUsuarioController extends g41.si2022.mvc.Controller<InscribirUsuarioView, InscribirUsuarioModel> {

	private static final String SIGN_IN = "sign-in";
	private static final String SIGN_UP = "sign-up";

	private List<CursoDTO> cursos;
	private List<AlumnoDTO> alumnos;
	boolean lleno = false;
	private String cursoId;
	private CursoDTO curso;

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
			getView().getTxtEmailLogin(),
			getView().getTxtEmail(),
			getView().getTxtNombre(),
			getView().getTxtApellidos(),
			getView().getTxtTelefono(),
			getView().getRadioSignin(),
			getView().getRadioSignup()
		).forEach(x -> x.addKeyListener(ka));

		Stream.of(
			getView().getRadioSignin(),
			getView().getRadioSignup()
		).forEach(x -> x.addActionListener(e -> SwingUtil.exceptionWrapper(this::manageForm)));

		getView().getBtnInscribir().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleInscripcion));
	}

	/**
	 * Método que introduce las inscripciones en la base de datos.
	 * Dependiendo de si se trata de un usuario nuevo o uno ya existente,
	 * se utilizan los datos existentes en la BBDD o no. Se comprueba que
	 * el alumno que se trata de introducir no esté ya inscrito en el curso.
	 * <p> Se envía un email de confirmación de inscripción.
	 */
	public void handleInscripcion() {
		if (curso == null) return;

		String email = "";
		AlumnoDTO alumno;
		switch (getView().getRadioSignin().isSelected() ? SIGN_IN : SIGN_UP) {
			case SIGN_IN:
				email = this.getView().getTxtEmailLogin().getText();
				break;

			case SIGN_UP:
				email = this.getView().getTxtEmail().getText();
				getModel().insertAlumno(
					getView().getTxtNombre().getText(),
					getView().getTxtApellidos().getText(),
					getView().getTxtEmail().getText(),
					getView().getTxtTelefono().getText()
				);
				break;

			default:
				throw new ApplicationException("Invalid radio button state");
		}

		try {
			alumno = getModel().getAlumnoFromEmail(email).get();
		} catch (Exception ex) {
			Dialog.showError("No existe ningún alumno con ese email");
			return;
		}


		if(alumnos.stream().anyMatch(x -> x.getId().equals(alumno.getId()))) {
			Dialog.showError("Ya está inscrito en este curso");
			return;
		}

		getModel().insertInscripcion(getToday().toString(),
			cursoId, alumno.getId(), getModel().getCostes(cursoId, ((ColectivoDTO) getView().getCbColectivo().getSelectedItem()).getNombre()));

		if(lleno) {
			String idInscripcion = this.getModel().getIdInscripcionFromCursoAlumno(alumno.getId(), cursoId).getId();
			getModel().insertListaEspera(idInscripcion, getToday().toString());
			getListaCursos();
			Dialog.show("Inscripción en la lista de espera realizada con éxito");
		} else {
			getListaCursos();
			Util.sendEmail(email, "COIIPA: Inscripción realizada", "Estimado alumno/a:\n\nSu inscripción al curso "
			 	+ SwingUtil.getSelectedKey(this.getView().getTablaCursos()) + " ha sido realizada con éxito.\n\n"
				+ "Su plaza no estará reservada hasta que se realice el pago completo del importe del curso.\n\n"
				+ "Reciba un cordial saludo,\nEquipo de gestión del COIIPA");
			Dialog.show("Inscripción realizada con éxito");
		}
	}

	public void manageForm() {
		getView().getBtnInscribir().setEnabled(false);
		if (cursoId == null) return;
		String signinEmail = this.getView().getTxtEmailLogin().getText();
		String signupEmail = this.getView().getTxtEmail().getText();

		if(signinEmail.isEmpty() && signupEmail.isEmpty()) return;

		getView().getLblSignin().setForeground(Color.RED);
		getView().getLblSignup().setForeground(Color.RED);

		boolean validEmail = false;
		JLabel target = null;
		String errorMsg = "";
		switch(getView().getRadioSignin().isSelected() ? SIGN_IN : SIGN_UP) {
			case SIGN_IN:
				target = this.getView().getLblSignin();
				if (!Util.verifyEmailStructure(signinEmail)) { // Check basic structure
					target.setText("");
					break;
				}
				validEmail = getModel().verifyEmail(signinEmail); // Check if email exists in database
				errorMsg = "Email desconocido";
				break;

			case SIGN_UP:
				target = getView().getLblSignup();
				if (!Util.verifyEmailStructure(signupEmail)) {
					getView().getLblSignup().setText("");
					break;
				}
				validEmail = !getModel().verifyEmail(signupEmail); // Check if email doesn't already exist in db
				errorMsg = "El email ya está registrado";
				break;

			default:
				throw new ApplicationException("Invalid radio button state");
		}

		target.setText(validEmail ? "" : errorMsg);
		getView().getBtnInscribir().setEnabled(validEmail);
	}

	public void updateCursoValue() {
		String targetId = Util.getInfoFromTable(getView().getTablaCursos(), 0);
		Optional<CursoDTO> temp = cursos.stream().filter(x -> x.getId().equals(targetId)).findFirst();
		if (!temp.isPresent()) {
			curso = null;
			Dialog.showError("Error a la hora de seleccionar curso.");
			return;
		}

		curso = temp.get();
		cursoId = curso.getId();
		alumnos = getModel().getAlumnosInCurso(cursoId);
		loadColectivosComboBox();

		if (curso.getPlazas_libres().equals("0") || Integer.valueOf(curso.getPlazas_libres()) < 0) {
			lleno = true;
			cursoId = curso.getId();
			this.getView().getBtnInscribir().setText("Añadirme a la lista de espera");
			return;
		}

		getView().getBtnInscribir().setText("Inscribirme ahora");
		lleno = false;
	}

	public void getListaCursos() {
		cursos = this.getModel().getListaCursos(getToday().toString());
		JTable table = getView().getTablaCursos();
		table.setModel(SwingUtil.getTableModelFromPojos(cursos, new String[] { "id", "nombre", "plazas_libres", "start_inscr", "end_inscr" },
			new String[] { "", "Nombre", "Plazas libres", "Fecha ini. inscr.", "Fecha fin inscr." }, null));
		Util.removeColumn(table, 0);
		this.loadColectivosComboBox();
		SwingUtil.autoAdjustColumns(table);
	}
	/**
	 * This method will update the contents of the colectivos ComboBox.
	 * @param cursos List of cursos ID to be added
	 */
	private void loadColectivosComboBox() {
		javax.swing.JComboBox<ColectivoDTO> cb = this.getView().getCbColectivo();
		cb.removeAllItems();
		this.getModel().getColectivos(cursoId).forEach(cb::addItem);
	}

}
