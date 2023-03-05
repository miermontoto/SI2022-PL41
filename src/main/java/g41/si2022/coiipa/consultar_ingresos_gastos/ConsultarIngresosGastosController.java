package g41.si2022.coiipa.consultar_ingresos_gastos;

import java.util.stream.Stream;

import javax.swing.JTable;

import java.util.List;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Iterator;

import g41.si2022.util.BetterDatePicker;
import g41.si2022.util.CursoState;
import g41.si2022.util.StateUtilities;
import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.ui.SwingUtil;

public class ConsultarIngresosGastosController {

	private ConsultarIngresosGastosView view;
	private ConsultarIngresosGastosModel model;

	private List<CursoDTO> cursos;
	private java.util.function.Supplier<List<CursoDTO>> sup = () -> {
		// return fillOtherData(filterData()); // TODO: fillOtherData with the Ingresos and Gastos
		return filterData();
	};

	/**
	 * Fills other data that is not originally in the data set.
	 * Particularly, it will fill the following columns:
	 * 	- Balance
	 *
	 * In most case scenarios, the list passed to this function is the one returned by <code>filterData</code>
	 *
	 * @param data Source data set
	 * @return Data set with extra data added
	 */
	private List<CursoDTO> fillOtherData (List<CursoDTO> data) {
		Iterator<CursoDTO> otherData = this.model.getCursosBalance().iterator();
		Iterator<CursoDTO> mainData = data.iterator();
		while (otherData.hasNext() && mainData.hasNext()) {
			CursoDTO currentMain = mainData.next();
			CursoDTO currentOther = otherData.next();
			currentMain.setIngresos(currentOther.getIngresos());
			currentMain.setGastos(currentOther.getGastos());
			currentMain.setBalance(currentOther.getBalance());
		}
		return data;
	}

	/**
	 * Filters the data set.
	 * The data set contains all the entries no matter what filters are set.
	 * This function will return another List but will have the data filtered depending on:
	 *  - State of the course
	 *  - Start date of the filter
	 *  - End date of the filter
	 *
	 *  In most case scenarios, the list returned by this function will then be passed to <code>fillOtherData</code>.
	 *
	 * @return Filtered data set
	 */
	private List<CursoDTO> filterData () {
		// Get the selected item from the the filter
		CursoState selectedItem = (CursoState) this.view.getFilterEstadoComboBox().getSelectedItem();
		ArrayList<CursoDTO>
			output = new ArrayList<CursoDTO> (), // Will contain the entries that meet the filter
			aux; // Is used as auxiliary list to avoid concurrent modifications

		// FIRST : WE FILTER THE STATES
		if (selectedItem.equals(CursoState.CUALQUIERA)) { // If the CB has chosen ANY, the output array will contain all entries
			output.addAll(this.cursos);
		} else { // If the CB has chosen something else, the entries are filtered
			this.cursos.forEach((x) -> {
				if (selectedItem.equals(x.getEstado())) {
					output.add(x);
				}
			});
		}
		aux = new ArrayList<CursoDTO>(output); // DO NOT REMOVE -> Concurrent Modifications will happen if removed

		// SECOND : WE FILTER THE DATES
		java.time.LocalDate
			start = this.view.getStartDatePicker().getDate(), // All entries' end date must be higher than the filter's start date
			end = this.view.getEndDatePicker().getDate(); // All entries' start date must be lower than the filter's end date
		if (start != null || end != null) {
			aux.forEach((x) -> {
				if (start != null && start.toString().compareTo(x.getEnd()) > 0) { // We remove the entries whose end date is lower than the filter's start date
					output.remove(x);
				}
				if (end != null && end.toString().compareTo(x.getStart_inscr()) < 0) { // We remove the entries whose start date is higher than the filter's end date
					output.remove(x);
				}
			});
		}
		return output; // We return the filtered array
	}

	public ConsultarIngresosGastosController (ConsultarIngresosGastosView view, ConsultarIngresosGastosModel model) {
		this.view = view;
		this.model = model;
		this.cursos = new ArrayList<CursoDTO> ();
		this.initView();
	}

	private void initView () {
		// this.cursos = this.model.getCursosList();
		this.cursos = this.model.getCursosBalance();
		this.cursos.forEach((x) -> x.setEstado(StateUtilities.getCursoState(x, this.view.getMain().getToday())));
		this.loadComboBox();
		this.loadTable();
		this.loadDateListeners();
	}

	private void loadComboBox () {
		Stream.of(CursoState.values()).forEach(e -> this.view.getFilterEstadoComboBox().addItem(e));
		this.view.getFilterEstadoComboBox().addItemListener((e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				this.loadTable();
			}
		});
	}

	private void loadTable () {
		this.view.getMovimientosTable().setModel(
				SwingUtil.getTableModelFromPojos(
						this.sup.get(),
						new String[] { "nombre", "start_inscr", "end_inscr", "start", "end", "ingresos", "gastos", "balance" },
						new String[] { "Nombre", "Inicio Inscripciones", "Fin Inscripciones", "Inicio Curso", "Fin Curso", "Ingresos", "Gastos", "Balance" },
						null
						)
				);
	}

	private void loadDateListeners() {
		BetterDatePicker start = this.view.getStartDatePicker();
		BetterDatePicker end = this.view.getEndDatePicker();
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
