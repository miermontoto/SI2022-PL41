package g41.si2022.coiipa.consultar_ingresos_gastos;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import g41.si2022.ui.components.BetterDatePicker;
import g41.si2022.ui.components.JLabelFactory;
import g41.si2022.ui.util.FontType;
import g41.si2022.util.state.CursoState;

public class ConsultarIngresosGastosView extends g41.si2022.mvc.View {

	private static final long serialVersionUID = 1L;

	private JTable movimientos;
	private JTable offMovimientos;
	private JComboBox<CursoState> filterEstado;
	private BetterDatePicker startDate, endDate;

	public JTable getMovimientosTable () { return this.movimientos; }
	public JTable getOffMovimientosTable () { return this.offMovimientos; }
	public JComboBox<CursoState> getFilterEstadoComboBox () { return this.filterEstado; }
	public BetterDatePicker getStartDatePicker () { return this.startDate; }
	public BetterDatePicker getEndDatePicker () { return this.endDate; }

	/**
	 * Create the panel.
	 */
	public ConsultarIngresosGastosView(g41.si2022.ui.SwingMain main) {
		super(main, ConsultarIngresosGastosModel.class, ConsultarIngresosGastosView.class, ConsultarIngresosGastosController.class);
	}

	@Override
	protected void initView () {
		this.setLayout(new BorderLayout());

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
			} {  // Tables
				JPanel tablePanel = new JPanel (new java.awt.GridLayout(0, 1));
				JPanel offsetTablePanel = new JPanel (new BorderLayout());
				{ // All Table
					JScrollPane sp = new JScrollPane();
					sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					this.movimientos = new JTable();
					this.movimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					this.movimientos.setDefaultEditor(Object.class, null);
					sp.setViewportView(this.movimientos);
					tablePanel.add(sp);
				} { // Message
					offsetTablePanel.add(JLabelFactory.getLabel(FontType.subtitle,
							"De los anteriores cursos, estos han recibido ingresos/gastos fuera de las fechas filtradas."),
							BorderLayout.NORTH);
				} { // Offset Table
					JScrollPane sp = new JScrollPane();
					sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					this.offMovimientos = new JTable();
					this.offMovimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					this.offMovimientos.setDefaultEditor(Object.class, null);
					sp.setViewportView(this.offMovimientos);
					offsetTablePanel.add(sp, BorderLayout.CENTER);
				}
				mainPanel.add(tablePanel, BorderLayout.CENTER);
				tablePanel.add(offsetTablePanel);
			}
		}
		this.add(mainPanel, BorderLayout.CENTER);
	}

}
