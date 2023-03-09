package g41.si2022.coiipa.dto;

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
		remuneracion,
		direccion;
	// La remuneracion es por curso, es neceasaria para que se pueda
	// generar la columa extra en la JTable de RegistrarCurso

}
