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
import g41.si2022.dto.FacturaDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.util.Dialog;
import g41.si2022.util.StatusCellRenderer;
import g41.si2022.util.state.FacturaState;

public class GestionarFacturasEmprController extends g41.si2022.mvc.Controller<GestionarFacturasEmprView, GestionarFacturasEmprModel> {
    
    private int row;
	private JTable tableCursoEmpr;
	private List<FacturaDTO> facturasEmpr;
    
    public GestionarFacturasEmprController(GestionarFacturasEmprModel modelo, GestionarFacturasEmprView vista) {
		super(vista, modelo);
		this.tableCursoEmpr = this.getView().getTableFacturasEmpr();
	}

    private Supplier<List<FacturaDTO>> supFacturasEmpr = () -> {
		List<FacturaDTO> facturasEmpr2 = getModel().getListaFacturasEmpr();
		facturasEmpr2.forEach(f -> f.updateEstado());
		if(this.getView().getChkAll().isSelected()) 
			return facturasEmpr2;

		return facturasEmpr2.stream().filter(f -> f.getEstado() != FacturaState.PAGADA).collect(Collectors.toList());
	};

    @Override
    public void initNonVolatileData() {
        this.getView().getBtnInsertarPago().addActionListener(e -> handleInsertarPago());
		this.getView().getBtnInsertarFactura().addActionListener(e -> handleInsertarFactura());
		this.getView().getTableFacturasEmpr().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
		this.getView().getChkAll().addActionListener(e -> getListaFacturasEmpr());
		this.getView().getCmbCurso().addActionListener(e -> handleSelectCurso());
    }

    @Override
    public void initVolatileData() {
        clear();
		getListaFacturasEmpr(); // Precarga inicial de la lista de facturas
		getListaCursosEmpr();   // Precarga inicial de cursos asociados a una empresa
    }

    public void clear() {
		getView().getTxtImporte().setText("");
		getView().getDatePago().setText("");
		getView().getDateFactura().setDate(getView().getMain().getToday());
	}

	private void handleSelectCurso() {
		try {
			JXComboBox cmbCurso = getView().getCmbCurso();
			String nombreEmpresa = ((CursoDTO) cmbCurso.getSelectedItem()).getE_nombre();
			getView().getLblEmpresa().setText(nombreEmpresa);
			String importeAPagar = ((CursoDTO) cmbCurso.getSelectedItem()).getImporte();
			getView().getTxtImporteFactura().setText(importeAPagar);
		} catch (NullPointerException | ClassCastException e) { 
			// No se hace nada
		}	
	}

	private void handleInsertarFactura() {
		JXComboBox cmbCurso = getView().getCmbCurso();
		String fecha = getView().getDateFactura().toString();
		String idEntidad = ((CursoDTO) cmbCurso.getSelectedItem()).getEntidad_id();
		String idCurso = ((CursoDTO) cmbCurso.getSelectedItem()).getId();
		String importe = getView().getTxtImporteFactura().getText();
		if(getModel().insertFacturaEmpresa(fecha, idCurso, idEntidad, importe)) {
			getListaFacturasEmpr();
			getView().getTxtImporteFactura().setText("");
		}
		cmbCurso.removeItem(cmbCurso.getSelectedItem());
	}

	private void handleInsertarPago() {
		String id = tableCursoEmpr.getModel().getValueAt(row, 0).toString();
		int facturasPorPagar = facturasEmpr.size();
		getModel().insertPago(
			getView().getDatePago().getDate().toString(),
			getView().getTxtImporte().getText(),
			id
		);
		getListaFacturasEmpr();
		Dialog.show("Pago registrado correctamente");
		if(facturasEmpr.size() == facturasPorPagar)
			Dialog.showWarning("El importe total no coincide con el importe de la factura");
	}

	private void handleSelect() {
		TableModel model = tableCursoEmpr.getModel();
		row = tableCursoEmpr.convertRowIndexToModel(tableCursoEmpr.getSelectedRow());

		getView().getDatePago().setDate(getView().getMain().getToday());
		getView().getTxtImporte().setText(model.getValueAt(row, 3).toString());
	}

	private void getListaFacturasEmpr() {
		facturasEmpr = supFacturasEmpr.get();
		tableCursoEmpr.setModel(SwingUtil.getTableModelFromPojos(facturasEmpr,
			new String[] {"id", "nombre_entidad", "curso_nombre", "remuneracion", "pagado", "fecha", "estado"},
			new String[] {"", "Nombre Empresa", "Curso", "Importe total", "Importe pagado", "Fecha introducción", "Estado"},
			null));
			tableCursoEmpr.removeColumn(tableCursoEmpr.getColumnModel().getColumn(0));
			tableCursoEmpr.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer(6));
		SwingUtil.autoAdjustColumns(tableCursoEmpr);
	}

	@SuppressWarnings("unchecked")
	private void getListaCursosEmpr() {
		JXComboBox cmb = getView().getCmbCurso();
		cmb.removeAllItems();
		List<CursoDTO> cursos = getModel().getListaCursosEmpr();
		cmb.addItem("Seleccione un curso");
		cursos.forEach(cmb::addItem);
	}
}
