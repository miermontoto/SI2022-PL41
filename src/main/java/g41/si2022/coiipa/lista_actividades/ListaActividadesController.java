package g41.si2022.coiipa.lista_actividades;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import g41.si2022.util.state.CursoState;
import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.ProfesorDTO;
import g41.si2022.ui.SwingUtil;
import java.util.stream.Collectors;


public class ListaActividadesController extends g41.si2022.mvc.Controller<ListaActividadesView, ListaActividadesModel> {

    private List<CursoDTO> cursos;
    private List<CursoDTO> cursosActivos;

    private java.util.function.Supplier<List<CursoDTO>> sup = () -> {
        // Get the selected item from the the filter
		CursoState selectedItem = (CursoState) this.getView().getCbFiltro().getSelectedItem();
		List<CursoDTO> output = new ArrayList<CursoDTO>(), // Will contain the entries that meet the filter
			aux; // Is used as auxiliary list to avoid concurrent modifications

		// FIRST : WE FILTER THE STATES
		if (selectedItem == null || selectedItem.equals(CursoState.CUALQUIERA)) { // If the CB has chosen ANY, the output array will contain all entries
			output.addAll(this.cursosActivos);
		} else { // If the CB has chosen something else, the entries are filtered
            output = this.cursosActivos.stream()
			    .filter(x -> selectedItem.equals(x.getEstado()))
			    .collect(Collectors.toList());
		}
		aux = new ArrayList<CursoDTO>(output); // DO NOT REMOVE -> Concurrent Modifications will happen if removed

		// SECOND : WE FILTER THE DATES
		java.time.LocalDate
		    start = this.getView().getStartDate().getDate(), // All entries' end date must be higher than the filter's start date
		    end = this.getView().getEndDate().getDate(); // All entries' start date must be lower than the filter's end date
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

    public ListaActividadesController(ListaActividadesModel model, ListaActividadesView view) {
    	super(view, model);
    }

    @Override
    public void initNonVolatileData() {
    	this.loadComboBox();
    	this.loadDates();
    	// Mostrar más detalles para cada curso seleccionado
        this.getView().getTablaCursos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent evt) {
                SwingUtil.exceptionWrapper(() -> showDetallesCurso());
            }
        });
        // Filtrar los cursos en función de su fecha ó estado
        this.getView().getCbFiltro().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent evt)
            {
                // SwingUtil.exceptionWrapper(() -> filtrarCursos());
                SwingUtil.exceptionWrapper(() -> {
                    ListaActividadesController.this.showListaCursos();
                });
            }
        });
    }

    @Override
    public void initVolatileData() {
    	this.getCursosActivos();
    	this.showListaCursos();
    }

    private void getCursosActivos() {
        cursosActivos = new LinkedList<>();
        cursos = this.getModel().getListaCursos();
        for (CursoDTO curso : cursos) {
            CursoState estadoCurso = curso.updateEstado(getView().getMain().getToday());

            if (estadoCurso != CursoState.FINALIZADO && estadoCurso != CursoState.CERRADO)
            {
                // Añadir número de plazas libres al curso activo (para mostrarlas)
                // Sumatorio de plazas totales menos las inscripciones NO canceladas
                curso.setPlazas_libres(String.valueOf((Integer.valueOf(curso.getPlazas()) - Integer.valueOf(this.getModel().getNumIscripciones(curso.getId())))));
                cursosActivos.add(curso); // Añadir curso a la lista de cursos activos
            }
        }
    }

    private void showListaCursos() {
        JTable table = this.getView().getTablaCursos();
        table.setModel(SwingUtil.getTableModelFromPojos(
            this.sup.get(),
            new String[] { "nombre", "estado", "start_inscr", "end_inscr", "plazas", "plazas_libres", "start" },
            new String[] { "Nombre", "Estado", "Inicio de inscripciones", "Fin de inscripciones", "Plazas", "Plazas vacantes" , "Inicio del curso" },
            null
        ));
        SwingUtil.autoAdjustColumns(table);
    }

    private void showDetallesCurso()
    {
        List<ProfesorDTO> docentes;

        for (CursoDTO curso : cursosActivos) {
            if (curso.getNombre().equals(SwingUtil.getSelectedKey(this.getView().getTablaCursos()))) {
                // Mostrar la descripción del curso
                this.getView().getTxtDescripcion().setText(" " + this.getModel().getDescripcionCurso(curso.getId()));
                // Obtener docente/s que imparten el curso
                docentes = this.getModel().getDocentesCurso(curso.getId());
                this.getView().getTxtProfesor().setText("");

                boolean first = true;
                for (ProfesorDTO docente : docentes) {
                    if(!first) {
                        this.getView().getTxtProfesor().setText(this.getView().getTxtProfesor().getText() + ", ");
                    } else first = false;
                    this.getView().getTxtProfesor().setText(this.getView().getTxtProfesor().getText()
                     + docente.getNombre() + " " + docente.getApellidos());
                }

                // Mostrar lugar en el que se imparte el curso
                this.getView().getTxtLugar().setText(" " + this.getModel().getLugarCurso(curso.getId()));
            }
        }
    }

    private void loadComboBox () {
		java.util.stream.Stream.of(CursoState.values())
        .filter(x -> !x.equals(CursoState.CERRADO) && !x.equals(CursoState.FINALIZADO))
        .forEach(e -> this.getView().getCbFiltro().addItem(e));
		this.getView().getCbFiltro().addItemListener((e) -> {
			if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				this.showListaCursos();
			}
		});
	}

    private void loadDates () {
        g41.si2022.util.BetterDatePicker start = this.getView().getStartDate(),
        end =  this.getView().getEndDate();
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
