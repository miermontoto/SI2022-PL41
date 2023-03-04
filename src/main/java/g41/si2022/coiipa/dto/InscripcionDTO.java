package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import g41.si2022.util.InscripcionState;
import lombok.Data;

@Getter @Setter @Data
public class InscripcionDTO {

    private String
    	inscripcion_id,
    	inscripcion_fecha,
    	inscripcion_estado,
    	inscripcion_curso_id,
    	inscripcion_alumno_id,
    	alumno_nombre,
    	curso_coste,
    	curso_nombre;
    private InscripcionState estado;

    public InscripcionDTO() {}

    public static String getSqlQuery() { return "select * from inscripcion order by id asc"; }
}
