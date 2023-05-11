package g41.si2022.coiipa.estado_actividades;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SortOrder;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.util.Pair;
import g41.si2022.util.StatusCellRenderer;
import g41.si2022.util.Util;
import g41.si2022.util.exception.ApplicationException;

public class EstadoActividadesController extends g41.si2022.mvc.Controller<EstadoActividadesView, EstadoActividadesModel> {

	private List<CursoDTO> cursos;

	public EstadoActividadesController(EstadoActividadesModel m, EstadoActividadesView t) {
		super(t, m);
	}

	public void initVolatileData() {
		clear();
		getListaCursos();
	}

	public void initNonVolatileData() {
		JTable table = getView().getTablaCursos();
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
				SwingUtil.exceptionWrapper(this::getListaInscr);
				SwingUtil.exceptionWrapper(this::balance);
			}
		});
	}

	public void clear() {
		Util.emptyTable(getView().getTablaInscr());
		getView().getLblEconomicInfo().setText("");
	}

	public void getListaCursos() {
		cursos = this.getModel().getListaCursos();
		JTable table = getView().getTablaCursos();
		LocalDate today = getToday();

		cursos.forEach(x -> {
			x.updateState(today);
			x.updateType();
		});

        table.setModel(SwingUtil.getTableModelFromPojos(cursos, new String[] { "id", "nombre", "state", "plazas", "start_inscr", "end_inscr", "start", "end", "tipo" },
        	new String[] { "", "Nombre", "Estado", "Plazas", "Fecha ini. inscr.", "Fecha fin inscr.", "Fecha ini. curso", "Fecha fin curso", "Tipo" }, null));
        SwingUtil.autoAdjustColumns(table);
		Util.removeColumn(table, 0);
	}

	@SuppressWarnings("unchecked") // Por el Util.sortOrder
	public void getListaInscr() {
		JTable table = getView().getTablaInscr();
		List<InscripcionDTO> inscripciones = this.getModel().getListaInscr(this.getId());
		LocalDate today = getToday();
		inscripciones.forEach(x -> x.updateEstado(today));

		table.setModel(SwingUtil.getTableModelFromPojos(inscripciones,
		new String[] { "fecha", "alumno_nombre", "alumno_apellidos", "curso_coste" , "pagado", "state", "entidad_nombre" },
			new String[] { "Fecha de inscripción", "Nombre", "Apellidos", "Coste", "Pagado", "Estado", "Entidad" }, null));
		table.setDefaultEditor(Object.class, null);
		table.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer(5));
		Util.sortTable(table, new Pair<Integer, SortOrder>(0, SortOrder.DESCENDING));
	}

	private String getId() {
		JTable table = getView().getTablaCursos();
		return table.getModel().getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 0).toString();
	}

	public void balance() {
		String gastos;
		String ingresosEstimados;
		String ingresosReales;

		CursoDTO curso = cursos.stream().filter(x -> x.getId().equals(getId())).findFirst().orElseThrow(() -> new ApplicationException("No se ha encontrado el curso"));

		gastos = curso.getImporte() == null ? curso.getGastos() : curso.getImporte();
		ingresosEstimados = this.getModel().getIngresosEstimados(curso.getId());
		ingresosReales = this.getModel().getImportePagosFromCurso(curso.getId());
		String balanceReal = gastos.equals("-") ? ingresosReales : String.valueOf(Double.parseDouble(ingresosReales) - Double.parseDouble(gastos));
		String balanceEstimado = gastos.equals("-") ? ingresosEstimados : String.valueOf(Double.parseDouble(ingresosEstimados) - Double.parseDouble(gastos));
		String colorReal = Double.parseDouble(balanceReal) > 0 ? "green" : "red";
		String colorEstimado = Double.parseDouble(balanceEstimado) > 0 ? "green" : "red";
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		sb.append("<p><b>Gastos actuales:</b> " + gastos + "€");
		sb.append("<p><p><b>Ingresos estimados:</b> " + ingresosEstimados + "€");
		sb.append("<p><b>Balance estimado:</b> <span style=\"color:" + colorEstimado + ";\">" + balanceEstimado + "€</span>");
		sb.append("<p><p><b>Ingresos actuales:</b> " + ingresosReales + "€");
		sb.append("<p><b>Balance real:</b> <span style=\"color:" + colorReal + ";\">" + balanceReal + "€</span>");
		sb.append("</body></html>");
		this.getView().getLblEconomicInfo().setText(sb.toString());

	}

}
