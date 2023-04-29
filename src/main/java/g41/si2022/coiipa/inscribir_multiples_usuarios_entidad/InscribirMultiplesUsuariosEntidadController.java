package g41.si2022.coiipa.inscribir_multiples_usuarios_entidad;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import javax.swing.JLabel;
import javax.swing.table.TableModel;

import g41.si2022.dto.AlumnoDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.GrupoDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.util.Dialog;
import g41.si2022.util.Util;
import g41.si2022.util.exception.ApplicationException;

public class InscribirMultiplesUsuariosEntidadController extends g41.si2022.mvc.Controller<InscribirMultiplesUsuariosEntidadView, InscribirMultiplesUsuariosEntidadModel> {

	private static final String SIGN_IN = "sign-in";
	private static final String SIGN_UP = "sign-up";

	private List<CursoDTO> cursos;
	private String cursoId;

	public InscribirMultiplesUsuariosEntidadController(InscribirMultiplesUsuariosEntidadModel m, InscribirMultiplesUsuariosEntidadView v) {
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

	/**
	 * This method handles the inserting of the group, alumnos and inscripciones.
	 */
	public void manageMain() {
		if (cursoId == null) return;

		String email = "";
		GrupoDTO grupo;
		switch (this.getView().getRadioSignin().isSelected() ? SIGN_IN : SIGN_UP) {
			case SIGN_IN:
				email = getView().getTxtEmailLogin().getText();
				break;
			case SIGN_UP:
				email = getView().getTxtEmail().getText();
				getModel().insertGrupo(
					getView().getTxtNombre().getText(),
					email,
					getView().getTxtTelefono().getText()
				);
				break;
			default:
				throw new ApplicationException("Invalid radio button value");
		}

		grupo = getModel().getGrupoFromEmail(email).get(0);

		getModel().insertInscripciones(
			getView().getMain().getToday().toString(), cursoId,
			getModel().insertAlumnos(gatherAllAlumnos()),
			grupo.getId()
		);

		getListaCursos();
		Util.sendEmail(email, "COIIPA: Inscripción realizada", "Su inscripción al curso " + SwingUtil.getSelectedKey(this.getView().getTablaCursos()) + " ha sido realizada con éxito.");
		Dialog.show("Inscripción realizada con éxito");
	}

	/**
	 * This method will return the list of Alumnos that are listed in the JTable.<br>
	 * In the context of the HU InscribirMultiplesUsuarios, the Alumnos have to:<br>
	 * <ol>
	 * <li> Be inserted in the DB (table alumno) if they are not already there. Use email as PK.
	 * <li> Create an entry in the table inscripcion that relates this alumno with the curso if the alumno has not joined yet.
	 * </ol>
	 *
	 * @return List of alumnos that are listed in the table
	 */
	public List<AlumnoDTO> gatherAllAlumnos () {
		return this.getView().getTablaInscritos().getData().stream().collect(
				new g41.si2022.util.collector.HalfwayListCollector<Map<String, String>, AlumnoDTO>() {
					@Override
					public BiConsumer<List<AlumnoDTO>, Map<String, String>> accumulator() {
						return (list, row) -> {
							AlumnoDTO alumno = new AlumnoDTO();
							alumno.setNombre(row.get(InscribirMultiplesUsuariosEntidadController.this.getView().getTablaInscritos().getColumnNames()[0]));
							alumno.setApellidos(row.get(InscribirMultiplesUsuariosEntidadController.this.getView().getTablaInscritos().getColumnNames()[1]));
							alumno.setEmail(row.get(InscribirMultiplesUsuariosEntidadController.this.getView().getTablaInscritos().getColumnNames()[2]));
							alumno.setTelefono(row.get(InscribirMultiplesUsuariosEntidadController.this.getView().getTablaInscritos().getColumnNames()[3]));
							list.add(alumno);
						};
					}

				});
	}

	public void manageForm() {
		this.getView().getBtnInscribir().setEnabled(false);
		if (cursoId == null) return;
		String signinEmail = getView().getTxtEmailLogin().getText();
		String signupEmail = getView().getTxtEmail().getText();

		if(signinEmail.isEmpty() && signupEmail.isEmpty()) return;

		getView().getLblSignin().setForeground(Color.RED);
		getView().getLblSignup().setForeground(Color.RED);

		boolean validEmail = false;
		JLabel target = null;
		String errorMsg = "";
		switch(this.getView().getRadioSignin().isSelected() ? SIGN_IN : SIGN_UP) {
			case SIGN_IN:
				target = this.getView().getLblSignin();
				if (!Util.verifyEmailStructure(signinEmail)) { // Check basic structure
					target.setText("");
					break;
				}
				validEmail = this.getModel().verifyEmail(signinEmail); // Check if email exists in database
				errorMsg = "Email desconocido";
				break;
			case SIGN_UP:
				target = this.getView().getLblSignup();
				if (!Util.verifyEmailStructure(signupEmail)) {
					this.getView().getLblSignup().setText("");
					break;
				}
				validEmail = !this.getModel().verifyEmail(signupEmail); // Check if email doesn't already exist in db
				errorMsg = "El email ya está registrado";
				break;
			default:
				throw new ApplicationException("Invalid radio button value");
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
		cursos = getModel().getListaCursos(getView().getMain().getToday().toString());
		TableModel tableModel = SwingUtil.getTableModelFromPojos(cursos, new String[] { "nombre", "plazas_libres", "start_inscr", "end_inscr" },
			new String[] { "Nombre", "Plazas libres", "Fecha ini. inscr.", "Fecha fin inscr." }, null);
		getView().getTablaCursos().setModel(tableModel);
		SwingUtil.autoAdjustColumns(getView().getTablaCursos());
	}
}