package g41.si2022.coiipa.registrar_curso;

import java.util.List;
import java.util.Map;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Optional;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import g41.si2022.coiipa.dto.ProfesorDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.util.BetterDatePicker;

public class RegistrarCursoController extends g41.si2022.ui.Controller<RegistrarCursoView, RegistrarCursoModel> {

	private Map<String, ProfesorDTO> profesoresMap;
	private final java.util.function.Supplier <List<ProfesorDTO>> sup = () -> {
		java.util.ArrayList<ProfesorDTO> out = new java.util.ArrayList<ProfesorDTO> ();
		out.addAll(RegistrarCursoController.this.profesoresMap.values());
		return out;
	};

	public RegistrarCursoController (RegistrarCursoView v, RegistrarCursoModel m) {
		super(v, m);
		this.profesoresMap = new HashMap<String, ProfesorDTO> ();
	}

	@Override
	protected void initNonVolatileData () {
		// Load the insert curso listener
		this.getView().getSubmitButton().addActionListener((e) -> SwingUtil.exceptionWrapper(() -> insertCurso()));
		this.loadDateListeners();
		this.loadTableListeners();
		this.loadTextAreaListeners();
		this.getView().getCosteTextField().addKeyListener(new java.awt.event.KeyAdapter() {

			@Override
			public void keyPressed (KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar()) && e.getKeyChar() != '.') {
					javax.swing.JTextField tf = RegistrarCursoController.this.getView().getCosteTextField();
					if (tf.getText().length() > 1) {
						tf.setText(tf.getText().substring(0, tf.getText().length()-1));
					} else {
						tf.setText("");
					}
				}
			}

		});
		this.getView().getCosteTextField().addFocusListener(new java.awt.event.FocusListener() {

			@Override
			public void focusGained(FocusEvent e) { }

			@Override
			public void focusLost(FocusEvent e) {
				javax.swing.JTextField tf = RegistrarCursoController.this.getView().getCosteTextField();
				if (!Character.isDigit(tf.getText().charAt(0)) && tf.getText().charAt(0) != '.') {
					tf.setText("");
				}
			}

		});

	}
	
	@Override
	protected void initVolatileData () {
		SwingUtil.exceptionWrapper(() -> getListaProfesores()); // Load the profesores list
	}

	private void loadTextAreaListeners () {
		java.awt.event.KeyAdapter ka = new java.awt.event.KeyAdapter () {
			@Override
			public void keyPressed (KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB) {
					e.consume();
					KeyboardFocusManager fm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
					fm.focusNextComponent();
				}
			}
		};
		this.getView().getObjetivosDescripcionTextArea().addKeyListener(ka);
		this.getView().getLocalizacionTextArea().addKeyListener(ka);
	}

	private void loadTableListeners () {
		this.getView().getTablaProfesores().getModel().addTableModelListener(new TableModelListener () {

			@Override
			public void tableChanged(TableModelEvent e) {
				javax.swing.JTable tabla = RegistrarCursoController.this.getView().getTablaProfesores();
				RegistrarCursoController.this.profesoresMap
				.get(tabla.getValueAt(e.getFirstRow(), 0).toString())
				.setRemuneracion(tabla.getValueAt(e.getFirstRow(), e.getColumn()).toString());
			}

		});
	}

	private void loadDateListeners () {
		BetterDatePicker
		inscripcionIni = this.getView().getInscripcionIni(),
		inscripcionFin = this.getView().getInscripcionFin(),
		cursoIni = this.getView().getCursoIni(),
		cursoFin = this.getView().getCursoFin();
		// Load the Inscripcion Ini DatePicker listener (set Fin to next day)
		inscripcionIni.addDateChangeListener((e) -> {
			// Query on #20879 -> Resolved to: Allow but issue a warning.
			if (inscripcionIni.compareTo(this.getView().getMain().getTodayPicker()) < 0) {
				SwingUtil.raiseWarning("Se está seleccionando una fecha anterior al día de hoy.");
			}
			if (inscripcionFin.getDate() == null || inscripcionFin.compareTo(inscripcionIni) <= 0) {
				inscripcionFin.setDate(e.getNewDate().plusDays(1));
			}
		});
		// Load the Curso Ini DatePicker listener (set Fin to next day)
		cursoIni.addDateChangeListener((e) -> {
			if (cursoFin.getDate() == null || cursoFin.compareTo(cursoIni) <= 0) {
				cursoFin.setDate(e.getNewDate().plusDays(1));
			}
			if (inscripcionFin.getDate() == null) {
				inscripcionFin.setDate(e.getNewDate().minusDays(1));
			}
		});
		// Load the Inscripcion Fin DatePicker listener (set Fin to next day if lower than Ini)
		inscripcionFin.addDateChangeListener((e) -> {
			if (cursoIni.getDate() == null) {
				cursoIni.setDate(e.getNewDate().plusDays(1));
			}
			if (inscripcionIni.getDate() == null || inscripcionIni.compareTo(inscripcionFin) >= 0) {
				inscripcionIni.setDate(e.getNewDate().minusDays(1));
			}
		});
		// Load the Curso Fin DatePicker listener (set Fin to next day if lower than Ini)
		cursoFin.addDateChangeListener((e) -> {
			if (cursoIni.getDate() == null || cursoIni.compareTo(cursoFin) >= 0) {
				cursoIni.setDate(e.getNewDate().minusDays(1));
			}
		});
	}

	public void getListaProfesores() {
		this.getModel().getListaProfesores().stream().forEach((x) -> profesoresMap.put(x.getDni(), x));
		this.getView().getTablaProfesores().setModel(
				SwingUtil.getTableModelFromPojos(
						this.sup.get(),
						new String[] { "dni", "nombre", "apellidos", "email", "direccion", "remuneracion" },
						new String[] { "DNI", "Nombre", "Apellidos", "email", "Dirección", "Remuneración" },
						new HashMap<Integer, java.util.regex.Pattern> () {
							private static final long serialVersionUID = 1L;
							{ put(5, java.util.regex.Pattern.compile("\\d+(\\.\\d+)?")); }
						}
						)
				);
		SwingUtil.autoAdjustColumns(this.getView().getTablaProfesores());
	}

	public void insertCurso () {
		Optional<ProfesorDTO> profesorElegido = this.getProfesor();
		if (!profesorElegido.isPresent()) {
			throw new g41.si2022.util.UnexpectedException("No se ha seleccionado a ningún docente.");
		}
		this.getModel().insertCurso(
				this.getView().getNombreCurso(), this.getView().getObjetivosDescripcion(),
				this.getView().getCoste(),
				this.getView().getInscripcionIniDate(), this.getView().getInscripcionFinDate(),
				this.getView().getCursoIniDate(), this.getView().getCursoFinDate(),
				this.getView().getPlazas(), this.getView().getLocalizacion(),
				profesorElegido.get().getId(), // El curso solo tiene un profesor
				profesorElegido.get().getRemuneracion());
		SwingUtil.showMessage("Curso registrado con éxito.", "Registro de cursos");
	}

	private Optional<ProfesorDTO> getProfesor () {
		javax.swing.table.TableModel model = this.getView().getTablaProfesores().getModel();
		for (int i = 0 ; i < model.getRowCount() ; i++) {
			if (model.getValueAt(i, model.getColumnCount()-1) != null) {
				return Optional.of(this.profesoresMap.get(model.getValueAt(i, 0).toString()));
			}
		}
		return Optional.empty();
	}
}
