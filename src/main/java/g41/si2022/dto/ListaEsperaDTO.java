package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class ListaEsperaDTO {

	private String
		id,
		nombre,
		apellidos,
		alumno_id,
		curso_id,
		inscripcion_id,
		en_cola,
		coste,
		fecha_entrada;
}
