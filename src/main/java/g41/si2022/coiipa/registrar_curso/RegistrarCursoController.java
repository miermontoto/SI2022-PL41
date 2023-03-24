package g41.si2022.coiipa.registrar_curso;

import java.util.List;
import java.util.Map;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import java.awt.event.KeyAdapter;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.github.lgooddatepicker.optionalusertools.DateChangeListener;

import g41.si2022.dto.ProfesorDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.components.BetterDatePicker;
import g41.si2022.util.Dialog;
import g41.si2022.util.exception.UnexpectedException;

public class RegistrarCursoController extends g41.si2022.mvc.Controller<RegistrarCursoView, RegistrarCursoModel> {

	private Map<String, ProfesorDTO> profesoresMap;
	private final java.util.function.Supplier <List<ProfesorDTO>> sup = () -> {
		java.util.ArrayList<ProfesorDTO> out = new java.util.ArrayList<ProfesorDTO> ();
		out.addAll(RegistrarCursoController.this.profesoresMap.values());
		return out;
	};

	public RegistrarCursoController(RegistrarCursoView v, RegistrarCursoModel m) {
		super(v, m);
		this.profesoresMap = new HashMap<String, ProfesorDTO> ();
	}

	@Override
	public void initNonVolatileData() {
		// Load the insert curso listener
		getView().getBtnRegistrar().addActionListener((e) -> SwingUtil.exceptionWrapper(() -> insertCurso()));
		loadDateListeners();
		loadTextAreaListeners();
		loadCheckListeners();
		loadValidateListeners();
		getView().getBtnRegistrar().setEnabled(false);
		JTextField coste = getView().getTxtCoste();
		coste.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar()) && e.getKeyChar() != '.') {
					JTextField tf = RegistrarCursoController.this.getView().getTxtCoste();
					if (tf.getText().length() > 1) {
						tf.setText(tf.getText().substring(0, tf.getText().length() - 1));
					} else tf.setText("");
				}
			}

		});

		coste.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) { }

			@Override
			public void focusLost(FocusEvent e) {
				JTextField tf = RegistrarCursoController.this.getView().getTxtCoste();
				if (!Character.isDigit(tf.getText().charAt(0)) && tf.getText().charAt(0) != '.') {
					tf.setText("");
				}
			}
		});

	}

	private void loadValidateListeners() {
		for(JComponent c : getView().getFocusableComponents()) {
			if(c instanceof BetterDatePicker) {
				((BetterDatePicker) c).addDateChangeListener((e) -> checkValidity());
			} else if (c instanceof JTextField) {
				((JTextField) c).addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) { checkValidity(); }
				});
			}
		}
	}

	@Override
	public void initVolatileData() {
		SwingUtil.exceptionWrapper(() -> getListaProfesores()); // Load the profesores list
	}

	private void loadCheckListeners() {
		for(JComponent jc : getView().getFocusableComponents()) {
			if (jc instanceof JTextField) {
				JTextField tf = (JTextField) jc;
				tf.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(KeyEvent e) {
						checkValidity();
					}

				});
			} else if (jc instanceof BetterDatePicker) {
				BetterDatePicker dp = (BetterDatePicker) jc;
				dp.addDateChangeListener((e) -> checkValidity());
			}
		}
	}

	private void checkValidity() {
		boolean valid = true;
		for(JComponent jc : getView().getFocusableComponents()) {
			if (jc instanceof JTextField) {
				JTextField tf = (JTextField) jc;
				if (tf.getText().isEmpty()) {
					valid = false;
					break;
				}
			} else if (jc instanceof BetterDatePicker) {
				BetterDatePicker dp = (BetterDatePicker) jc;
				if (dp.getDate() == null) {
					valid = false;
					break;
				}
			}
		}
		getView().getBtnRegistrar().setEnabled(valid);
	}

	private void loadTextAreaListeners() {
		KeyAdapter ka = new KeyAdapter () {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB) {
					e.consume();
					KeyboardFocusManager fm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
					fm.focusNextComponent();
				}
			}
		};

		this.getView().getTxtDescripcion().addKeyListener(ka);
	}

	private void loadTableListeners() {
		this.getView().getTableProfesores().getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				JTable tabla = RegistrarCursoController.this.getView().getTableProfesores();
				RegistrarCursoController.this.profesoresMap
				.get(tabla.getValueAt(e.getFirstRow(), 0).toString())
				.setRemuneracion(tabla.getValueAt(e.getFirstRow(), e.getColumn()).toString());
			}

		});
	}

	private void loadDateListeners() {
		BetterDatePicker
			inscripcionIni = getView().getDateInscrStart(),
			inscripcionFin = getView().getDateInscrEnd(),
			cursoIni = getView().getDateCursoStart(),
			cursoFin = getView().getDateCursoEnd();

		DateChangeListener inscripcionListener = (e) -> {
			/*if(inscripcionFin.getDate() == null && inscripcionIni.getDate() != null) {
				inscripcionFin.setDate(inscripcionIni.getDate().plusDays(1));
				return;
			}*/

			if(inscripcionIni.getDate() != null && inscripcionIni.compareTo(getView().getMain().getTodayPicker()) <= 0
				&& e.getSource() == inscripcionIni) {
				Dialog.showWarning("La fecha de inicio de inscripción es anterior a la fecha actual.");
			} // Comprobación fecha de inicio de inscripción

			if(inscripcionFin.getDate() == null) return;

			if(inscripcionFin.compareTo(getView().getMain().getTodayPicker()) <= 0
				&& e.getSource() == inscripcionFin) {
				Dialog.showWarning("El periodo de inscripción es anterior a la fecha actual y ha finalizado.");
			} // Comprobación fecha de fin de inscripción

			int diff = inscripcionFin.compareTo(inscripcionIni);
			if (diff < 0) { // Comprobación de rango válido de fechas
				inscripcionFin.setDate(null);
				Dialog.showError("La fecha de fin de inscripción no puede ser anterior a la de inicio.");
			} else if (diff == 0)  Dialog.showWarning("La fecha de fin de inscripción es igual a la de inicio.");

			// Comprobación de overlap de fechas (curso e inscripción)
			if(cursoIni.getDate() != null && cursoIni.compareTo(inscripcionFin) <= 0)
				Dialog.showWarning("Las fechas de curso y de inscripción se solapan.");
			};

		DateChangeListener cursoListener = (e) -> {
			/*if(cursoFin.getDate() == null && cursoIni.getDate() != null) {
				cursoFin.setDate(cursoIni.getDate().plusDays(1));
				return;
			}*/

			if(cursoFin.getDate() == null) return;

			int diff = cursoFin.compareTo(cursoIni);
			if (diff < 0) {
				cursoFin.setDate(null);
				Dialog.showError("La fecha de fin de curso no puede ser anterior a la de inicio.");
			} else if (diff == 0)  Dialog.showWarning("La fecha de fin de curso es igual a la de inicio.");

			if(inscripcionFin.getDate() != null)
				if (cursoIni.compareTo(inscripcionFin) <= 0)
					Dialog.showWarning("Las fechas de curso y de inscripción se solapan.");
		};

		cursoIni.addDateChangeListener(cursoListener);
		cursoFin.addDateChangeListener(cursoListener);
		inscripcionIni.addDateChangeListener(inscripcionListener);
		inscripcionFin.addDateChangeListener(inscripcionListener);
	}

	public void getListaProfesores() {
		this.getModel().getListaProfesores().stream().forEach((x) -> profesoresMap.put(x.getDni(), x));
		this.getView().getTableProfesores().setModel(
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
		SwingUtil.autoAdjustColumns(this.getView().getTableProfesores());
		loadTableListeners();
	}

	public void insertCurso() {
		List<ProfesorDTO> docentes = this.getDocentes();

		if (docentes.size() == 0) throw new UnexpectedException("No se ha seleccionado a ningún docente.");

		String idCurso = this.getModel().insertCurso(
				getView().getTxtNombre().getText(),
				getView().getTxtDescripcion().getText(),
				getView().getTxtCoste().getText(),
				getView().getDateInscrStart().getDate().toString(),
				getView().getDateInscrEnd().getDate().toString(),
				getView().getDateCursoStart().getDate().toString(),
				getView().getDateCursoEnd().getDate().toString(),
				getView().getTxtPlazas().getText());

		docentes.forEach((x) -> getModel().insertDocencia(x.getRemuneracion(), x.getId(), idCurso));

		Dialog.show("Curso registrado con éxito.");
	}

	private List<ProfesorDTO> getDocentes() {
		List<ProfesorDTO> docentes = new ArrayList<>();
		TableModel model = this.getView().getTableProfesores().getModel();
		for (int i = 0 ; i < model.getRowCount() ; i++) {
			if (model.getValueAt(i, model.getColumnCount()-1) != null) {
				docentes.add(this.profesoresMap.get(model.getValueAt(i, 0)));
			}
		}
		return docentes;
	}
}
