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
			setJComboIfStudents(true);
		}
		else
		{
			setJComboIfStudents(false);
		}
	}

	public void setJComboIfStudents(boolean status) {

		if(status) {
			this.getView().getError().setVisible(false);
			this.getView().getCmbCurso().setVisible(true);
		}
		else {
			this.getView().getError().setVisible(true);
			this.getView().getCmbCurso().setVisible(false);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initNonVolatileData() {
		//Handler del botón de eliminar
		this.getView().getBtnEliminarListaEspera().addActionListener(e -> handleEliminar());
		//Handler de la tabla de inscripciones en lista de espera
		this.getView().getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) {
				handleSelect();
			}
		});

		//Handler del JComboBox, al seleccionar algo, nos va a rellenar
		this.getView().getCmbCurso().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
				String elementoSeleccionado = (String) comboBox.getSelectedItem(); //Obtenemos lo que hemos seleccionado en el comboBox
				if(elementoSeleccionado != null) {
					cursoNombre = elementoSeleccionado; //Obtenemos en la variable global el nombre del curso seleccionado.
					getListaEspera(getModel().getCursoId(elementoSeleccionado)); //Obtenemos la lista de espera y cargamos la tabla.
					clear(); //Apagamos los controles
				}
			}
		});
	}

	@Override
	public void initVolatileData() {
		clear();
		updateCombos();
	}

	public void clear() { //Función dedicada a poner a valores nulos los controles, o habilitarlos
		getView().getBtnEliminarListaEspera().setEnabled(false); //Apagamos el botón de la lista de espera.
		this.getView().getNombreApellidosLabel().setText("Seleccionar alumno"); //Quitamos los nombres y apellidos
		this.getView().getFechaListaLabel().setText("Seleccionar alumno"); //Quitamos la fecha de inscripción
	}

	public void enable() {
		getView().getBtnEliminarListaEspera().setEnabled(true); //Encendemos el botón de la lista de espera.
	}

	private void handleEliminar() {
		String id = table.getModel().getValueAt(row, 0).toString();
		String nombreApellidosAlumno = table.getModel().getValueAt(row, 1).toString() + " " + table.getModel().getValueAt(row, 2).toString();
		getModel().eliminarInscripcion(id);
		Dialog.show("El alumno " + nombreApellidosAlumno + " ha sido eliminado de la lista de espera del curso " + cursoNombre + " con éxito");
		this.getListaEspera(getModel().getCursoId(cursoNombre));
		updateCombos(); //Actualizamos el JComboBox
		this.clear();
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
		this.enable();
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
