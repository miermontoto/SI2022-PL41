package g41.si2022.coiipa.inscribir_multiples_usuarios;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.swing.table.TableModel;

import g41.si2022.dto.AlumnoDTO;
import g41.si2022.dto.ColectivoDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.util.Dialog;

public class InscribirMultiplesUsuariosController extends g41.si2022.mvc.Controller<InscribirMultiplesUsuariosView, InscribirMultiplesUsuariosModel> {

	private List<CursoDTO> cursos;
	private String cursoId;

	public InscribirMultiplesUsuariosController(InscribirMultiplesUsuariosModel m, InscribirMultiplesUsuariosView v) {
		super(v, m);
		this.cursoId = null;
	}

	@Override
	public void initVolatileData() {
		this.getListaCursos();
		this.loadColectivosListeners();
	}

	@Override
	public void initNonVolatileData() {
		this.getView().getBtnInscribir().addActionListener(e -> this.insertInscripciones());
	}
	
	@SuppressWarnings("unchecked")
	public void loadColectivosListeners () {
		g41.si2022.ui.components.table.editors.JComboBoxEditor<String> cellEditor = 
			((g41.si2022.ui.components.table.editors.JComboBoxEditor<String>) InscribirMultiplesUsuariosController.this.getView().getTablaInscritos().getColumnModel().getColumn(4).getCellEditor());
		this.getView().getTablaInscritos().addRowAppendedListener(e -> { 
			InscribirMultiplesUsuariosController.this.getView().getComboBoxEditors().add(cellEditor);
			cellEditor.setData(InscribirMultiplesUsuariosController.this.getModel().getListaColectivos()
				.stream().collect(new g41.si2022.util.collector.HalfwayListCollector<ColectivoDTO, String> () {
					@Override
					public BiConsumer<List<String>, ColectivoDTO> accumulator() {
						return (lista, item) -> lista.add(item.getNombre());
					}
				}));
		});
	}
	
	/**
	 * insertInscripciones. Inserts all the inscripciones that are listed in the table.<br>
	 * This will also add as many alumnos as needed in the database 
	 */
	private void insertInscripciones () {
		this.getModel().insertInscripciones(
			this.gatherAllAlumnos(),
			this.getView().getMain().getToday().toString(),
			cursoId
		);
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
		return this.getView().getTablaInscritos().getData().stream().collect(
				new g41.si2022.util.collector.HalfwayListCollector<Map<String, String>, AlumnoDTO>() {
					@Override
					public BiConsumer<List<AlumnoDTO>, Map<String, String>> accumulator() {
						return (list, row) -> {
							AlumnoDTO alumno = new AlumnoDTO();
							alumno.setNombre(row.get(InscribirMultiplesUsuariosController.this.getView().getTablaInscritos().getColumnNames()[0]));
							alumno.setApellidos(row.get(InscribirMultiplesUsuariosController.this.getView().getTablaInscritos().getColumnNames()[1]));
							alumno.setEmail(row.get(InscribirMultiplesUsuariosController.this.getView().getTablaInscritos().getColumnNames()[2]));
							alumno.setTelefono(row.get(InscribirMultiplesUsuariosController.this.getView().getTablaInscritos().getColumnNames()[3]));
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
