package g41.si2022.coiipa.modificar_curso;

import java.time.LocalDate;
import java.util.List;

import g41.si2022.dto.ColectivoDTO;
import g41.si2022.dto.CosteDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.DocenciaDTO;
import g41.si2022.dto.ProfesorDTO;
import g41.si2022.dto.SesionDTO;
import g41.si2022.mvc.Model;
import g41.si2022.ui.util.Dialog;
import g41.si2022.util.state.CursoState;

public class ModificarCursosModel extends Model {

    public List<CursoDTO> getListaCursos(LocalDate today) {
        String sql = "select c.*, count(i.id) as ocupadas"
            + " from curso as c"
            + " inner join inscripcion as i on i.curso_id = c.id"
            + " where i.cancelada = 0 group by c.id ";
        List<CursoDTO> lista = getDatabase().executeQueryPojo(CursoDTO.class, sql);
        lista.forEach(c -> c.updateState(today)); // Actualizar todos los estados de los cursos.
        return lista;
    }

    public List<SesionDTO> getSesionesFromCurso(String cursoId) {
        String sql = "select * from sesion where curso_id = ?";
        return getDatabase().executeQueryPojo(SesionDTO.class, sql, cursoId);
    }

    public List<ProfesorDTO> getListaProfesores(String cursoId) {
        String sql = "select dce.id, dce.dni, dce.nombre, dce.apellidos,"
        + " case when dca.remuneracion is null then 0 else dca.remuneracion end as remuneracion"
        + " from docente as dce"
        + " left join docencia as dca on dca.docente_id = dce.id and dca.curso_id = ?";
        return getDatabase().executeQueryPojo(ProfesorDTO.class, sql, cursoId);
    }

    public List<ColectivoDTO> getColectivosFromCurso(String cursoId) {
        String sql = "select col.id, col.nombre, cos.coste from colectivo as col"
        + " inner join coste as cos on cos.colectivo_id = col.id"
        + " where cos.curso_id = ?";
        return getDatabase().executeQueryPojo(ColectivoDTO.class, sql, cursoId);
    }

    public boolean updateSesiones(CursoDTO curso, List<SesionDTO> sesiones, LocalDate today) {
        String idCurso = curso.getId();

        if(curso.getState() == CursoState.FINALIZADO || curso.getState() == CursoState.CANCELADO ||
            curso.getState() == CursoState.CERRADO) {
            Dialog.showError("No se puede modificar un curso finalizado o cerrado.");
            return false;
        }

        // Validar nuevo listado de sesiones
        if(curso.getState() == CursoState.EN_CURSO) {
                List<SesionDTO> oldSesiones = getSesionesFromCurso(idCurso);
            if (oldSesiones.stream().filter(s -> LocalDate.parse(s.getFecha()).isBefore(today)).anyMatch(
                s1 -> sesiones.stream().noneMatch(s2 -> s2.equals(s1)))) {
                Dialog.showError("No se puede eliminar una sesión ya realizada.");
                return false;
            }
        }

        // Eliminar todas las sesiones del curso
        String sql = "delete from sesion where curso_id = ?";
        getDatabase().executeUpdate(sql, idCurso);

        // Insertar las nuevas sesiones
        sql = "insert into sesion (curso_id, fecha, hora_ini, hora_fin, loc) values (?, ?, ?, ?, ?)";
        for (SesionDTO sesion : sesiones) {
            getDatabase().executeUpdate(sql, idCurso, sesion.getFecha(), sesion.getHoraIni(), sesion.getHoraFin(), sesion.getLoc());
        }
        return true;
    }

    public boolean updateCurso(CursoDTO curso, String nombre, String descripcion, String plazas) {
        String idCurso = curso.getId();
        int plazasInt = Integer.parseInt(plazas);

        if(plazasInt < 1) {
            Dialog.showError("Número de plazas inválido.\n(Debe ser mayor que 0)");
            return false;
        }

        if(plazasInt < Integer.parseInt(curso.getOcupadas())) {
            Dialog.showError("Número de plazas inválido.\n(Debe tener más plazas que alumnos ya inscritos)");
            return false;
        }

        switch (curso.getState()) {
            case EN_CURSO:
                if (!curso.getNombre().equals(nombre) || !curso.getPlazas().equals(plazas)) {
                    Dialog.showError("No se puede modificar el nombre ni el número de plazas de un curso activo.");
                    return false;
                }
                break;
            case FINALIZADO:
                Dialog.showError("No se puede modificar un curso finalizado.");
                return false;
            default:
                break;
        }

        String sql = "update curso set nombre = ?, descripcion = ?, plazas = ? where id = ?";
        getDatabase().executeUpdate(sql, nombre, descripcion, plazas, idCurso);
        return true;
    }

    public void updateDocencias(String idCurso, List<DocenciaDTO> docencias) {
        // Eliminar todas las docencias del curso
        String sql = "delete from docencia where curso_id = ?";
        getDatabase().executeUpdate(sql, idCurso);

        // Insertar las nuevas docencias
        sql = "insert into docencia (curso_id, docente_id, remuneracion) values (?, ?, ?)";
        for (DocenciaDTO docencia : docencias) {
            getDatabase().executeUpdate(sql, idCurso, docencia.getDocente_id(), docencia.getRemuneracion());
        }
    }

    public boolean updateCostes(CursoDTO curso, List<CosteDTO> costes) {
        String idCurso = curso.getId();

        // Comprobar si ya existen inscripciones o si el curso está cerrado.
        if (!curso.getOcupadas().equals("0") || curso.getState() == CursoState.CERRADO) {
            Dialog.showError("No se puede modificar los costes de un curso con inscripciones o cerrado.");
            return false;
        }

        // Eliminar todos los costes del curso
        String sql = "delete from coste where curso_id = ?";
        getDatabase().executeUpdate(sql, idCurso);

        // Insertar los nuevos costes
        sql = "insert into coste (curso_id, colectivo_id, coste) values (?, ?, ?)";
        for (CosteDTO coste : costes) {
            getDatabase().executeUpdate(sql, idCurso, coste.getColectivo_id(), coste.getCoste());
        }

        return true;
    }

    public CursoDTO getCurso(String idCurso) {
        String sql = "select c.*, count(i.id) as ocupadas"
            + " from curso as c"
            + " inner join inscripcion as i on i.curso_id = c.id"
            + " where i.cancelada = 0 and c.id = ? group by c.id ";
        CursoDTO ret = getDatabase().executeQueryPojo(CursoDTO.class, sql, idCurso).get(0);
        ret.updateState(LocalDate.now());
        return ret;
    }

}
