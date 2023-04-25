package g41.si2022.coiipa.lista_actividades;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXComboBox;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.SesionDTO;
import g41.si2022.dto.ProfesorDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.util.state.CursoState;

public class ListaActividadesController extends g41.si2022.mvc.Controller<ListaActividadesView, ListaActividadesModel> {

    private List<CursoDTO> cursos;
    private List<CursoDTO> cursosActivos;

    private Supplier<List<CursoDTO>> supCursos = () -> {
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

    private Supplier<List<SesionDTO>> supSesiones = () -> {
        List<SesionDTO> sesiones = getModel().getSesionesCurso(SwingUtil.getSelectedKey(this.getView().getTablaCursos()));
        try {
            String locSelected = getView().getInfoLocalizaciones().getSelectedItem().toString();
            if (locSelected.equals("------")) return sesiones;
            return sesiones.stream().filter(x -> x.getLoc().equals(locSelected)).collect(Collectors.toList());
        } catch (java.lang.NullPointerException npe) {return sesiones;}
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
                SwingUtil.exceptionWrapper(() -> showListaSesiones());
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

        getView().getInfoLocalizaciones().addActionListener(e -> {
            SwingUtil.exceptionWrapper(() -> {
                ListaActividadesController.this.showListaSesiones();
            });
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

            if (estadoCurso != CursoState.FINALIZADO && estadoCurso != CursoState.CERRADO) {
                // Añadir número de plazas libres al curso activo (para mostrarlas)
                // Sumatorio de plazas totales menos las inscripciones NO canceladas
                curso.setPlazas_libres(String.valueOf((Integer.valueOf(curso.getPlazas()) - Integer.valueOf(this.getModel().getNumInscripciones(curso.getId())))));
                cursosActivos.add(curso); // Añadir curso a la lista de cursos activos
            }
        }
    }

    private void showListaCursos() {
        JTable table = this.getView().getTablaCursos();
        table.setModel(SwingUtil.getTableModelFromPojos(
            this.supCursos.get(),
            new String[] { "id", "nombre", "estado", "start_inscr", "end_inscr", "plazas", "plazas_libres", "start" },
            new String[] { "", "Nombre", "Estado", "Inicio de inscripciones", "Fin de inscripciones", "Plazas", "Plazas vacantes" , "Inicio del curso" },
            null
        ));
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));
        SwingUtil.autoAdjustColumns(table);
    }

    private void showListaSesiones() {
        JTable table = this.getView().getTableSesiones();
        table.setModel(SwingUtil.getTableModelFromPojos(supSesiones.get(),
            new String[] {"loc", "fecha", "horaIni", "horaFin"},
            new String[] {"Localización (aula)", "Fecha", "Hora de inicio", "Hora de fin"},
            null));


        // Order by date and then by time
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        sorter.setComparator(1, (String o1, String o2) -> {
            LocalDate date1 = LocalDate.parse(o1);
            LocalDate date2 = LocalDate.parse(o2);
            return date1.compareTo(date2);
        });
        sorter.setComparator(2, (String o1, String o2) -> {
            LocalTime time1 = LocalTime.parse(o1);
            LocalTime time2 = LocalTime.parse(o2);
            return time1.compareTo(time2);
        });
        table.setRowSorter(sorter);
    }

    @SuppressWarnings("unchecked") // <- no tiene sentido que se necesite esto!! (raw type en comboboxes)
    private void showDetallesCurso() {
        for (CursoDTO curso : cursosActivos) {
            if (!curso.getId().equals(SwingUtil.getSelectedKey(this.getView().getTablaCursos()))) continue;
            // Mostrar la descripción del curso
            this.getView().getInfoDescripcion().setText(" " + this.getModel().getDescripcionCurso(curso.getId()));

            // Obtener docente/s que imparten el curso
            JXComboBox comboProf = this.getView().getInfoProfesores();
            comboProf.removeAllItems();
            List<ProfesorDTO> docentes = getModel().getDocentesCurso(curso);

            if (docentes.isEmpty()) comboProf.addItem("N/A");
            else for (ProfesorDTO docente : docentes) comboProf.addItem(docente.getNombre() + " " + docente.getApellidos() + " (" + docente.getRemuneracion() + "€)");

            // Mostrar las localizaciones del curso
            JXComboBox comboLocs = this.getView().getInfoLocalizaciones();
            comboLocs.removeAllItems();
            List<SesionDTO> sesiones = getModel().getSesionesCurso(curso);
            ArrayList<String> locs = new ArrayList<>();

            comboLocs.addItem("------");
            if (!sesiones.isEmpty()) for(SesionDTO sesion : sesiones) {
                if(!locs.contains(sesion.getLoc())) {
                    locs.add(sesion.getLoc());
                    comboLocs.addItem(sesion.getLoc());
                }

            }

        }
    }

    private void loadComboBox() {
		java.util.stream.Stream.of(CursoState.values())
        .filter(x -> !x.equals(CursoState.CERRADO) && !x.equals(CursoState.FINALIZADO))
        .forEach(e -> this.getView().getCbFiltro().addItem(e));
		this.getView().getCbFiltro().addItemListener((e) -> {
			if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				this.showListaCursos();
			}
		});
	}

    private void loadDates() {
        g41.si2022.ui.components.BetterDatePicker start = this.getView().getStartDate(),
        end = this.getView().getEndDate();
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
