package g41.si2022.coiipa.inscribir_multiples_usuarios_entidad;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.swing.table.TableModel;

import g41.si2022.dto.AlumnoDTO;
import g41.si2022.dto.ColectivoDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.GrupoDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.util.Dialog;
import g41.si2022.util.Util;
import g41.si2022.util.exception.ApplicationException;

public class InscribirMultiplesUsuariosEntidadController extends g41.si2022.mvc.Controller<InscribirMultiplesUsuariosEntidadView, InscribirMultiplesUsuariosEntidadModel> {

	private static final String SIGN_IN = "sign-in";
	private static final String SIGN_UP = "sign-up";

	private List<CursoDTO> cursos;
	private String cursoId;

	public InscribirMultiplesUsuariosEntidadController(InscribirMultiplesUsuariosEntidadModel m, InscribirMultiplesUsuariosEntidadView v) {
		super(v, m);
		this.cursoId = null;
	}

	@Override
	public void initVolatileData() {
		this.getListaCursos();
	}

	@Override
	public void initNonVolatileData() {
		this.getView().getTablaCursos().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent ent) {
				SwingUtil.exceptionWrapper(() -> updateCursoValue());
				InscribirMultiplesUsuariosEntidadController.this
					.getView().getComboBoxEditors().parallelStream()
				.forEach(cbe -> 
					cbe.setData(
					InscribirMultiplesUsuariosEntidadController.this
						.getModel().getColectivosFromCurso(cursoId)
						.stream().collect(new g41.si2022.util.collector.HalfwayListCollector<ColectivoDTO, String> () {

							@Override
							public BiConsumer<List<String>, ColectivoDTO> accumulator() {
								return (list, item) -> list.add(item.getNombre());
							}
							
						})
					)
				);
			}
		});
		
		@SuppressWarnings("unchecked")
		g41.si2022.ui.components.table.editors.JComboBoxEditor<String> cellEditor = 
			((g41.si2022.ui.components.table.editors.JComboBoxEditor<String>) 
			InscribirMultiplesUsuariosEntidadController.this.getView().getTablaInscritos()
				.getColumnModel().getColumn(4).getCellEditor());
		this.getView().getTablaInscritos().addRowAppendedListener(e -> { 
			InscribirMultiplesUsuariosEntidadController.this.getView().getBtnInscribir().setEnabled(true);
			InscribirMultiplesUsuariosEntidadController.this
				.getView().getComboBoxEditors().add(cellEditor);
			cellEditor.setData(InscribirMultiplesUsuariosEntidadController.this
				.getModel().getColectivosFromCurso(cursoId)
				.stream().collect(new g41.si2022.util.collector.HalfwayListCollector<ColectivoDTO, String> () {
					@Override
					public BiConsumer<List<String>, ColectivoDTO> accumulator() {
						return (lista, item) -> lista.add(item.getNombre());
					}
				}));
		});
		
		this.getView().getBtnInscribir().addActionListener(e -> SwingUtil.exceptionWrapper(() -> manageMain()));
	}

	/**
	 * This method handles the inserting of the group, alumnos and inscripciones.
	 */
	public void manageMain() {
		if (cursoId == null) return;

		String email = "";
		GrupoDTO grupo;
		switch (this.getView().getRadioSignin().isSelected() ? SIGN_IN : SIGN_UP) {
			case SIGN_IN:
				email = getView().getTxtEmailLogin().getText();
				break;
			case SIGN_UP:
				email = getView().getTxtEmail().getText();
				getModel().insertGrupo(
					getView().getTxtNombre().getText(),
					email,
					getView().getTxtTelefono().getText()
				);
				break;
			default:
				throw new ApplicationException("Invalid radio button value");
		}

		grupo = getModel().getGrupoFromEmail(email).get(0);

		getModel().insertInscripciones(
			getView().getMain().getToday().toString(),
			cursoId,
			gatherAllAlumnos(),
			grupo.getId()
		);

		getListaCursos();
		Util.sendEmail(email, "COIIPA: Inscripción realizada", "Su inscripción al curso " + SwingUtil.getSelectedKey(this.getView().getTablaCursos()) + " ha sido realizada con éxito.");
		Dialog.show("Inscripción realizada con éxito");
	}

	/**
	 * This method will return the list of Alumnos that are listed in the JTable.<br>
	 * In the context of the HU InscribirMultiplesUsuarios, the Alumnos have to:<br>
	 * <ol>
	 * <li> Be inserted in the DB (table alumno) if they are not already there. Use email as PK.
	 * <li> Create an entry in the table inscripcion that relates this alumno with the curso if the alumno has not joined yet.
	 * </ol>
	 *
	 * @return List of alumnos that are listed in the table
	 */
	public List<AlumnoDTO> gatherAllAlumnos () {
		java.util.function.BiFunction<Map<String, Object>, Integer, String> tableGetter = 
				(map, columnIndex) -> map.get(InscribirMultiplesUsuariosEntidadController.this.getView()
				.getTablaInscritos().getColumnNames()[columnIndex]) == null
				? ""
				: map.get(InscribirMultiplesUsuariosEntidadController.this.getView().getTablaInscritos().getColumnNames()[columnIndex]).toString();

		return this.getView().getTablaInscritos().getData().stream().collect(
				new g41.si2022.util.collector.HalfwayListCollector<Map<String, Object>, AlumnoDTO>() {
					@Override
					public BiConsumer<List<AlumnoDTO>, Map<String, Object>> accumulator() {
						return (list, row) -> {
							AlumnoDTO alumno = new AlumnoDTO();
							alumno.setNombre(tableGetter.apply(row, 0));
							alumno.setApellidos(tableGetter.apply(row, 1));
							alumno.setEmail(tableGetter.apply(row, 2));
							alumno.setTelefono(tableGetter.apply(row, 3));
							alumno.setNombreColectivo(tableGetter.apply(row, 4));
							list.add(alumno);
						};
					}
				});
	}

	public void updateCursoValue() {
		cursoId = null;
		for (CursoDTO curso : cursos) {
			if (curso.getNombre().equals(SwingUtil.getSelectedKey(this.getView().getTablaCursos()))) {
				if (curso.getPlazas_libres().equals("0")) {
					Dialog.showError("No quedan plazas libres para este curso");
					return;
				}
				cursoId = curso.getId();
				return;
			}
		}
	}

	public void getListaCursos() {
		cursos = getModel().getListaCursos(getView().getMain().getToday().toString());
		TableModel tableModel = SwingUtil.getTableModelFromPojos(cursos, new String[] { "nombre", "plazas_libres", "start_inscr", "end_inscr" },
			new String[] { "Nombre", "Plazas libres", "Fecha ini. inscr.", "Fecha fin inscr." }, null);
		getView().getTablaCursos().setModel(tableModel);
		SwingUtil.autoAdjustColumns(getView().getTablaCursos());
	}
}
