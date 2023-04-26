package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;


import java.time.LocalDate;

import g41.si2022.util.state.CursoState;
import g41.si2022.util.state.InscripcionState;
import g41.si2022.util.state.StateUtilities;

@Setter @Getter @Data
public class CursoDTO implements DTO {

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
		plazas_libres, // Annadido por InscribirUsuario
		ingresos, // Annadido por ConsultarIngresosGastos
		gastos, // Annadido por ConsultarIngresosGastos
		balance, // Annadido por ConsultarIngresosGastos
		pagoHighestFecha, // Annadido por ConsultarIngresosGastos
		pagoLowestFecha, // Annadido por ConsultarIngresosGastos
		pago_importe_devuelto; // Annadido por consultarCursos 


	private InscripcionState inscripcion_estado; // Annadido por consultarCursos
	private CursoState estado; // Annadido por ?

	// private boolean isCancelled = false; // Annadido por gestionarCursos

	public CursoState updateEstado(LocalDate today) {
		this.estado = StateUtilities.getCursoState(this.getId(), today);
		return this.estado;
	}


	@Override
	public String toString() {
		return this.getNombre();
	}
}
