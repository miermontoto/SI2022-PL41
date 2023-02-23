package g41.SI2022.coiipa.registrarCurso;

public class ProfesorDisplayDTO {
	private String
		id,
		nombre, apellido,
		dni,
		email,
		telefono,
		direccion;
	
	public ProfesorDisplayDTO () { }
	public ProfesorDisplayDTO (String id, String nombre, String apellido, String dni, String email, String telefono, String direccion) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.email = email;
		this.telefono = telefono;
		this.direccion = direccion;
	}
	
	public String getId () { return this.id; }
	public String getNombre () { return this.nombre; }
	public String getApellido () { return this.apellido; }
	public String getDni () { return this.dni; }
	public String getEmail () { return this.email; }
	public String getTelefono () { return this.telefono; }
	public String getDireccion () { return this.direccion; }
	
}
