package g41.si2022.coiipa.dto;

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
		docente_id, // Annadido por RegistrarCurso
		ingresos, // Annadido por ConsultarIngresosGastos
		gastos, // Annadido por ConsultarIngresosGastos
		balance; // Annadido por ConsultarIngresosGastos

	private g41.si2022.util.CursoState estado; // Annadido por ?

	public CursoDTO() { }

	public static String getSqlQuery() { return "select * from curso order by id asc"; }
}
