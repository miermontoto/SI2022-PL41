package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class CancelacionDTO implements DTO {

	private String
		id,
		alumno_id,
		nombre,
		apellidos,
		coste,
		nombre_curso,
		inicio_curso;

}
