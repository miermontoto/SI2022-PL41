package g41.si2022.coiipa.registrar_pago_profesor;

import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.awt.event.MouseAdapter;

import g41.si2022.coiipa.dto.FacturaDTO;
import g41.si2022.util.SwingUtil;

public class RegistrarPagoProfesorController {

	private RegistrarPagoProfesorView view;
	private RegistrarPagoProfesorModel model;

	public RegistrarPagoProfesorController(RegistrarPagoProfesorView vista, RegistrarPagoProfesorModel modelo) {
		this.view = vista;
		this.model = modelo;
		this.initView();
	}

	private String idFactura = null;

	public void initView() {
		getListaFacturas(); // Precarga inicial de la lista de inscripciones
		setControls(false); // Inicio la vista con todo deshabilitado

		view.getBtnInsertarPago().addActionListener(e -> handleInsertar());
		view.getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
		view.getChkAll().addActionListener(e -> getListaFacturas());
	}

	private void setControls(boolean status) {
		view.getBtnInsertarPago().setEnabled(status);
		view.getDatePicker().setEnabled(status);
	}

	private void handleInsertar() {

	}

	private void handleSelect() {
		JTable table = view.getTableInscripciones();
		TableModel model = table.getModel();
		int row = table.getSelectedRow();

		if(row == -1) {
			eraseControls(true);
			return;
		}

		view.getLblNombreCurso().setText(model.getValueAt(row, 3).toString());
		view.getLblNombreDocente().setText(model.getValueAt(row, 1).toString() + " " + model.getValueAt(row, 2).toString());
		String date = model.getValueAt(row, 6).toString();
		if (date.equals("")) {
			view.getDatePicker().setDateToToday();
			setControls(true);
		} else {
			view.getDatePicker().setDate(LocalDate.parse(date));
			setControls(false);
		}
	}

	private void getListaFacturas() {
		List<FacturaDTO> listaFacturas;
		JTable table = view.getTableInscripciones();
		String today = view.getMain().getToday().toString();
		listaFacturas = view.getChkAll().isSelected() ? model.getListaFacturas(today) : model.getListaFacturasSinPagar(today);
		table.setModel(SwingUtil.getTableModelFromPojos(listaFacturas,
			new String[] {"id", "doc_nombre", "doc_apellidos", "curso_nombre", "remuneracion", "fecha_introd", "fecha_pago"},
			new String[] {"", "", "", "", "€", "Fecha int.", "Fecha pago"},
			null));
		for(int i=0;i<3;i++) table.removeColumn(table.getColumnModel().getColumn(1));
		SwingUtil.autoAdjustColumns(table);
	}

	private void eraseControls(boolean eliminarAviso) {
		view.getLblNombreDocente().setText("N/A");
		view.getLblNombreCurso().setText("N/A");
		view.getDatePicker().setText("");
		if(eliminarAviso) view.getLblError().setText("");
		setControls(false);
	}
}
