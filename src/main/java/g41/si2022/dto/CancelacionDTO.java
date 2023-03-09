package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class CancelacionDTO {

	private int id;
	private int alumno_id;
	private String nombre;
	private String coste;
	private String nombre_curso;
	private String inicio_curso;

}
