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
	private List<InscripcionDTO> inscripciones;
	private List<PagoDTO> listaPagos;
	private String gastos;
	private String ingresosEstimados;
	private String ingresosReales;
	private String balanceEstimado;
	private String balanceReal;
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
		view.getTablaCursos().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent ent) {
				SwingUtil.exceptionWrapper(() -> getValueCurso());
				SwingUtil.exceptionWrapper(() -> balance());
			}
		});
	}

	public void getListaCursos() {
		cursos = model.getListaCursos();
        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursos, new String[] { "nombre", "estado", "plazas", "start", "end" },
        		new String[] { "Nombre", "Estado", "Plazas", "Fecha ini. curso", "Fecha fin curso" }, null);
        view.getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(view.getTablaCursos());
	}

	public void getValueCurso() {
		for (CursoDTO curso : cursos) {
			if (curso.getNombre().equals(SwingUtil.getSelectedKey(view.getTablaCursos()))) {
				getListaInscripciones(curso.getId());
				return;
			}
		}
        throw new ApplicationException("Curso seleccionado desconocido");
	}

	public void balance() {
		for (CursoDTO curso : cursos) {
			if (curso.getNombre().equals(SwingUtil.getSelectedKey(view.getTablaCursos()))) {
				gastos = model.getGastos(curso.getId());
				ingresosEstimados = model.getIngresosEstimados(curso.getId());
				costeCurso = model.getCosteCurso(curso.getId());

				balanceEstimado = gastos.equals("-") ? ingresosEstimados : String.valueOf(Double.parseDouble(ingresosEstimados) - Integer.parseInt(gastos));
				StringBuilder sb = new StringBuilder();

				sb.append("<html><body>");
				sb.append("<p><b>Gastos:</b> " + gastos + "€");
				sb.append("<p><b>Ingresos estimados:</b> " + ingresosEstimados + "€");
				sb.append("<p><b>Balance estimado:</b> " + balanceEstimado + "€");

				listaPagos = model.getListaPagos(curso.getId());
				InscripcionState estado = StateUtilities.getInscripcionState(Double.valueOf(Double.parseDouble(costeCurso)), listaPagos);

				if (estado == InscripcionState.PAGADA)
					ingresosReales =  model.getImportePagos(curso.getId());
				else
					ingresosReales = "0";

				balanceReal = gastos.equals("-") ? ingresosReales : String.valueOf(Double.parseDouble(ingresosReales) - Integer.parseInt(gastos));
				sb.append("<p><b>Ingresos reales:</b> " + ingresosReales + "€");
				sb.append("<p><b>Balance real:</b> " + balanceReal + "€");
				sb.append("</body></html>");

				view.getLblEconomicInfo().setText(sb.toString());

				return;
			}
		}
		throw new ApplicationException("Curso seleccionado desconocido");
	}

	public void getListaInscripciones(String idCurso) {
		inscripciones = model.getListaInscr(idCurso);
		TableModel tableModel = SwingUtil.getTableModelFromPojos(inscripciones, new String[] { "inscripcion_fecha", "alumno_nombre" },
				new String[] { "Fecha de inscripción", "Alumno"}, null);
		view.getTablaInscr().setModel(tableModel);
		SwingUtil.autoAdjustColumns(view.getTablaInscr());
	}
}
