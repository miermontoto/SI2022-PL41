package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;


import java.time.LocalDate;

import g41.si2022.util.state.CursoState;
import g41.si2022.util.state.CursoType;
import g41.si2022.util.state.InscripcionState;
import g41.si2022.util.state.StateUtilities;

@Setter @Getter @Data
public class CursoDTO implements DTO {

	private String
		nombre,
		descripcion,
		coste,
		estado,
		start_inscr,
		end_inscr,
		start,
		end,
		id,
		plazas,
		plazas_libres, // Annadido por InscribirUsuario
		ocupadas,
		ingresos, // Annadido por ConsultarIngresosGastos
		gastos, // Annadido por ConsultarIngresosGastos
		balance, // Annadido por ConsultarIngresosGastos
		pagoHighestFecha, // Annadido por ConsultarIngresosGastos
		pagoLowestFecha, // Annadido por ConsultarIngresosGastos
		pago_importe_devuelto, // Annadido por consultarCursos
		entidad_id; // Annadido por RegistrarCursos

	private String e_nombre;
	private String importe; // Necesario para almacenar importe (a pagar a empresa) al RegistrarCursos
	private InscripcionState inscripcion_estado; // Annadido por consultarCursos
	private CursoState state; // Annadido por ?
	private CursoType tipo;

	// private boolean isCancelled = false; // Annadido por gestionarCursos

	public CursoState updateState(LocalDate today) {
		this.state = StateUtilities.getCursoState(this, today);
		return this.state;
	}

	public CursoType updateType() {
		this.tipo = StateUtilities.getCursoType(this);
		return this.tipo;
	}

	public void setImporte(String value) {
		if(value == null) {
			this.importe = null;
			return;
		}
		this.importe = value.equals("") ? null : value;
	}

	@Override
	public String toString() {
		return this.getNombre();
	}
}
