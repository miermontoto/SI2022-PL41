package g41.si2022.coiipa.consultar_ingresos_gastos;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JTable;

import java.util.List;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

import g41.si2022.dto.CursoDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.components.BetterDatePicker;
import g41.si2022.util.state.CursoState;
import g41.si2022.util.state.StateUtilities;

public class ConsultarIngresosGastosController extends g41.si2022.mvc.Controller<ConsultarIngresosGastosView, ConsultarIngresosGastosModel> {

	private List<CursoDTO> cursos;
	private java.util.function.Supplier<List<CursoDTO>> sup = () -> filterData();
	private java.util.function.Supplier<List<CursoDTO>> supOutOfRangeDates = () -> filterData().stream().filter(x ->
		 	(this.getView().getEndDatePicker().getDate() != null && x.getPagoHighestFecha() != null &&
		 		x.getPagoHighestFecha().compareTo(this.getView().getEndDatePicker().getDate().toString()) > 0) ||
			(this.getView().getStartDatePicker().getDate() != null && x.getPagoLowestFecha() != null &&
				x.getPagoLowestFecha().compareTo(this.getView().getStartDatePicker().getDate().toString()) < 0)
	).collect(Collectors.toList());

	/**
	 * Filters the data set.
	 * The data set contains all the entries no matter what filters are set.
	 * This function will return another List but will have the data filtered depending on:
	 *  - State of the course
	 *  - Start date of the filter
	 *  - End date of the filter
	 *
	 * @return Filtered data set rubén beta (mg)
	 */
	private List<CursoDTO> filterData () {
		// Get the selected item from the the filter
		CursoState selectedItem = (CursoState) this.getView().getFilterEstadoComboBox().getSelectedItem();
		List<CursoDTO> output = new ArrayList<CursoDTO>(), // Will contain the entries that meet the filter
			aux; // Is used as auxiliary list to avoid concurrent modifications

		// FIRST : WE FILTER THE STATES
		if (selectedItem == null || selectedItem.equals(CursoState.CUALQUIERA)) { // If the CB has chosen ANY, the output array will contain all entries
			output.addAll(this.cursos);
		} else { // If the CB has chosen something else, the entries are filtered
			output = this.cursos.stream()
			.filter(x -> selectedItem.equals(x.getEstado()))
			.collect(Collectors.toList());
		}
		aux = new ArrayList<CursoDTO>(output); // DO NOT REMOVE -> Concurrent Modifications will happen if removed

		// SECOND : WE FILTER THE DATES
		java.time.LocalDate
		start = this.getView().getStartDatePicker().getDate(), // All entries' end date must be higher than the filter's start date
		end = this.getView().getEndDatePicker().getDate(); // All entries' start date must be lower than the filter's end date
		if (start != null) {
			aux.stream()
			.filter(x -> start.toString().compareTo(x.getEnd()) > 0)	// We remove the entries whose end date is lower than the filter's start date
			.forEach(output::remove);
		}
		if (end != null) {
			aux.stream()
			.filter(x -> end.toString().compareTo(x.getStart_inscr()) < 0)	// We remove the entries whose start date is higher than the filter's end date
			.forEach(output::remove);
		}
		// THIRD: We add the balance to each row (just in case it hasn't been added yet).
		// TODO: Can this be avoided?
		output.forEach(
				x -> x.setBalance(
						String.format("%.2f",
								Double.parseDouble(x.getIngresos() == null ? "0.0" : x.getIngresos()) -
								Double.parseDouble(x.getGastos() == null ? "0.0" : x.getGastos()))
						));
		return output; // We return the filtered array
	}

	/**
	 * Creates a new Controller
	 *
	 * @param view View that this controller is controlling.
	 * @param model Model that this controller uses.
	 */
	public ConsultarIngresosGastosController (ConsultarIngresosGastosModel model, ConsultarIngresosGastosView view) {
		super(view, model);
	}

	@Override
	public void initVolatileData() {
		this.cursos = this.getModel().getCursosBalance();
		this.cursos.forEach((x) -> x.setEstado(StateUtilities.getCursoState(x, this.getView().getMain().getToday(), false)));
		this.loadTable();
	}

	@Override
	public void initNonVolatileData() {
		loadComboBox();
		loadDateListeners();
	}

	/**
	 * Loads the listeners and data needed for the different JComboBoxes in the view.
	 */
	private void loadComboBox () {
		Stream.of(CursoState.values()).forEach(e -> this.getView().getFilterEstadoComboBox().addItem(e));
		this.getView().getFilterEstadoComboBox().addItemListener((e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				this.loadTable();
			}
		});
	}

	/**
	 * Loads the listeners and data needed for the different JTables in the view.
	 */
	private void loadTable() {
		JTable table = this.getView().getMovimientosTable();
		table.setModel(
			SwingUtil.getTableModelFromPojos(
				this.sup.get(),
				new String[] { "nombre", "start_inscr", "end_inscr", "start", "end", "ingresos", "gastos", "balance" },
				new String[] { "Nombre", "Inicio Inscripciones", "Fin Inscripciones", "Inicio Curso", "Fin Curso", "Ingresos", "Gastos", "Balance" },
				null
			)
		);
		getView().getOffMovimientosTable().setModel(
			SwingUtil.getTableModelFromPojos(
				this.supOutOfRangeDates.get(),
				new String[] { "nombre", "start_inscr", "end_inscr", "start", "end", "ingresos", "gastos", "balance" },
				new String[] { "Nombre", "Inicio Inscripciones", "Fin Inscripciones", "Inicio Curso", "Fin Curso", "Ingresos", "Gastos", "Balance" },
				null
			)
		);
		SwingUtil.autoAdjustColumns(table);
	}

	/**
	 * Loads the listeners and data needed for the different Dates in the view.
	 */
	private void loadDateListeners() {
		BetterDatePicker start = this.getView().getStartDatePicker();
		BetterDatePicker end = this.getView().getEndDatePicker();
		start.addDateChangeListener((e) -> {
			if (start.getDate() != null && end.getDate() != null && start.compareTo(end) >= 0) {
				end.setDate(start.getDate().plusDays(1));
			}
		});
		end.addDateChangeListener((e) -> {
			if (end.getDate() != null && start.getDate() != null && start.compareTo(end) >= 0) {
				start.setDate(end.getDate().plusDays(-1));
			}
			this.loadTable();
		});
	}

}
