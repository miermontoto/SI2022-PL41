package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter @Setter @Data
public class InscripcionDTO {

    private String fecha;
    private String estado;
    private String prueba;

    public InscripcionDTO() {}

    public InscripcionDTO(String fecha, String coste, String estado) {
        this.fecha = fecha;
        this.estado = estado;
    }
}
