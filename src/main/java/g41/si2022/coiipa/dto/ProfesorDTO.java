package g41.si2022.coiipa.dto;

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

	public ProfesorDTO () { }

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

	public int getId () { return this.id; }
	public String getNombre () { return this.nombre; }
	public String getApellidos () { return this.apellidos; }
	public String getDni () { return this.dni; }
	public String getEmail () { return this.email; }
	public String getTelefono () { return this.telefono; }
	public String getDireccion () { return this.direccion; }
	public Double getRemuneracion () { return this.remuneracion; }

	public void setId (int id) { this.id = id; }
	public void setNombre (String nombre) { this.nombre = nombre; }
	public void setApellidos (String apellido) { this.apellidos = apellido; }
	public void setDni (String dni) { this.dni = dni; }
	public void setEmail (String email) { this.email = email; }
	public void setTelefono (String telefono) { this.telefono = telefono; }
	public void setDireccion (String direccion) { this.direccion = direccion; }
	public void setRemuneracion (Double remuneracion) { this.remuneracion = remuneracion; }

}