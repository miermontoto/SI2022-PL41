package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class CursoDTO {

	private String
		nombre,
		descripcion,
		coste,
		start_inscr,
		end_inscr,
		start,
		end,
		id,
		plazas,
		cursoEstado,	// Annadido por StateUtilities
		plazas_libres, // Annadido por InscribirUsuario
		docente_id, // Annadido por RegistrarCurso
		ingresos, // Annadido por ConsultarIngresosGastos
		gastos, // Annadido por ConsultarIngresosGastos
		balance, // Annadido por ConsultarIngresosGastos
		pagoHighestFecha, // Annadido por ConsultarIngresosGastos
		pagoLowestFecha, // Annadido por ConsultarIngresosGastos
		inscripcion_fecha, // Annadido por consultarCursos
		inscripcion_alumno, // Annadido por consultarCursos
		pago_importe_devuelto; // Annadido por consultarCursos


	private g41.si2022.util.state.InscripcionState inscripcion_estado; // Annadido por consultarCursos
	private g41.si2022.util.state.CursoState estado; // Annadido por ?

	public CursoDTO() { }
}
