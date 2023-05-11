package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class ProfesorDTO implements DTO {
	private String
		nombre, apellidos,
		dni, id,
		email,
		telefono,
		remuneracion, // Necesario para almacenar remuneración al registrar cursos.
		direccion;
	
	public ProfesorDTO () { }
	
	public ProfesorDTO (String nombre, String apellidos, String dni, String email, String direccion, String telefono) {
		this.setNombre(nombre);
		this.setApellidos(apellidos);
		this.setDni(dni);
		this.setEmail(email);
		this.setDireccion(direccion);
		this.setTelefono(telefono);
	}

	@Override
	public String toString() {
		return this.nombre + " " + this.apellidos;
	}
}
