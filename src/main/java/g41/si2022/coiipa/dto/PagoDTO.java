package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class PagoDTO {

	private String
		id,
		importe,
		fecha,
		inscripcion_id;
		
	public PagoDTO() { }

}
