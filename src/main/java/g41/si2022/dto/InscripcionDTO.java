package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import g41.si2022.util.state.InscripcionState;
import g41.si2022.util.state.StateUtilities;
import lombok.Data;

@Getter @Setter @Data
public class InscripcionDTO {

    private String
    	id,
    	fecha,
    	pagado,
    	curso_id,
		cancelada,
    	alumno_id,
    	alumno_nombre,
		alumno_apellidos,
    	curso_coste,
    	curso_nombre,
    	grupo_id,
		entidad_nombre;
    private InscripcionState estado;

	public void updateEstado(LocalDate today) {
		this.estado = StateUtilities.getInscripcionState(this, today);
	}

}
