package g41.si2022.coiipa.gestionar_cursos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.TableModel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.coiipa.dto.ProfesorDTO;
import g41.si2022.util.CursoState;
import g41.si2022.util.StateUtilities;
import g41.si2022.ui.SwingUtil;
import java.util.stream.Collectors;


public class GestionarCursosController {

    private GestionarCursosModel model;
    private GestionarCursosView view;
    private List<CursoDTO> cursos;
    private List<CursoDTO> cursosActivos;
    
    private java.util.function.Supplier<List<CursoDTO>> sup = () -> {
        // Get the selected item from the the filter
		CursoState selectedItem = (CursoState) this.view.getCbFiltro().getSelectedItem();
		List<CursoDTO> output = new ArrayList<CursoDTO>(), // Will contain the entries that meet the filter
			aux; // Is used as auxiliary list to avoid concurrent modifications

		// FIRST : WE FILTER THE STATES
		if (selectedItem.equals(CursoState.CUALQUIERA)) { // If the CB has chosen ANY, the output array will contain all entries
			output.addAll(this.cursosActivos);
		} else { // If the CB has chosen something else, the entries are filtered
            output = this.cursosActivos.stream()
			    .filter(x -> selectedItem.equals(x.getEstado()))
			    .collect(Collectors.toList());
		}
		aux = new ArrayList<CursoDTO>(output); // DO NOT REMOVE -> Concurrent Modifications will happen if removed

		// SECOND : WE FILTER THE DATES
		java.time.LocalDate
		    start = this.view.getStartDate().getDate(), // All entries' end date must be higher than the filter's start date
		    end = this.view.getEndDate().getDate(); // All entries' start date must be lower than the filter's end date
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

		return output; // We return the filtered array
    };

    public GestionarCursosController(GestionarCursosModel model, GestionarCursosView view) {
        this.model = model;
        this.view = view;
        initView();
    }

    public void initView() {
        // Mostrar cursos activos en JTable
        getCursosActivos();
        loadComboBox();
        showListaCursos();
        loadDates();

        // Mostrar más detalles para cada curso seleccionado
        view.getTablaCursos().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent evt) {
                SwingUtil.exceptionWrapper(() -> showDetallesCurso());
            }
        });

        // Filtrar los cursos en función de su fecha ó estado
        view.getCbFiltro().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent evt)
            {
                // SwingUtil.exceptionWrapper(() -> filtrarCursos());
                SwingUtil.exceptionWrapper(() -> {
                    // GestionarCursosController.this.filtrarCursos();
                    GestionarCursosController.this.showListaCursos();
                });
            }
        });
    }

    private void getCursosActivos()
    {
        cursosActivos = new LinkedList<>();
        cursos = model.getListaCursos();
        for (CursoDTO curso : cursos)
        {
            // Añadir estado al curso
            curso.setEstado(StateUtilities.getCursoState(curso, this.view.getMain().getToday()));
            CursoState estadoCurso = curso.getEstado();

            if (estadoCurso != CursoState.FINALIZADO && estadoCurso != CursoState.CERRADO)
            {
                // Añadir número de plazas libres al curso activo (para mostrarlas)
                // Sumatorio de plazas totales menos las inscripciones NO canceladas
                curso.setPlazas_libres(String.valueOf((Integer.valueOf(curso.getPlazas()) - Integer.valueOf(model.getNumIscripciones(curso.getId())))));

                // Añadir curso a la lista de cursos activos
                cursosActivos.add(curso);
            }
        }
    }

    private void showListaCursos()
    {
        TableModel tableModel = SwingUtil.getTableModelFromPojos(
            this.sup.get(),
            new String[] { "nombre", "estado", "start_inscr", "end_inscr", "plazas", "plazas_libres", "start" },
            new String[] { "Nombre", "Estado", "Inicio de inscripciones", "Fin de inscripciones", "Plazas", "Plazas vacantes" , "Inicio del curso" },
            null
        );
        view.getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(view.getTablaCursos());
    }

    private void showDetallesCurso()
    {
        List<ProfesorDTO> docentes;
        
        for (CursoDTO curso : cursosActivos)
        {
            if (curso.getNombre().equals(SwingUtil.getSelectedKey(view.getTablaCursos())))
            {
                // Mostrar la descripción del curso
                view.getTxtDescripcion().setText(" " + model.getDescripcionCurso(curso.getId()));
                // Obtener docente/s que imparten el curso
                docentes = model.getDocentesCurso(curso.getId());
                view.getTxtProfesor().setText("");
               
                for (ProfesorDTO docente: docentes)
                {
                    String nombre = docente.getNombre();
                    String apellidos = docente.getApellidos();
                    view.getTxtProfesor().setText(view.getTxtProfesor().getText() + " " + nombre + " " + apellidos);
                }
                // Mostrar lugar en el que se imparte el curso
                view.getTxtLugar().setText(" " + model.getLugarCurso(curso.getId()));
            }
        }
    }

    private void loadComboBox () {
		java.util.stream.Stream.of(CursoState.values())
        .filter(x -> !x.equals(CursoState.CERRADO) && !x.equals(CursoState.FINALIZADO))
        .forEach(e -> this.view.getCbFiltro().addItem(e));
		this.view.getCbFiltro().addItemListener((e) -> {
			if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				this.showListaCursos();
			}
		});
	}

    private void loadDates () {
        g41.si2022.util.BetterDatePicker start = this.view.getStartDate(),
        end =  this.view.getEndDate();
       start.addDateChangeListener((e) -> {
			if (start.getDate() != null && end.getDate() != null && start.compareTo(end) >= 0) {
				end.setDate(start.getDate().plusDays(1));
			}
			this.showListaCursos();
		});
        end.addDateChangeListener((e) -> {
			if (end.getDate() != null && start.getDate() != null && start.compareTo(end) >= 0) {
				start.setDate(end.getDate().plusDays(-1));
			}
			this.showListaCursos();
		});
    }
}
