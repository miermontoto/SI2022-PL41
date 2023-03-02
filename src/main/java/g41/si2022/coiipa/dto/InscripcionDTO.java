package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InscripcionDTO {
    public String fecha;
    public String coste;
    public String estado;

    public InscripcionDTO() {}

    public InscripcionDTO(String fecha, String coste, String estado) {
        this.fecha = fecha;
        this.coste = coste;
        this.estado = estado;
    }
}
