package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class ListaEsperaDTO {

	private String
		id,
		alumno_id,
		curso_id,
		en_cola,
		fecha_entrada;
}
