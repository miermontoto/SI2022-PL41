package g41.si2022.coiipa.gestionar_lista_espera;


import java.util.List;


import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableModel;



import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

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
	    List<CursoDTO> eventos = this.getModel().getListaCursos(this.getView().getMain().getToday().toString());
	    DefaultComboBoxModel<String> cmbCursoModel = this.getView().getCmbCursoModel();

	   
	    if (!eventos.isEmpty()) for(CursoDTO evento : eventos) {
	    		String curso = evento.toString();
	            cmbCursoModel.addElement(curso);
	            comboCursos.setModel(cmbCursoModel); // Actualizar el modelo de datos del JComboBox

	    }
	}

	@Override
	public void initNonVolatileData() {
		this.getView().getBtnEliminarListaEspera().addActionListener(e -> handleEliminar());
		this.getView().getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { 
				handleSelect(); 
				}
		});
		
		this.getView().getCmbCurso().addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
		        String elementoSeleccionado = (String) comboBox.getSelectedItem();
		        System.out.println("Elemento seleccionado: " + elementoSeleccionado);
		        if(elementoSeleccionado != null) {
		        	cursoNombre = elementoSeleccionado;
		        	getListaEspera(getModel().getCursoId(elementoSeleccionado));
		        }
		        // Realiza las acciones necesarias con el elemento seleccionado
		    }
		});
	}

	@Override
	public void initVolatileData() {
		clear();
		updateCombos();
	}

	public void clear() {
		/*getView().getTxtImporte().setText("");
		getView().getDatePago().setText("");
		getView().getDateFactura().setDate(getView().getMain().getToday());*/
	}

	private void handleEliminar() {
		String id = table.getModel().getValueAt(row, 0).toString();
		getModel().eliminarInscripcion(id);
		Dialog.show("Alumno eliminado de la lista de espera con éxito");
		this.getListaEspera(getModel().getCursoId(cursoNombre));

	}

	private void handleSelect() {
		TableModel model = table.getModel();
		row = table.convertRowIndexToModel(table.getSelectedRow());
		String idString = model.getValueAt(row, 0).toString();
		id = Integer.valueOf(idString);
	}

	private void getListaEspera(String cursoID) {
		List<ListaEsperaDTO> listaespera = this.getModel().getListaEspera(cursoID);
    	System.out.printf("El curso es %s \n", cursoID);

		table.setModel(SwingUtil.getTableModelFromPojos(listaespera,
			new String[] {"id", "nombre", "apellidos", "fecha_entrada", "inscripcion_id"},
			new String[] {"", "Nombre alumno", "Apellidos", "Fecha entrada en lista de espera", "ID Inscripcion"},
			null));
		table.removeColumn(table.getColumnModel().getColumn(0));
		//table.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer(7));
		SwingUtil.autoAdjustColumns(table);
	}

}
