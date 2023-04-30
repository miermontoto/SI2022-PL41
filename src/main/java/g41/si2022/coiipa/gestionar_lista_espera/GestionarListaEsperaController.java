package g41.si2022.coiipa.gestionar_lista_espera;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.ListaEsperaDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.util.Dialog;

public class GestionarListaEsperaController extends g41.si2022.mvc.Controller<GestionarListaEsperaView, GestionarListaEsperaModel> {

	private int row;
	int id;
	private JTable table;
	String cursoNombre;

	public GestionarListaEsperaController(GestionarListaEsperaModel modelo, GestionarListaEsperaView vista) {
		super(vista, modelo);
		this.table = this.getView().getTableInscripciones();
	}

	public void updateCombos() {
		JComboBox<String> comboCursos = this.getView().getCmbCurso();
		comboCursos.removeAllItems();
		List<CursoDTO> sesiones = this.getModel().getListaCursosConEspera(this.getView().getMain().getToday().toString());
		DefaultComboBoxModel<String> cmbCursoModel = this.getView().getCmbCursoModel();

		if (!sesiones.isEmpty()) for(CursoDTO evento : sesiones) {
			String curso = evento.toString();
			cmbCursoModel.addElement(curso);
			comboCursos.setModel(cmbCursoModel); // Actualizar el modelo de datos del JComboBox
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initNonVolatileData() {
		this.getView().getBtnEliminarListaEspera().addActionListener(e -> handleEliminar());
		this.getView().getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) {
				handleSelect();
			}
		});

		this.getView().getCmbCurso().addActionListener(e -> {
			JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
			String elementoSeleccionado = (String) comboBox.getSelectedItem();
			if (elementoSeleccionado != null) {
				cursoNombre = elementoSeleccionado;
				getListaEspera(getModel().getCursoId(elementoSeleccionado));
			}
		});
	}

	@Override
	public void initVolatileData() {
		clear();
		updateCombos();
	}

	public void clear() {
		getView().getNombreApellidosLabel().setText("Seleccionar alumno");
		getView().getFechaListaLabel().setText("SeleccionarAlumno");
	}

	private void handleEliminar() {
		getModel().eliminarInscripcion(table.getModel().getValueAt(row, 0).toString());
		Dialog.show("Alumno eliminado de la lista de espera con éxito");
		getListaEspera(getModel().getCursoId(cursoNombre));
		clear();
	}

	private void handleSelect() {
		TableModel model = table.getModel();
		row = table.convertRowIndexToModel(table.getSelectedRow());
		id = Integer.valueOf(model.getValueAt(row, 0).toString());
		String nombreAlumno = model.getValueAt(row, 1).toString();
		String apellidosAlumno = model.getValueAt(row, 2).toString();
		String fechaEntradaListaEspera = model.getValueAt(row, 3).toString();

		String nombreApellidosAlumno = nombreAlumno + " " + apellidosAlumno;
		this.getView().getNombreApellidosLabel().setText(nombreApellidosAlumno);
		this.getView().getFechaListaLabel().setText(fechaEntradaListaEspera);
	}

	private void getListaEspera(String cursoID) {
		List<ListaEsperaDTO> listaespera = this.getModel().getListaEspera(cursoID);

		table.setModel(SwingUtil.getTableModelFromPojos(listaespera,
				new String[] {"id", "nombre", "apellidos", "fecha_entrada"},
				new String[] {"", "Nombre alumno", "Apellidos", "Fecha entrada en lista de espera"},
				null));
		table.removeColumn(table.getColumnModel().getColumn(0));
		SwingUtil.autoAdjustColumns(table);
	}

}
