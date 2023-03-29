package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class ProfesorDTO {
	private String
		nombre, apellidos,
		dni, id,
		email,
		telefono,
		remuneracion, // Necesario para almacenar remuneración al registrar cursos.
		direccion;

}
