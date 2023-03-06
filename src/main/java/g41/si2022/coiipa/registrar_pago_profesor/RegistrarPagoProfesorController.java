package g41.si2022.coiipa.registrar_pago_profesor;

import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import java.awt.event.MouseEvent;
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

	}

	private void getListaFacturas() {
		List<FacturaDTO> listaFacturas;
		JTable table = view.getTableInscripciones();
		String today = view.getMain().getToday().toString();
		listaFacturas = view.getChkAll().isSelected() ? model.getListaFacturas(today) : model.getListaFacturasSinPagar(today);
		table.setModel(SwingUtil.getTableModelFromPojos(listaFacturas,
			new String[] {"id", "remuneracion", "fecha_introd", "fecha_pago"},
			new String[] {"ID", "Importe", "Fecha de introducción", "Fecha de pago"},
			null));
		table.removeColumn(table.getColumnModel().getColumn(0));
	}

	private void eraseControls(boolean eliminarAviso) {
		view.getLblNombreDocente().setText("No se ha seleccionado ninguna factura");
		view.getTxtImporte().setText("");
		view.getDatePicker().setText("");
		if(eliminarAviso) view.getLblError().setText("");
		setControls(false);
	}
}
