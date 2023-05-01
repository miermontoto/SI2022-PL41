package g41.si2022.coiipa.gestionar_facturas_empresas;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXComboBox;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.DocenciaDTO;
import g41.si2022.dto.FacturaDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.util.Dialog;
import g41.si2022.util.StatusCellRenderer;
import g41.si2022.util.state.FacturaState;

public class GestionarFacturasEmprController extends g41.si2022.mvc.Controller<GestionarFacturasEmprView, GestionarFacturasEmprModel> {
    
    private int row;
	private JTable tableFacturasEmpr;
	private List<FacturaDTO> facturas;
    
    public GestionarFacturasEmprController(GestionarFacturasEmprModel modelo, GestionarFacturasEmprView vista) {
		super(vista, modelo);
		this.tableFacturasEmpr = this.getView().getTableFacturasEmpr();
	}

    private Supplier<List<FacturaDTO>> supFacturas = () -> {
		List<FacturaDTO> facturas = getModel().getListaFacturasEmpr();
		facturas.forEach(f -> f.updateEstado());
		if(this.getView().getChkAll().isSelected()) return facturas;
		return facturas.stream().filter(f -> f.getEstado() != FacturaState.PAGADA).collect(Collectors.toList());
	};

    @Override
    public void initNonVolatileData() {
        this.getView().getBtnInsertarPago().addActionListener(e -> handleInsertarPago());
		this.getView().getBtnInsertarFactura().addActionListener(e -> handleInsertarFactura());
		this.getView().getTableFacturasEmpr().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
		this.getView().getChkAll().addActionListener(e -> getListaFacturas());
		this.getView().getCmbCurso().addActionListener(e -> getListaDocentes());
    }

    @Override
    public void initVolatileData() {
        clear();
		getListaFacturas(); // Precarga inicial de la lista de inscripciones
		getListaCursos();
    }

    public void clear() {
		getView().getTxtImporte().setText("");
		getView().getDatePago().setText("");
		getView().getDateFactura().setDate(getView().getMain().getToday());
	}

	private void handleInsertarFactura() {
		String fecha = getView().getDateFactura().toString();
		String idDocencia = ((DocenciaDTO) getView().getCmbEmpresa().getSelectedItem()).getId();
		String importe = getView().getTxtImporteFactura().getText().toString();
		if(getModel().insertFactura(fecha, idDocencia, importe)) {
			getListaFacturas();
			getListaDocentes();
			getView().getTxtImporteFactura().setText("");
		}

	}

	private void handleInsertarPago() {
		String id = tableFacturasEmpr.getModel().getValueAt(row, 0).toString();
		int facturasPorPagar = facturas.size();
		getModel().insertPago(
			getView().getDatePago().getDate().toString(),
			getView().getTxtImporte().getText(),
			id
		);
		getListaFacturas();
		Dialog.show("Pago registrado correctamente");
		if(facturas.size() == facturasPorPagar)
			Dialog.showWarning("El importe total no coincide con el importe de la factura");
	}

	private void handleSelect() {
		TableModel model = tableFacturasEmpr.getModel();
		row = tableFacturasEmpr.convertRowIndexToModel(tableFacturasEmpr.getSelectedRow());

		getView().getDatePago().setDate(getView().getMain().getToday());
		getView().getTxtImporte().setText(model.getValueAt(row, 4).toString());
	}

	private void getListaFacturas() {
		facturas = supFacturas.get();
		tableFacturasEmpr.setModel(SwingUtil.getTableModelFromPojos(facturas,
			new String[] {"id", "doc_nombre", "doc_apellidos", "curso_nombre", "remuneracion", "pagado", "fecha", "estado"},
			new String[] {"", "Nombre", "Apellidos", "Curso", "Importe total", "Importe pagado", "Fecha introducción", "Estado"},
			null));
			tableFacturasEmpr.removeColumn(tableFacturasEmpr.getColumnModel().getColumn(0));
			tableFacturasEmpr.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer(7));
		SwingUtil.autoAdjustColumns(tableFacturasEmpr);
	}

	@SuppressWarnings("unchecked")
	private void getListaCursos() {
		JXComboBox cmb = getView().getCmbCurso();
		cmb.removeAllItems();
		List<CursoDTO> cursos = getModel().getListaCursos(getView().getMain().getToday().toString());
		cmb.addItem("Seleccione un curso");
		cursos.forEach(x -> cmb.addItem(x));
	}

	@SuppressWarnings("unchecked")
	private void getListaDocentes() {
		getView().getCmbEmpresa().removeAllItems();
		try {
			String idCurso = ((CursoDTO) getView().getCmbCurso().getSelectedItem()).getId();
			List<DocenciaDTO> docentes = getModel().getListaDocentes(idCurso);
			docentes.forEach(x -> getView().getCmbEmpresa().addItem(x));
			getView().getCmbEmpresa().setEnabled(true);
			getView().getBtnInsertarFactura().setEnabled(true);
		} catch (ClassCastException | NullPointerException e) {
			getView().getCmbEmpresa().setEnabled(false);
			getView().getBtnInsertarFactura().setEnabled(false);
		} // No se ha seleccionado un curso.
	}
}
