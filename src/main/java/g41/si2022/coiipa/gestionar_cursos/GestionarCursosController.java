package g41.si2022.coiipa.gestionar_cursos;

import java.time.LocalDate;
import java.util.List;

import javax.swing.table.TableModel;


import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.util.CursoState;
import g41.si2022.util.StateUtilities;
import g41.si2022.util.SwingUtil;

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
        getCursosActivos(); 
        // Mostrar más detalles para cada curso seleccionado
        view.getTablaCursos().addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseReleased(MouseEvent evt)
            {
                SwingUtil.exceptionWrapper(() -> mostrarDetallesCurso());
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
        cursos = model.getListaCursos();
        for (CursoDTO curso : cursos)
        {
            LocalDate today = LocalDate.now();
            CursoState estadoCurso = StateUtilities.getCursoState(curso, today);

            if (estadoCurso == CursoState.EN_CURSO)
                cursosActivos.add(curso);
        }
        // ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ 
        // ¿Cómo añadir estado y plazas vacantes?
        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursosActivos, new String[] { "nombre", "start_inscr", "end_inscr", "plazas", "start" },
                new String[] { "Nombre", "Inicio de inscripciones", "Fin de inscripciones", "Plazas (no vacantes)", "Inicio del curso" }, null);
        view.getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(view.getTablaCursos());
    }

    private void mostrarDetallesCurso()
    {
        
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
