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
import g41.si2022.util.Util;

public class GestionarListaEsperaController extends g41.si2022.mvc.Controller<GestionarListaEsperaView, GestionarListaEsperaModel> {

	private int row;
	private JTable table;
	private String cursoNombre;

	public GestionarListaEsperaController(GestionarListaEsperaModel modelo, GestionarListaEsperaView vista) {
		super(vista, modelo);
		this.table = this.getView().getTableInscripciones();
	}

	public void updateCombos() {
		JComboBox<String> comboCursos = this.getView().getCmbCurso();
		comboCursos.removeAllItems();
		List<CursoDTO> sesiones = this.getModel().getListaCursosConEspera(this.getToday().toString());
		DefaultComboBoxModel<String> cmbCursoModel = this.getView().getCmbCursoModel();

		if (!sesiones.isEmpty()) for(CursoDTO sesion : sesiones) {
			String curso = sesion.toString();
			cmbCursoModel.addElement(curso);
			comboCursos.setModel(cmbCursoModel);
			setJComboIfStudents(true);
		} else setJComboIfStudents(false);
	}

	public void setJComboIfStudents(boolean status) {
		if(status) {
			this.getView().getError().setVisible(false);
			this.getView().getCmbCurso().setVisible(true);
		} else {
			this.getView().getError().setVisible(true);
			this.getView().getCmbCurso().setVisible(false);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initNonVolatileData() {
		this.getView().getBtnEliminarListaEspera().addActionListener(e -> handleEliminar());
		this.getView().getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});

		this.getView().getCmbCurso().addActionListener(e -> {
			JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
			String elementoSeleccionado = (String) comboBox.getSelectedItem();
			cursoNombre = elementoSeleccionado;
			if(elementoSeleccionado == null) return;
			getListaEspera(getModel().getCursoId(elementoSeleccionado));
			clear();
		});
	}

	@Override
	public void initVolatileData() {
		clear();
		updateCombos();
	}

	public void clear() {
		getView().getBtnEliminarListaEspera().setEnabled(false);
		this.getView().getNombreApellidosLabel().setText("Seleccionar alumno");
		this.getView().getFechaListaLabel().setText("Seleccionar alumno");
	}

	public void enable() {
		getView().getBtnEliminarListaEspera().setEnabled(true);
	}

	private void handleEliminar() {
		String nombreApellidosAlumno = table.getModel().getValueAt(row, 1).toString() + " " + table.getModel().getValueAt(row, 2).toString();
		getModel().eliminarInscripcion(table.getModel().getValueAt(row, 0).toString());
		Dialog.show("El alumno " + nombreApellidosAlumno + " ha sido eliminado de la lista de espera del curso " + cursoNombre + " con éxito");
		Util.sendEmail(getModel().getEmailAlumno(table.getModel().getValueAt(row, 0).toString()), "COIIPA: Lista de espera", "Estimado/a alumno:\n\n"
			+ "Le informamos que ha sido eliminado de la lista de espera del curso " + cursoNombre + ".\n\n"
			+ "Reciba un cordial saludo,\n"
			+ "Equipo de gestión del COIIPA");
		this.getListaEspera(getModel().getCursoId(cursoNombre));
		updateCombos();
		this.clear();
	}

	private void handleSelect() {
		TableModel model = table.getModel();
		row = table.convertRowIndexToModel(table.getSelectedRow());
		String nombreAlumno = model.getValueAt(row, 1).toString();
		String apellidosAlumno = model.getValueAt(row, 2).toString();
		String fechaEntradaListaEspera = model.getValueAt(row, 3).toString();

		String nombreApellidosAlumno = nombreAlumno + " " + apellidosAlumno;
		this.getView().getNombreApellidosLabel().setText(nombreApellidosAlumno);
		this.getView().getFechaListaLabel().setText(fechaEntradaListaEspera);
		this.enable();
	}

	private void getListaEspera(String cursoID) {
		List<ListaEsperaDTO> listaespera = this.getModel().getListaEspera(cursoID);

		table.setModel(SwingUtil.getTableModelFromPojos(listaespera,
			new String[] {"id", "nombre", "apellidos", "fecha_entrada", "coste"},
			new String[] {"", "Nombre alumno", "Apellidos", "Fecha entrada en lista de espera", "Coste de la inscripción"},
			null
		));
		table.removeColumn(table.getColumnModel().getColumn(0));
		SwingUtil.autoAdjustColumns(table);
	}

}
