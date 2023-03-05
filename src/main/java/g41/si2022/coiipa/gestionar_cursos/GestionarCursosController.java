package g41.si2022.coiipa.gestionar_cursos;

import java.time.LocalDate;
import java.util.List;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.util.CursoState;
import g41.si2022.util.StateUtilities;

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
    // datos sobre este en el panel de debajo

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
    }

    private void getCursosActivos()
    {
        cursos = model.getListaCursos();
        for (CursoDTO curso : cursos)
        {
            LocalDate today = LocalDate.now();
            CursoState estadoCurso = StateUtilities.getCursoState(curso, today);

            if (estadoCurso == CursoState.EN_CURSO)
            {
                cursosActivos.add(curso);
            }

            
        }
    }
}
