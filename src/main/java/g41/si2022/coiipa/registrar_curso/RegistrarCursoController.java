package g41.si2022.coiipa.registrar_curso;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import g41.si2022.coiipa.dto.ProfesorDTO;
import g41.si2022.util.SwingUtil;

public class RegistrarCursoController {
	private RegistrarCursoModel model;
	private RegistrarCursoView view;

	private List<ProfesorDTO> profesores;
	private Map<String, ProfesorDTO> profesoresMap;
	
	public RegistrarCursoController (RegistrarCursoModel m, RegistrarCursoView v) {
		this.model = m;
		this.view = v;
		this.profesoresMap = new HashMap<String, ProfesorDTO> ();
		this.initView();
	}

	public void initView () {
		// Load the profesores list
		SwingUtil.exceptionWrapper(() -> this.getListaProfesores());
		// Load the insert curso listener
		this.view.getSubmitButton().addActionListener((e) -> SwingUtil.exceptionWrapper(() -> this.insertCurso()));
		this.loadDateListeners();
	}
	
	private void loadDateListeners () {
		g41.si2022.util.BetterDatePicker
			inscripcionIni = this.view.getInscripcionIni(),
			inscripcionFin = this.view.getInscripcionFin(),
			cursoIni = this.view.getCursoIni(),
			cursoFin = this.view.getCursoFin();
		// Load the Inscripcion Ini DatePicker listener (set Fin to next day)
		inscripcionIni.addDateChangeListener((e) -> {
			if (inscripcionFin.getDate() == null) {
				inscripcionFin.setDate(e.getNewDate().plusDays(1));
			}
		});
		// Load the Curso Ini DatePicker listener (set Fin to next day)
		cursoIni.addDateChangeListener((e) -> {
			if (cursoFin.getDate() == null) {
				cursoFin.setDate(e.getNewDate().plusDays(1));
			}
		});
		// Load the Inscripcion Fin DatePicker listener (set Fin to next day if lower than Ini)
		inscripcionFin.addDateChangeListener((e) -> {
			if (inscripcionIni.getDate() != null && inscripcionIni.compareTo(inscripcionFin) >= 0) {
				inscripcionFin.setDate(inscripcionIni.getDate().plusDays(1));
			}
			if (cursoFin.getDate() != null && inscripcionFin.compareTo(cursoFin) >= 0) {
				cursoFin.setDate(inscripcionFin.getDate());
			}
			if (cursoIni.getDate() == null) {
				cursoIni.setDate(inscripcionFin.getDate().plusDays(1));
			}
		});
		// Load the Curso Fin DatePicker listener (set Fin to next day if lower than Ini)
		cursoFin.addDateChangeListener((e) -> {
			if (cursoIni.getDate() != null && cursoIni.compareTo(cursoFin) >= 0) {
				cursoFin.setDate(cursoIni.getDate().plusDays(1));
			}
			if (inscripcionFin.getDate() != null && inscripcionFin.compareTo(cursoFin) >= 0) {
				inscripcionFin.setDate(cursoFin.getDate());
			}
		});
	}

	public void getListaProfesores() {
		this.model.getListaProfesores().stream().forEach((x) -> this.profesoresMap.put(x.getDni(), x));
		view.getTablaProfesores().setModel(
				SwingUtil.getTableModelFromPojos(
						this.profesores = this.model.getListaProfesores(),
						new String[] { "dni", "nombre", "apellidos", "email", "direccion", "remuneracion" },
						new String[] { "DNI", "Nombre", "Apellidos", "email", "Dirección", "Remuneración" },
						new java.util.HashMap<Integer, java.util.regex.Pattern> () {
							private static final long serialVersionUID = 1L;
							{
								put(5, java.util.regex.Pattern.compile("\\d+(\\.\\d+)?"));
							}}
						)
				);
		SwingUtil.autoAdjustColumns(this.view.getTablaProfesores());
	}

	public void insertCurso () {
		this.model.insertCurso(
				this.view.getNombreCurso(), this.view.getObjetivosDescripcion(),
				this.view.getInscripcionIniDate(), this.view.getInscripcionFinDate(), this.view.getCursoIniDate(), this.view.getCursoFinDate(),
				this.view.getPlazas(), this.getProfesor()
				.orElseThrow(() -> new g41.si2022.util.UnexpectedException("No se ha seleccionado a ningún docente."))
				.getId());
	}

	private Optional<ProfesorDTO> getProfesor () {
		javax.swing.table.TableModel model = this.view.getTablaProfesores().getModel();
		for (int i = 0 ; i < model.getRowCount() ; i++) {
			if (model.getValueAt(i, model.getColumnCount()-1) != null) {
				return Optional.of(this.profesoresMap.get(model.getValueAt(i, 0).toString()));
			}
		}
		return Optional.empty();
	}
}
