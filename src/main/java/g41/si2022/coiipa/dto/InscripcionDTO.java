package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import g41.si2022.util.InscripcionState;
import lombok.Data;

@Getter @Setter @Data
public class InscripcionDTO {

    private String
    	id,
    	fecha,
    	pagado,
    	curso_id,
    	alumno_id,
    	inscripcion_id,
    	alumno_nombre,
		alumno_apellidos, 
    	curso_coste,
    	curso_nombre;
    private InscripcionState estado;

    public InscripcionDTO() {}
}
