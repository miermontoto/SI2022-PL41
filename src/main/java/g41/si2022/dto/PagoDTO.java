package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class PagoDTO implements DTO {

	private String
		id,
		importe,
		importedevuelto,
		fecha,
		inscripcion_id;

}
