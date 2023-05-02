package g41.si2022.coiipa.modificar_curso;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import g41.si2022.dto.ColectivoDTO;
import g41.si2022.dto.CosteDTO;
import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.DocenciaDTO;
import g41.si2022.dto.ProfesorDTO;
import g41.si2022.dto.SesionDTO;
import g41.si2022.mvc.Model;

public class ModificarCursosModel extends Model {

    public List<CursoDTO> getListaCursos(LocalDate today) {
        String sql = "select * from curso";
        List<CursoDTO> lista = getDatabase().executeQueryPojo(CursoDTO.class, sql);
        lista.forEach(c -> c.updateEstado(today)); // Actualizar todos los estados de los cursos.
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

    public void updateSesiones(String idCurso, List<SesionDTO> sesiones) {
        // Eliminar todas las sesiones del curso
        String sql = "delete from sesion where curso_id = ?";
        getDatabase().executeUpdate(sql, idCurso);

        // Insertar las nuevas sesiones
        sql = "insert into sesion (curso_id, fecha, hora_ini, hora_fin, loc) values (?, ?, ?, ?, ?)";
        for (SesionDTO sesion : sesiones) {
            getDatabase().executeUpdate(sql, idCurso, sesion.getFecha(), sesion.getHoraIni(), sesion.getHoraFin(), sesion.getLoc());
        }
    }

    public void updateCurso(String infoFromCurso, JTextField txtNombre, JTextArea txtDescripcion,
            JTextField txtPlazas) {
        String sql = "update curso set nombre = ?, descripcion = ?, plazas = ? where id = ?";
        getDatabase().executeUpdate(sql, txtNombre.getText(), txtDescripcion.getText(), txtPlazas.getText(), infoFromCurso);
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

    public boolean checkIfCursoHasInscripciones(String idCurso) {
        String sql = "select count(*) from inscripcion where curso_id = ?";
        return Integer.parseInt(getDatabase().executeQuerySingle(sql, idCurso).toString()) > 0;
    }

    public void updateCostes(String idCurso, List<CosteDTO> costes) {
        // Eliminar todos los costes del curso
        String sql = "delete from coste where curso_id = ?";
        getDatabase().executeUpdate(sql, idCurso);

        // Insertar los nuevos costes
        sql = "insert into coste (curso_id, colectivo_id, coste) values (?, ?, ?)";
        for (CosteDTO coste : costes) {
            getDatabase().executeUpdate(sql, idCurso, coste.getColectivo_id(), coste.getCoste());
        }
    }

    public boolean validatePlazas(String idCurso, String plazas) {
        int n = Integer.parseInt(plazas);
        if(n < 0) return false;

        String sql = "select count(*) from inscripcion where curso_id = ?";
        int inscripciones = Integer.parseInt(getDatabase().executeQuerySingle(sql, idCurso).toString());
        return n >= inscripciones;
    }

}
