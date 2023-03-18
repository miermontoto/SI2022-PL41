package g41.si2022.coiipa.registrar_pago_profesor;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.awt.event.MouseAdapter;

import g41.si2022.dto.FacturaDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.util.Dialog;

public class RegistrarPagoProfesorController extends g41.si2022.mvc.Controller<RegistrarPagoProfesorView, RegistrarPagoProfesorModel> {

	private int row;
	private JTable table;

	public RegistrarPagoProfesorController(RegistrarPagoProfesorModel modelo, RegistrarPagoProfesorView vista) {
		super(vista, modelo);
	}

	@Override
	public void initNonVolatileData() {
		this.table = this.getView().getTableInscripciones();
		this.getView().getBtnInsertarPago().addActionListener(e -> handleInsertar());
		this.getView().getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
		this.getView().getChkAll().addActionListener(e -> getListaFacturas());
	}

	@Override
	public void initVolatileData() {
		getListaFacturas(); // Precarga inicial de la lista de inscripciones
		setControls(false); // Inicio la vista con todo deshabilitado
	}

	private void setControls(boolean status) {
		this.getView().getBtnInsertarPago().setEnabled(status);
		this.getView().getDatePicker().setEnabled(status);
	}

	private void handleInsertar() {
		String date = this.getView().getDatePicker().getDate().toString();
		String id = table.getModel().getValueAt(row, 0).toString();
		this.getModel().updateFactura(id, date);
		getListaFacturas();
		Dialog.show("Pago registrado correctamente");
	}

	private void handleSelect() {
		TableModel model = table.getModel();
		row = table.getSelectedRow();

		if(row == -1) {
			eraseControls(true);
			return;
		}

		this.getView().getLblNombreCurso().setText(model.getValueAt(row, 3).toString());
		this.getView().getLblNombreDocente().setText(model.getValueAt(row, 1).toString() + " " + model.getValueAt(row, 2).toString());
		String date = model.getValueAt(row, 6).toString();
		if (date.equals("")) {
			this.getView().getDatePicker().setDateToToday();
			setControls(true);
		} else {
			this.getView().getDatePicker().setDate(LocalDate.parse(date));
			setControls(false);
		}
	}

	private void getListaFacturas() {
		List<FacturaDTO> listaFacturas;
		String today = this.getView().getMain().getToday().toString();
		listaFacturas = this.getView().getChkAll().isSelected() ? this.getModel().getListaFacturas(today) : this.getModel().getListaFacturasSinPagar(today);
		table.setModel(SwingUtil.getTableModelFromPojos(listaFacturas,
			new String[] {"id", "doc_nombre", "doc_apellidos", "curso_nombre", "remuneracion", "fecha_introd", "fecha_pago"},
			new String[] {"", "Nombre", "Apellidos", "Curso", "€", "Fecha int.", "Fecha pago"},
			null));
		table.removeColumn(table.getColumnModel().getColumn(0));
		//SwingUtil.autoAdjustColumns(table);
	}

	private void eraseControls(boolean eliminarAviso) {
		this.getView().getLblNombreDocente().setText("N/A");
		this.getView().getLblNombreCurso().setText("N/A");
		this.getView().getDatePicker().setText("");
		if(eliminarAviso) this.getView().getLblError().setText("");
		setControls(false);
	}
}
