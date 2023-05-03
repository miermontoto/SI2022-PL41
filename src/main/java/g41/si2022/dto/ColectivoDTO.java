package g41.si2022.dto;

import lombok.Setter;
import lombok.Getter;
import lombok.Data;

@Setter @Getter @Data
public class ColectivoDTO implements DTO {
	private String
		nombre,
		coste,
		id;

	@Override
	public String toString() { return String.format("%s (%s€)", this.nombre, this.coste); }
}
