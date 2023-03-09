package g41.si2022.coiipa.consultar_cursos;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.coiipa.dto.InscripcionDTO;
import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.ui.SwingUtil;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.TableModel;
import g41.si2022.util.ApplicationException;
import g41.si2022.util.InscripcionState;
import g41.si2022.util.StateUtilities;

public class ConsultarCursosController {

	private ConsultarCursosModel model;
	private ConsultarCursosView view;

	private List<CursoDTO> cursos;
	private List<CursoDTO> cursosAndInscr;
	private List<PagoDTO> listaPagos;
	private String gastos;
	private String ingresosEstimados;
	private String ingresosReales;
	// dont needed
	// private String balanceEstimado;
	// private String balanceReal;
	private String costeCurso;

	public ConsultarCursosController(ConsultarCursosModel m, ConsultarCursosView v)
	{
		this.model = m;
		this.view = v;
		initView();
	}

	public void initView()
	{
		getListaCursos();
		view.getTablaCursos().addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent ent) {
				SwingUtil.exceptionWrapper(() -> getListaInscr());
				SwingUtil.exceptionWrapper(() -> balance());
			}
		});
	}

	public void getListaCursos() {
		cursosAndInscr = model.getListaCursosInscr();

		for (CursoDTO curso : cursosAndInscr)
			curso.setEstado(StateUtilities.getCursoState(curso, this.view.getMain().getToday()));

        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursosAndInscr, new String[] { "nombre", "estado", "plazas", "start", "end" },
        		new String[] { "Nombre", "Estado", "Plazas", "Fecha ini. curso", "Fecha fin curso" }, null);
        view.getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(view.getTablaCursos());
	}

	public void getListaInscr() {
		cursos = model.getListaCursos();
		for (CursoDTO curso : cursos) {
			if (curso.getNombre().equals(SwingUtil.getSelectedKey(view.getTablaCursos()))) {

				List<InscripcionDTO> listaInscr = model.getListaInscr(curso.getId());
				Double valorCurso = Double.parseDouble(curso.getCoste());
				for(InscripcionDTO inscripcion : listaInscr) {
					listaPagos = model.getListaPagos(inscripcion.getId());
					inscripcion.setEstado(StateUtilities.getInscripcionState(valorCurso, listaPagos));
				}

				TableModel tableModel = SwingUtil.getTableModelFromPojos(listaInscr,
					new String[] { "fecha", "alumno_nombre", "alumno_apellidos", "estado" },
						new String[] { "Fecha de inscripción", "Nombre", "Apellidos", "Estado" }, null);
				view.getTablaInscr().setModel(tableModel);
				SwingUtil.autoAdjustColumns(view.getTablaInscr());

				return;
			}
		}
        throw new ApplicationException("Curso seleccionado desconocido");
	}

	public void balance() {
		for (CursoDTO curso : cursosAndInscr) {
			if (curso.getNombre().equals(SwingUtil.getSelectedKey(view.getTablaCursos()))) {
				gastos = model.getGastos(curso.getId());
				ingresosEstimados = model.getIngresosEstimados(curso.getId());
				costeCurso = model.getCosteCurso(curso.getId());

				listaPagos = model.getListaPagos(curso.getId());
				InscripcionState estado = StateUtilities.getInscripcionState(Double.valueOf(Double.parseDouble(costeCurso)), listaPagos);

				ingresosReales = estado == InscripcionState.PAGADA ? model.getImportePagos(curso.getId()) : "0";

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

				view.getLblEconomicInfo().setText(sb.toString());
				return;
			}
		}
		throw new ApplicationException("Curso seleccionado desconocido");
	}

	// public void getListaInscripciones(String idCurso) {
	// 	inscripciones = model.getListaInscr(idCurso);
	// 	TableModel tableModel = SwingUtil.getTableModelFromPojos(inscripciones, new String[] { "inscripcion_fecha", "alumno_nombre" },
	// 			new String[] { "Fecha de inscripción", "Alumno"}, null);
	// 	view.getTablaInscr().setModel(tableModel);
	// 	SwingUtil.autoAdjustColumns(view.getTablaInscr());
	// }
}
