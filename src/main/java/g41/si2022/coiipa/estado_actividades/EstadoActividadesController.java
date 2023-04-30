package g41.si2022.coiipa.estado_actividades;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.ui.SwingUtil;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import g41.si2022.util.StatusCellRenderer;
import g41.si2022.util.Util;
import g41.si2022.util.exception.ApplicationException;
import g41.si2022.util.state.StateUtilities;

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
		this.getView().getTablaCursos().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent ent) {
				SwingUtil.exceptionWrapper(() -> getListaInscr());
				SwingUtil.exceptionWrapper(() -> balance());
			}
		});
	}

	public void clear() {
		Util.emptyTable(getView().getTablaInscr());
		getView().getLblEconomicInfo().setText("");
	}

	public void getListaCursos() {
		cursos = this.getModel().getListaCursos();

		for (CursoDTO curso : cursos)
			curso.setEstado(StateUtilities.getCursoState(curso, this.getTab().getMain().getToday()));

        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursos, new String[] { "nombre", "estado", "plazas", "start_inscr", "end_inscr", "start", "end" },
        		new String[] { "Nombre", "Estado", "Plazas", "Fecha ini. inscr.", "Fecha fin inscr.", "Fecha ini. curso", "Fecha fin curso" }, null);
        this.getView().getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(this.getView().getTablaCursos());
	}

	public void getListaInscr() {
		List<InscripcionDTO> listaInscr;
		cursos = this.getModel().getListaCursos();
		for (CursoDTO curso : cursos) {
			if (curso.getNombre().equals(SwingUtil.getSelectedKey(this.getView().getTablaCursos()))) {
				listaInscr = this.getModel().getListaInscr(curso.getId());
				for(InscripcionDTO inscripcion : listaInscr) inscripcion.updateEstado(getView().getMain().getToday());

				JTable table = this.getView().getTablaInscr();
				TableModel tableModel = SwingUtil.getTableModelFromPojos(listaInscr,
					new String[] { "fecha", "alumno_nombre", "alumno_apellidos", "curso_coste" , "pagado", "estado", "entidad_nombre" },
						new String[] { "Fecha de inscripción", "Nombre", "Apellidos", "Coste", "Pagado", "Estado", "Entidad" }, null);
				table.setModel(tableModel);
				table.setDefaultEditor(Object.class, null);
				table.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer(5));
				TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
				table.setRowSorter(sorter);
				List<RowSorter.SortKey> sortKeys = new ArrayList<>();
				sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
				sorter.setSortKeys(sortKeys);

				return;
			}
		}
        throw new ApplicationException("Curso seleccionado desconocido");
	}

	public void balance() {
		String gastos;
		String ingresosEstimados;
		String ingresosReales;

		for (CursoDTO curso : cursos) {
			if (curso.getNombre().equals(SwingUtil.getSelectedKey(this.getView().getTablaCursos()))) {
				gastos = this.getModel().getGastos(curso.getId());
				ingresosEstimados = this.getModel().getIngresosEstimados(curso.getId());
				ingresosReales = this.getModel().getImportePagosFromCurso(curso.getId());

				String balanceReal = gastos.equals("-") ? ingresosReales : String.valueOf(Double.parseDouble(ingresosReales) - Integer.parseInt(gastos));
				String balanceEstimado = gastos.equals("-") ? ingresosEstimados : String.valueOf(Double.parseDouble(ingresosEstimados) - Integer.parseInt(gastos));

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

				return;
			}
		}
		throw new ApplicationException("Curso seleccionado desconocido");
	}

}
