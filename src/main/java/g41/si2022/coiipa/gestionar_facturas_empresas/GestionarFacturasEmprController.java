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
	private JTable table;
	private List<FacturaDTO> facturas;
    
    public GestionarFacturasEmprController(GestionarFacturasEmprModel modelo, GestionarFacturasEmprView vista) {
		super(vista, modelo);
		this.table = this.getView().getTableFacturasProf();
	}

    @Override
    public void initNonVolatileData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initNonVolatileData'");
    }

    @Override
    public void initVolatileData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initVolatileData'");
    }
}
