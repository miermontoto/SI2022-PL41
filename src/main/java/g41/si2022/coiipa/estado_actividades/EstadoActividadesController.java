package g41.si2022.coiipa.estado_actividades;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.ui.SwingUtil;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import g41.si2022.util.exception.ApplicationException;
import g41.si2022.util.renderer.InscripcionStatusCellRenderer;
import g41.si2022.util.state.StateUtilities;

public class EstadoActividadesController extends g41.si2022.mvc.Controller<EstadoActividadesView, EstadoActividadesModel> {

	private List<CursoDTO> cursos;
	private List<CursoDTO> cursosAndInscr;
	private List<InscripcionDTO> listaInscr;
	private String gastos;
	private String ingresosEstimados;
	private String ingresosReales;

	public EstadoActividadesController(EstadoActividadesModel m, EstadoActividadesView t) {
		super(t, m);
	}

	public void initVolatileData() {
		this.getListaCursos();
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

	public void getListaCursos() {
		cursosAndInscr = this.getModel().getListaCursosInscr();

		for (CursoDTO curso : cursosAndInscr)
			curso.setEstado(StateUtilities.getCursoState(curso, this.getTab().getMain().getToday()));

        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursosAndInscr, new String[] { "nombre", "estado", "plazas", "start", "end" },
        		new String[] { "Nombre", "Estado", "Plazas", "Fecha ini. curso", "Fecha fin curso" }, null);
        this.getView().getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(this.getView().getTablaCursos());
	}

	public void getListaInscr() {
		cursos = this.getModel().getListaCursos();
		for (CursoDTO curso : cursos) {
			if (curso.getNombre().equals(SwingUtil.getSelectedKey(this.getView().getTablaCursos()))) {

				listaInscr = this.getModel().getListaInscr(curso.getId());
				for(InscripcionDTO inscripcion : listaInscr) inscripcion.updateEstado();

				JTable table = this.getView().getTablaInscr();
				TableModel tableModel = SwingUtil.getTableModelFromPojos(listaInscr,
					new String[] { "fecha", "alumno_nombre", "alumno_apellidos", "estado" },
						new String[] { "Fecha de inscripción", "Nombre", "Apellidos", "Estado" }, null);
				table.setModel(tableModel);
				//SwingUtil.autoAdjustColumns(table);
				table.setDefaultEditor(Object.class, null);
				table.getColumnModel().getColumn(3).setCellRenderer(new InscripcionStatusCellRenderer(3));

				return;
			}
		}
        throw new ApplicationException("Curso seleccionado desconocido");
	}

	public void balance() {
		for (CursoDTO curso : cursosAndInscr) {
			if (curso.getNombre().equals(SwingUtil.getSelectedKey(this.getView().getTablaCursos()))) {
				gastos = this.getModel().getGastos(curso.getId());
				ingresosEstimados = this.getModel().getIngresosEstimados(curso.getId());
				ingresosReales = this.getModel().getImportePagosFromCurso(curso.getId());

				String balanceReal = gastos.equals("-") ? ingresosReales : String.valueOf(Double.parseDouble(ingresosReales) - Integer.parseInt(gastos));
				String balanceEstimado = gastos.equals("-") ? ingresosEstimados : String.valueOf(Double.parseDouble(ingresosEstimados) - Integer.parseInt(gastos));

				StringBuilder sb = new StringBuilder();
				sb.append("<html><body>");
				sb.append("<p><b>Gastos actuales:</b> " + gastos + "€");
				sb.append("<p><p><b>Ingresos estimados:</b> " + ingresosEstimados + "€");
				sb.append("<p><b>Balance estimado:</b> " + balanceEstimado + "€");
				sb.append("<p><p><b>Ingresos actuales:</b> " + ingresosReales + "€");
				sb.append("<p><b>Balance real:</b> " + balanceReal + "€");
				sb.append("</body></html>");
				this.getView().getLblEconomicInfo().setText(sb.toString());

				return;
			}
		}
		throw new ApplicationException("Curso seleccionado desconocido");
	}

}
