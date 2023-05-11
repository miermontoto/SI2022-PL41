package g41.si2022.dto;

import lombok.Setter;
import lombok.Getter;
import lombok.Data;

@Setter @Getter @Data
public class EntidadDTO implements DTO {
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private String importe;

    @Override
	public String toString() { return this.nombre; }
}
