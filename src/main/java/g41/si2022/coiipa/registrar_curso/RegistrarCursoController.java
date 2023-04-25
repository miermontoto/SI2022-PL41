package g41.si2022.coiipa.registrar_curso;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.awt.KeyboardFocusManager;
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
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.optionalusertools.DateChangeListener;

import g41.si2022.dto.SesionDTO;
import g41.si2022.dto.ProfesorDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.components.BetterDatePicker;
import g41.si2022.ui.util.Dialog;
import g41.si2022.ui.util.EventDialog;
import g41.si2022.util.exception.UnexpectedException;

public class RegistrarCursoController extends g41.si2022.mvc.Controller<RegistrarCursoView, RegistrarCursoModel> {

	private Map<String, ProfesorDTO> profesoresMap;
	private LinkedList<SesionDTO> sesiones;


	private final java.util.function.Supplier <List<ProfesorDTO>> sup = () -> {
		java.util.ArrayList<ProfesorDTO> out = new java.util.ArrayList<ProfesorDTO> ();
		out.addAll(RegistrarCursoController.this.profesoresMap.values());
		return out;
	};

	public RegistrarCursoController(RegistrarCursoView v, RegistrarCursoModel m) {
		super(v, m);
		profesoresMap = new HashMap<>();
		sesiones = new LinkedList<>();
	}

	@Override
	public void initVolatileData() {
		SwingUtil.exceptionWrapper(() -> getListaProfesores()); // Load the profesores list
	}

	@Override
	public void initNonVolatileData() {
		// Load the insert curso listener
		getView().getBtnRegistrar().addActionListener((e) -> SwingUtil.exceptionWrapper(() -> insertCurso()));
		loadDateListeners();
		loadTextAreaListeners();
		loadValidateListeners();
		loadEventListeners();
		loadColectivos();
		getView().getBtnRegistrar().setEnabled(false);
	}

	private void loadColectivos() {
		this.getView().getTablaCostes().setModel(
		SwingUtil.getTableModelFromPojos(
				this.getModel().getColectivos(),
				new String[]{"nombre", "coste"},
				new String[]{"Colectivo", "Coste"},
				null));
	}

	private void loadEventListeners() {
		javax.swing.JButton
		btnAdd = getView().getBtnAddSesion(),
		btnRemove = getView().getBtnRemoveSesion();
		BetterDatePicker
		start = getView().getDateCursoStart(),
		end = getView().getDateCursoEnd();

		JTable table = getView().getTableSesiones();

		btnAdd.addActionListener( (e) -> { // Add new event listener
			EventDialog ed;
			if(sesiones.isEmpty()) ed = new EventDialog(start.getDate(), end.getDate());
			else ed = new EventDialog(start.getDate(), end.getDate(), sesiones.getLast());

			if(ed.showDialog()) {
				sesiones.addAll(ed.getSesiones());
				table.setModel(SwingUtil.getTableModelFromPojos(sesiones,
						new String[] {"loc", "fecha", "horaIni", "horaFin"},
						new String[] {"Localizacion", "Fecha", "Hora de inicio", "Hora de fin"},
						null));
			}
		});

		btnRemove.addActionListener( (e) -> {
			int[] rows = table.getSelectedRows();
			for(int i = 0; i < rows.length; i++) {
				sesiones.remove(rows[i]);
				((DefaultTableModel) table.getModel()).removeRow(rows[i]);
				for(int j = i; j < rows.length; j++) if(rows[j] > rows[i]) rows[j]--;
			}
			btnRemove.setEnabled(false);
		});

		for(BetterDatePicker dp : new BetterDatePicker[] {start, end}) { // Check for date validity before allowing to add events
			dp.addDateChangeListener((e) -> {
				btnAdd.setEnabled(start.getDate() != null && end.getDate() != null);
			});
		}

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				btnRemove.setEnabled(table.getSelectedRows().length > 0);
			}
		});

		btnAdd.setEnabled(false);
		btnRemove.setEnabled(false);
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
			} else if (c instanceof JTable) {
				((JTable) c).addMouseListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mouseClicked(java.awt.event.MouseEvent e) { checkValidity(); }
				});

				( (JTable) c).getModel().addTableModelListener((e) -> checkValidity());
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
		// valid &= this.getView().getTablaCostes().getValueAt(0, 0) != this.getView().getTablaCostes().getColumnNames()[0];
		// valid &= this.getView().getTablaCostes().getValueAt(0, 1) != this.getView().getTablaCostes().getColumnNames()[1];
		valid &= !sesiones.isEmpty();
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
			if (cursoIni.getDate() != null) {
				if (cursoIni.compareTo(getView().getMain().getTodayPicker()) <= 0) {
					Dialog.showError("La fecha de inicio de curso es anterior a la fecha actual.");
					cursoIni.setDate(null);
				} // Comparación entre fecha actual y fecha de inicio de curso

				if (inscripcionFin.getDate() != null && cursoIni.compareTo(inscripcionFin) <= 0) {
					Dialog.showWarning("Las fechas de curso y de inscripción se solapan.");
				} // Comparación entre solapamiento de fechas de curso e inscripción
			}

			if (cursoFin.getDate() == null) return;

			int diff = cursoFin.compareTo(cursoIni);
			if (diff < 0) { // Comprobación de rango válido de fechas
				cursoFin.setDate(null);
				Dialog.showError("La fecha de fin de curso no puede ser anterior a la de inicio.");
			} else if (diff == 0)  Dialog.showWarning("La fecha de fin de curso es igual a la de inicio.");
		};

		cursoIni.addDateChangeListener(cursoListener);
		cursoFin.addDateChangeListener(cursoListener);
		inscripcionIni.addDateChangeListener(inscripcionListener);
		inscripcionFin.addDateChangeListener(inscripcionListener);
	}

	public void getListaProfesores() {
		getModel().getListaProfesores().stream().forEach((x) -> profesoresMap.put(x.getDni(), x));
		getView().getTableProfesores().setModel(
				SwingUtil.getTableModelFromPojos(
						this.sup.get(),
						new String[] { "dni", "nombre", "apellidos", "email", "direccion", "remuneracion" },
						new String[] { "DNI", "Nombre", "Apellidos", "Email", "Dirección", "Remuneración" },
						new HashMap<Integer, Pattern> () {
							private static final long serialVersionUID = 1L;
							{ put(5, Pattern.compile("\\d+(\\.\\d+)?")); }
						}
						)
				);
		SwingUtil.autoAdjustColumns(this.getView().getTableProfesores());
		loadTableListeners();
	}

	public void insertCurso() {
		List<ProfesorDTO> docentes = this.getDocentes();
		if (docentes.size() == 0) throw new UnexpectedException("No se ha seleccionado remuneración para ningún docente.");

		String idCurso = this.getModel().insertCurso(
				getView().getTxtNombre().getText(),
				getView().getTxtDescripcion().getText(),
				getView().getDateInscrStart().getDate().toString(),
				getView().getDateInscrEnd().getDate().toString(),
				getView().getDateCursoStart().getDate().toString(),
				getView().getDateCursoEnd().getDate().toString(),
				getView().getTxtPlazas().getText(),
				g41.si2022.util.Util.getData(getView().getTablaCostes()).stream().collect(
						new java.util.stream.Collector<Map<String, String>, Map<String, Double>, Map<String, Double>> () {

							@Override
							public Supplier<Map<String, Double>> supplier() {
								return java.util.TreeMap::new;
							}

							@Override
							public BiConsumer<Map<String, Double>, Map<String, String>> accumulator() {
								return (map, row) -> map.put(
										row.get(RegistrarCursoController.this.getView().getTablaCostes().getColumnName(0)),
										Double.parseDouble(row.get(RegistrarCursoController.this.getView().getTablaCostes().getColumnName(1)))
										);
							}

							@Override
							public BinaryOperator<Map<String, Double>> combiner() {
								return (mapA, mapB) -> { mapA.putAll(mapB); return mapA; };
							}

							@Override
							public Function<Map<String, Double>, Map<String, Double>> finisher() {
								return java.util.Collections::unmodifiableMap;
							}

							@Override
							public Set<Characteristics> characteristics() {
								return new java.util.HashSet<>(java.util.Arrays.asList(Characteristics.CONCURRENT));
							}

						}
						));

		docentes.forEach((x) -> getModel().insertDocencia(x.getRemuneracion(), x.getId(), idCurso));
		sesiones.forEach((e) -> getModel().insertSesion(e.getLoc(), e.getFecha(), e.getHoraIni(), e.getHoraFin(), idCurso));

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
