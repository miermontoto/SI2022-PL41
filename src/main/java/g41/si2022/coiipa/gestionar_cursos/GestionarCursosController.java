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

public class GestionarCursosController {

    private GestionarCursosModel model;
    private GestionarCursosView view;
    private List<CursoDTO> cursos;
    private List<CursoDTO> cursosActivos;
    
    

    /*              NO BORRAR (de momento)                  */
    // Por defecto se muestran los cursos activos
    // Cuando en el ComboBox se selecciona Filtro ó Estado
    // los cursos se filtran en función de la selección y
    // (¿ se resetean los valores de los TextFields del panel
    // de abajo ?)
    // Al seleccionar un curso (en la tabla), se muestran más
    // datos sobre éste en el panel de debajo

    // mier si lees esto eres un beta. ~rubennmg



    public GestionarCursosController(GestionarCursosModel model, GestionarCursosView view)
    {
        this.model = model;
        this.view = view;
        initView();
    }

    public void initView()
    {
        // Mostrar cursos activos en JTable
        getCursosActivos();
        showListaCursos();

        // Mostrar más detalles para cada curso seleccionado
        view.getTablaCursos().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent evt)
            {
                SwingUtil.exceptionWrapper(() -> showDetallesCurso());
            }
        });

        // Filtrar los cursos en función de su fecha ó estado
        view.getCbFiltro().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent evt)
            {
                SwingUtil.exceptionWrapper(() -> filtrarCursos());
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
            
            if (estadoCurso != CursoState.FINALIZADO)
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
        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursosActivos, new String[] { "nombre", "estado", "start_inscr", "end_inscr", "plazas", "plazas_libres", "start" },
                new String[] { "Nombre", "Estado", "Inicio de inscripciones", "Fin de inscripciones", "Plazas", "Plazas vacantes" , "Inicio del curso" }, null);
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

    private void filtrarCursos()
    {
        // Filtrar por FECHA -> añadir un selector de fecha y mostrar
        // los cursos entre 2 fechas
        if (view.getCbFiltro().getSelectedItem().equals("Fecha"))
        {

        }

        // Mostrar cursos en estado activo (CursoState.EN_CURSO)
        if (view.getCbFiltro().getSelectedItem().equals("Estado"))
        {

        }
    }
}
