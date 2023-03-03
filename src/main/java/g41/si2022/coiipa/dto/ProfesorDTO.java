package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class ProfesorDTO {
	private int id;
	private String
		nombre, apellidos,
		dni,
		email,
		telefono,
		direccion;
	// La remuneracion es por curso, es neceasaria para que se pueda
	// generar la columa extra en la JTable de RegistrarCurso
	private Double remuneracion;

	public ProfesorDTO() { }

	public ProfesorDTO (int id, String nombre, String apellidos, String dni, String email, String telefono, String direccion) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.email = email;
		this.telefono = telefono;
		this.direccion = direccion;
		this.remuneracion = null;
	}
}
