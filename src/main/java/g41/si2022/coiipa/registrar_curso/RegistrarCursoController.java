package g41.si2022.coiipa.registrar_curso;

import java.util.List;
import java.util.Map;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Optional;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import g41.si2022.coiipa.dto.ProfesorDTO;
import g41.si2022.util.BetterDatePicker;
import g41.si2022.util.SwingUtil;

public class RegistrarCursoController {
	private RegistrarCursoModel model;
	private RegistrarCursoView view;

	private Map<String, ProfesorDTO> profesoresMap;
	private final java.util.function.Supplier <List<ProfesorDTO>> sup = () -> {
		java.util.ArrayList<ProfesorDTO> out = new java.util.ArrayList<ProfesorDTO> ();
		out.addAll(RegistrarCursoController.this.profesoresMap.values());
		return out;
	};

	public RegistrarCursoController (RegistrarCursoModel m, RegistrarCursoView v) {
		this.model = m;
		this.view = v;
		profesoresMap = new HashMap<String, ProfesorDTO> ();
		this.initView();
	}

	public void initView () {
		SwingUtil.exceptionWrapper(() -> getListaProfesores()); // Load the profesores list
		// Load the insert curso listener
		view.getSubmitButton().addActionListener((e) -> SwingUtil.exceptionWrapper(() -> insertCurso()));
		loadDateListeners();
		loadTableListeners();
		loadTextAreaListeners();
		this.view.getCosteTextField().addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed (KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar()) && e.getKeyChar() != '.') {
					javax.swing.JTextField tf = RegistrarCursoController.this.view.getCosteTextField();
					if (tf.getText().length() > 1) {
						tf.setText(tf.getText().substring(0, tf.getText().length()-1));
					} else {
						tf.setText("");
					}
				}
			}
		});
		this.view.getCosteTextField().addFocusListener(new java.awt.event.FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				javax.swing.JTextField tf = RegistrarCursoController.this.view.getCosteTextField();
				if (!Character.isDigit(tf.getText().charAt(0)) && tf.getText().charAt(0) != '.') {
					tf.setText("");
				}
			}
			
		});
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
		this.view.getObjetivosDescripcionTextArea().addKeyListener(ka);
		this.view.getLocalizacionTextArea().addKeyListener(ka);
	}

	private void loadTableListeners () {
		view.getTablaProfesores().getModel().addTableModelListener(new TableModelListener () {

			private final javax.swing.JTable tabla = RegistrarCursoController.this.view.getTablaProfesores();

			@Override
			public void tableChanged(TableModelEvent e) {
				RegistrarCursoController.this.profesoresMap
				.get(this.tabla.getValueAt(e.getFirstRow(), 0).toString())
				.setRemuneracion(this.tabla.getValueAt(e.getFirstRow(), e.getColumn()).toString());
			}

		});
		view.getTablaProfesores().getModel().addTableModelListener(new TableModelListener () {

			private final javax.swing.JTable tabla = RegistrarCursoController.this.view.getTablaProfesores();

			@Override
			public void tableChanged(TableModelEvent e) {
				RegistrarCursoController.this.profesoresMap
				.get(this.tabla.getValueAt(e.getFirstRow(), 0).toString())
				.setRemuneracion(this.tabla.getValueAt(e.getFirstRow(), e.getColumn()).toString());
			}

		});
	}

	private void loadDateListeners () {
		BetterDatePicker
		inscripcionIni = view.getInscripcionIni(),
		inscripcionFin = view.getInscripcionFin(),
		cursoIni = view.getCursoIni(),
		cursoFin = view.getCursoFin();
		// Load the Inscripcion Ini DatePicker listener (set Fin to next day)
		inscripcionIni.addDateChangeListener((e) -> {
			// Query on #20879
			if (inscripcionIni.compareTo(this.view.getMain().getTodayPicker()) < 0) {
				inscripcionIni.setDate(this.view.getMain().getToday());
			}
			if (inscripcionFin.getDate() == null) {
				inscripcionFin.setDate(e.getNewDate().plusDays(1));
			}
		});
		// Load the Curso Ini DatePicker listener (set Fin to next day)
		cursoIni.addDateChangeListener((e) -> {
			if (cursoFin.getDate() == null) {
				cursoFin.setDate(e.getNewDate().plusDays(1));
			}
		});
		// Load the Inscripcion Fin DatePicker listener (set Fin to next day if lower than Ini)
		inscripcionFin.addDateChangeListener((e) -> {
			if (inscripcionIni.getDate() != null && inscripcionIni.compareTo(inscripcionFin) >= 0) {
				inscripcionFin.setDate(inscripcionIni.getDate().plusDays(1));
			}
			if (cursoFin.getDate() != null && inscripcionFin.compareTo(cursoFin) >= 0) {
				cursoFin.setDate(inscripcionFin.getDate());
			}
			if (cursoIni.getDate() == null) {
				cursoIni.setDate(inscripcionFin.getDate().plusDays(1));
			}
		});
		// Load the Curso Fin DatePicker listener (set Fin to next day if lower than Ini)
		cursoFin.addDateChangeListener((e) -> {
			if (cursoIni.getDate() != null && cursoIni.compareTo(cursoFin) >= 0) {
				cursoFin.setDate(cursoIni.getDate().plusDays(1));
			}
			if (inscripcionFin.getDate() != null && inscripcionFin.compareTo(cursoFin) >= 0) {
				inscripcionFin.setDate(cursoFin.getDate());
			}
		});
	}

	public void getListaProfesores() {
		model.getListaProfesores().stream().forEach((x) -> profesoresMap.put(x.getDni(), x));
		view.getTablaProfesores().setModel(
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
		SwingUtil.autoAdjustColumns(view.getTablaProfesores());
	}

	public void insertCurso () {
		Optional<ProfesorDTO> profesorElegido = this.getProfesor();
		if (!profesorElegido.isPresent()) {
			throw new g41.si2022.util.UnexpectedException("No se ha seleccionado a ningún docente.");
		}
		this.model.insertCurso(
				view.getNombreCurso(), view.getObjetivosDescripcion(),
				view.getCoste(), 
				view.getInscripcionIniDate(), view.getInscripcionFinDate(), view.getCursoIniDate(), view.getCursoFinDate(),
				view.getPlazas(), view.getLocalizacion(),
				profesorElegido.get().getId(), // El curso solo tiene un profesor
				profesorElegido.get().getRemuneracion());
		SwingUtil.showMessage("Curso registrado con éxito.", "Registro de cursos");
	}

	private Optional<ProfesorDTO> getProfesor () {
		javax.swing.table.TableModel model = view.getTablaProfesores().getModel();
		for (int i = 0 ; i < model.getRowCount() ; i++) {
			if (model.getValueAt(i, model.getColumnCount()-1) != null) {
				return Optional.of(this.profesoresMap.get(model.getValueAt(i, 0).toString()));
			}
		}
		return Optional.empty();
	}
}
