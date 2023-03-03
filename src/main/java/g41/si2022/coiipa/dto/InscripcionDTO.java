package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter @Setter @Data
public class InscripcionDTO {

    private String fecha;
    private String estado;

    public InscripcionDTO() {}

    public InscripcionDTO(String fecha, String estado) {
        this.fecha = fecha;
        this.estado = estado;
    }

    public static String getSqlQuery() { return "select * from inscripcion order by id asc"; }
}
