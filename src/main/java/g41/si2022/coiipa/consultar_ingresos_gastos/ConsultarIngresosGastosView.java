package g41.si2022.coiipa.consultar_ingresos_gastos;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import g41.si2022.util.BetterDatePicker;
import g41.si2022.util.CursoState;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;

public class ConsultarIngresosGastosView extends g41.si2022.ui.Tab {

	private static final long serialVersionUID = 1L;

	private JTable movimientos;
	private JComboBox<CursoState> filterEstado;
	private BetterDatePicker startDate, endDate;

	public JTable getMovimientosTable () { return this.movimientos; }
	public JComboBox<CursoState> getFilterEstadoComboBox () { return this.filterEstado; }
	public BetterDatePicker getStartDatePicker () { return this.startDate; }
	public BetterDatePicker getEndDatePicker () { return this.endDate; }

	/**
	 * Create the panel.
	 */
	public ConsultarIngresosGastosView(g41.si2022.ui.SwingMain main) {
		super(main);
		this.initView();
	}

	private void initView () {
		this.setLayout(new BorderLayout());
		// Title
		this.add(JLabelFactory.getLabel(FontType.title, "Ingresos y Gastos"), BorderLayout.NORTH);

		JPanel mainPanel = new JPanel (new BorderLayout());
		{ // Main Panel
			{ // Filters
				JPanel filters = new JPanel (new java.awt.FlowLayout());
				filters.add(JLabelFactory.getLabel("Mostrar cursos "));
				filters.add(this.filterEstado = new JComboBox<CursoState>());
				filters.add(JLabelFactory.getLabel(" entre "));
				filters.add(this.startDate = new BetterDatePicker());
				filters.add(JLabelFactory.getLabel(" y "));
				filters.add(this.endDate = new BetterDatePicker());
				mainPanel.add(filters, BorderLayout.NORTH);
			} { // Table
				JScrollPane sp = new JScrollPane();
				sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				this.movimientos = new JTable();
				this.movimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				this.movimientos.setDefaultEditor(Object.class, null);
				sp.setViewportView(this.movimientos);
				mainPanel.add(sp, BorderLayout.CENTER);
			}
		}
		this.add(mainPanel, BorderLayout.CENTER);
	}

	@Override
	protected void initController() {
		new ConsultarIngresosGastosController(this, new ConsultarIngresosGastosModel());
	}

}
