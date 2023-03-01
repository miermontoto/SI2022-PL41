package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ProfesorDTO {
	private String
		id,
		nombre, apellidos,
		dni,
		email,
		telefono,
		direccion;

	public ProfesorDTO() { }

	public ProfesorDTO(String id, String nombre, String apellidos, String dni, String email, String telefono, String direccion) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.email = email;
		this.telefono = telefono;
		this.direccion = direccion;
	}
}
